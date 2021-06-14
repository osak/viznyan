package jp.osak.viznyan.scene

import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.Slider
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import jp.osak.viznyan.loader.CommandLoader
import jp.osak.viznyan.loader.Tokenizer
import jp.osak.viznyan.rendering.command.AnimationRunner
import jp.osak.viznyan.streaming.SocketStreamer
import java.io.File

class VisualizerSceneManager(stage: Stage) {
    val scene: Scene
    private val canvas: Canvas
    private var frame: IntegerProperty = SimpleIntegerProperty()
    private var maxFrame: IntegerProperty = SimpleIntegerProperty()
    private var animationRunner: AnimationRunner = AnimationRunner()
    private var animationTimer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            if (frame.get() + 1 > animationRunner.maxFrame) {
                //this.stop()
                return
            }
            frame.set(frame.value + 1)
        }
    }

    init {
        val root = VBox()
        root.isFillWidth = true

        val menubar = MenuBar()
        val fileMenu = Menu("File")
        val openFile = MenuItem("Open")
        fileMenu.items.add(openFile)
        menubar.menus.add(fileMenu)
        root.children.add(menubar)

        val fileChooser = FileChooser()
        fileChooser.initialDirectory = File(System.getProperty("user.dir"));
        openFile.onAction = EventHandler {
            val file = fileChooser.showOpenDialog(stage)
            loadState(file)
            frame.set(0)
            maxFrame.set(animationRunner.maxFrame)
            repaint()
        }

        val borderPane = BorderPane()
        canvas = Canvas(500.0, 500.0)
        canvas.widthProperty().addListener { _ -> repaint() }
        canvas.heightProperty().addListener { _ -> repaint() }
        frame.addListener { _ -> repaint() }
        borderPane.center = canvas

        val controlPane = HBox(8.0)
        val startButton = Button("Start")
        startButton.onAction = EventHandler {
            animationTimer.start()
        }
        val stopButton = Button("Stop")
        stopButton.onAction = EventHandler {
            animationTimer.stop()
        }
        controlPane.children.addAll(startButton, stopButton)

        val slider = Slider()
        slider.valueProperty().bindBidirectional(frame)
        slider.min = 0.0
        slider.maxProperty().bind(maxFrame)
        slider.majorTickUnit = 1.0
        slider.isSnapToTicks = true
        controlPane.children.add(slider)

        val frameLabel = Label()
        frame.addListener { _ -> frameLabel.text = "${frame.value + 1} / ${maxFrame.value + 1}" }
        maxFrame.addListener { _ -> frameLabel.text = "${frame.value + 1} / ${maxFrame.value + 1}" }
        controlPane.children.add(frameLabel)

        val streamingButton = Button("Stream")
        streamingButton.onAction = EventHandler {
            animationRunner = AnimationRunner()
            maxFrame.set(0)
            frame.set(0)

            animationTimer.start()
            SocketStreamer(4444) {
                Platform.runLater {
                    animationRunner.addFrame(it)
                    maxFrame.set(maxFrame.value + 1)
                }
            }
        }
        controlPane.children.add(streamingButton)

        borderPane.bottom = controlPane

        root.children.add(borderPane)

        scene = Scene(root)
    }

    fun repaint() {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        //gc.strokeLine(0.0, 0.0, canvas.width, canvas.height)

        val currentFrame = frame.get()
        if (currentFrame in 0..animationRunner.maxFrame) {
            animationRunner.getState(currentFrame).render(gc)
        }
    }

    private fun loadState(file: File) {
        val loader = CommandLoader()
        file.inputStream().use {
            val tokenizer = Tokenizer(it.bufferedReader())
            while (true) {
                val frame = loader.readFrame(tokenizer) ?: break
                animationRunner.addFrame(frame)
            }
        }
    }
}
