package jp.osak.viznyan.scene

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import jp.osak.viznyan.loader.CompProgStateLoader
import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.State
import java.io.File

class VisualizerSceneManager(stage: Stage) {
    val scene: Scene
    private val canvas: Canvas
    private var state: State = State()

    init {
        val root = VBox()
        val menubar = MenuBar()
        val fileMenu = Menu("File")
        val openFile = MenuItem("Open")
        fileMenu.items.add(openFile)
        menubar.menus.add(fileMenu)
        root.children.add(menubar)

        val fileChooser = FileChooser()
        openFile.onAction = EventHandler {
            val file = fileChooser.showOpenDialog(stage)
            loadState(file)
        }

        canvas = Canvas(500.0, 500.0)
        canvas.widthProperty().bind(root.widthProperty())
        canvas.heightProperty().bind(root.heightProperty())
        canvas.widthProperty().addListener { _ -> repaint() }
        canvas.heightProperty().addListener { _ -> repaint() }
        root.children.add(canvas)

        scene = Scene(root, 500.0, 500.0)
        state.set(listOf(
            Circle(1, 100.0, 100.0, 10.0),
            Rectangle(2, 150.0, 50.0, 200.0, 100.0),
            Line(3, 100.0, 100.0, 175.0, 75.0)
        ))
    }

    fun repaint() {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        state.render(gc)
    }

    private fun loadState(file: File) {
        val loader = CompProgStateLoader()
        state = file.inputStream().use {
            loader.load(it)
        }
        repaint()
    }
}