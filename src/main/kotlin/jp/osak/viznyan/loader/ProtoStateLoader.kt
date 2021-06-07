package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.Shape
import jp.osak.viznyan.rendering.State
import java.io.File

class ProtoStateLoader {
    fun loadFromFile(path: String): State {
        val protoState = File(path).inputStream().use {
            jp.osak.viznyan.proto.Shape.State.newBuilder().mergeFrom(it).build()
        }

        val shapes = mutableListOf<Shape>()
        shapes.addAll(protoState.circlesList.map { convertCircle(it) })
        shapes.addAll(protoState.rectanglesList.map { convertRectangle(it) })
        shapes.addAll(protoState.linesList.map { convertLine(it) })
        return State(shapes)
    }

    private fun convertCircle(protoCircle: jp.osak.viznyan.proto.Shape.Circle): Circle {
        return Circle(protoCircle.id, protoCircle.x.toDouble(), protoCircle.y.toDouble(), protoCircle.radius.toDouble())
    }

    private fun convertRectangle(protoRect: jp.osak.viznyan.proto.Shape.Rectangle): Rectangle {
        return Rectangle(
            protoRect.id,
            protoRect.x1.toDouble(),
            protoRect.y1.toDouble(),
            protoRect.x2.toDouble(),
            protoRect.y2.toDouble(),
        )
    }

    private fun convertLine(protoLine: jp.osak.viznyan.proto.Shape.Line): Line {
        return Line(
            protoLine.id,
            protoLine.x1.toDouble(),
            protoLine.y1.toDouble(),
            protoLine.x2.toDouble(),
            protoLine.y2.toDouble(),
        )
    }
}