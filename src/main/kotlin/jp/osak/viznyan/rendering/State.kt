package jp.osak.viznyan.rendering

import javafx.scene.canvas.GraphicsContext

class State {
    private val shapes: MutableMap<Int, Shape> = mutableMapOf()

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
            }
        }
    }
}