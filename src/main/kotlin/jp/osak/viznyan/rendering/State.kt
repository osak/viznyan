package jp.osak.viznyan.rendering

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font

class State {
    private val shapes: MutableMap<Int, Shape> = mutableMapOf()

    constructor()

    constructor(init: Collection<Shape>) {
        set(init)
    }

    fun set(newValues: Collection<Shape>) {
        shapes.clear()
        for (shape in newValues) {
            shapes[shape.id] = shape
        }
    }

    fun render(gc: GraphicsContext) {
        for (id in shapes.keys.sorted()) {
            when (val shape = shapes[id]) {
                is Circle -> {
                    val x = shape.x - shape.radius
                    val y = shape.y - shape.radius
                    val w = shape.radius * 2
                    val h = shape.radius * 2
                    gc.strokeOval(x, y, w, h)
                }
                is Rectangle -> {
                    gc.strokeRect(shape.x1, shape.y1, shape.x2 - shape.x1, shape.y2 - shape.y1)
                }
                is Line -> {
                    gc.strokeLine(shape.x1, shape.y1, shape.x2, shape.y2)
                }
                is Text -> {
                    gc.textBaseline = VPos.TOP
                    gc.font = Font(12.0)
                    gc.fillText(shape.text, shape.x, shape.y)
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is State -> this.shapes.equals(other.shapes)
            else -> false
        }
    }

    override fun hashCode(): Int {
        return shapes.hashCode()
    }
}