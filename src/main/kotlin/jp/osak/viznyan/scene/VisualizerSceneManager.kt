package jp.osak.viznyan.scene

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.State

class VisualizerSceneManager {
    val scene: Scene
    private val canvas: Canvas
    private val state: State = State()

    init {
        val root = StackPane()
        canvas = Canvas()
        canvas.widthProperty().bind(root.widthProperty())
        canvas.heightProperty().bind(root.heightProperty())
        canvas.widthProperty().addListener { _ -> repaint() }
        canvas.heightProperty().addListener { _ -> repaint() }
        root.children.add(canvas)

        scene = Scene(root)
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
}