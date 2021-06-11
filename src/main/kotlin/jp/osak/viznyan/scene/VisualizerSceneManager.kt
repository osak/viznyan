package jp.osak.viznyan.scene

import javafx.animation.Animation
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.StringProperty
import javafx.beans.property.StringPropertyBase
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
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
import jp.osak.viznyan.loader.CompProgStateLoader
import jp.osak.viznyan.rendering.State
import jp.osak.viznyan.streaming.SocketStreamer
import java.io.File

class VisualizerSceneManager(stage: Stage) {
    val scene: Scene
    private val canvas: Canvas
    private var states: ListProperty<State> = SimpleListProperty()
    private var frame: IntegerProperty = SimpleIntegerProperty()
    private var animationTimer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            if (frame.get() + 1 >= states.size) {
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
        slider.maxProperty().bind(states.sizeProperty().subtract(1))
        slider.majorTickUnit = 1.0
        slider.isSnapToTicks = true
        controlPane.children.add(slider)

        val frameLabel = Label()
        frame.addListener { _ -> frameLabel.text = "${frame.value + 1} / ${states.size}" }
        states.addListener(ListChangeListener { frameLabel.text = "${frame.value + 1} / ${states.size}" })
        controlPane.children.add(frameLabel)

        val streamingButton = Button("Stream")
        streamingButton.onAction = EventHandler {
            states.set(FXCollections.observableArrayList())
            animationTimer.start()
            SocketStreamer(4444) {
                Platform.runLater {
                    states.add(it)
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
        if (currentFrame >= 0 && currentFrame < states.size) {
            states[currentFrame].render(gc)
        }
    }

    private fun loadState(file: File) {
        val loader = CompProgStateLoader()
        file.inputStream().use {
            states.set(FXCollections.observableArrayList(loader.load(it)))
        }
        repaint()
    }
}
