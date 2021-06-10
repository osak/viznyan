package jp.osak.viznyan.scene

import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import jp.osak.viznyan.loader.CompProgStateLoader
import jp.osak.viznyan.rendering.State
import java.io.File

class VisualizerSceneManager(stage: Stage) {
    val scene: Scene
    private val canvas: Canvas
    private var states: List<State> = listOf()
    private var frame: Int = 0

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
            frame = 0
        }

        val borderPane = BorderPane()
        canvas = Canvas(500.0, 500.0)
        canvas.widthProperty().addListener { _ -> repaint() }
        canvas.heightProperty().addListener { _ -> repaint() }
        borderPane.center = canvas

        val controlPane = HBox()
        val startButton = Button("Start")
        startButton.onAction = EventHandler {
            frame = 0
            val timer = object : AnimationTimer() {
                override fun handle(now: Long) {
                    frame++
                    if (frame >= states.size) {
                        this.stop()
                        return
                    }
                    repaint()
                }
            }
            timer.start()
        }
        controlPane.children.add(startButton)
        borderPane.bottom = controlPane

        root.children.add(borderPane)

        scene = Scene(root)
    }

    fun repaint() {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        //gc.strokeLine(0.0, 0.0, canvas.width, canvas.height)

        if (frame >= 0 && frame < states.size) {
            states[frame].render(gc)
        }
    }

    private fun loadState(file: File) {
        val loader = CompProgStateLoader()
        states = file.inputStream().use {
            loader.load(it)
        }
        repaint()
    }
}