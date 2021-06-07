package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.Shape
import jp.osak.viznyan.rendering.State
import java.io.InputStream

class CompProgStateLoader {
    fun load(inputStream: InputStream): State {
        return inputStream.bufferedReader().use { reader ->
            Tokenizer(reader).use {
                readState(it)
            }
        }
    }

    /**
     * Read state object.
     *
     * Format:
     * ```
     * <n-circles>
     * [circle...]
     * <n-rectangles>
     * [rectangle...]
     * <n-lines>
     * [line...]
     * ```
     */
    private fun readState(tokenizer: Tokenizer): State {
        val shapes = mutableListOf<Shape>()

        tokenizer.skipNonNewLineSpace()
        val nCircles = tokenizer.readIntOrThrow("n-circles")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        repeat(nCircles) {
            shapes.add(readCircle(tokenizer))
        }

        tokenizer.skipNonNewLineSpace()
        val nRectangles = tokenizer.readIntOrThrow("n-rectangles")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        repeat(nRectangles) {
            shapes.add(readRectangle(tokenizer))
        }

        tokenizer.skipNonNewLineSpace()
        val nLines = tokenizer.readIntOrThrow("n-lines")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        repeat(nLines) {
            shapes.add(readLine(tokenizer))
        }

        return State(shapes)
    }

    /**
     * Read a Circle object.
     *
     * Format:
     * ```
     * <id> <x> <y> <radius> '\n'
     * ```
     */
    private fun readCircle(tokenizer: Tokenizer): Circle {
        tokenizer.skipNonNewLineSpace()
        val id = tokenizer.readIntOrThrow("id")

        tokenizer.skipNonNewLineSpace()
        val x = tokenizer.readIntOrThrow("x")

        tokenizer.skipNonNewLineSpace()
        val y = tokenizer.readIntOrThrow("y")

        tokenizer.skipNonNewLineSpace()
        val radius = tokenizer.readIntOrThrow("radius")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        return Circle(id, x.toDouble(), y.toDouble(), radius.toDouble())
    }

    /**
     * Read a Rectangle object.
     *
     * Format:
     * ```
     * <id> <x1> <y1> <x2> <y2> '\n'
     * ```
     */
    private fun readRectangle(tokenizer: Tokenizer): Rectangle {
        tokenizer.skipNonNewLineSpace()
        val id = tokenizer.readIntOrThrow("id")

        tokenizer.skipNonNewLineSpace()
        val x1 = tokenizer.readIntOrThrow("x1")

        tokenizer.skipNonNewLineSpace()
        val y1 = tokenizer.readIntOrThrow("y1")

        tokenizer.skipNonNewLineSpace()
        val x2 = tokenizer.readIntOrThrow("x2")

        tokenizer.skipNonNewLineSpace()
        val y2 = tokenizer.readIntOrThrow("y2")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        return Rectangle(id, x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())
    }

    /**
     * Read a Line object.
     *
     * Format:
     * ```
     * <id> <x1> <y1> <x2> <y2> '\n'
     * ```
     */
    private fun readLine(tokenizer: Tokenizer): Line {
        tokenizer.skipNonNewLineSpace()
        val id = tokenizer.readIntOrThrow("id")

        tokenizer.skipNonNewLineSpace()
        val x1 = tokenizer.readIntOrThrow("x1")

        tokenizer.skipNonNewLineSpace()
        val y1 = tokenizer.readIntOrThrow("y1")

        tokenizer.skipNonNewLineSpace()
        val x2 = tokenizer.readIntOrThrow("x2")

        tokenizer.skipNonNewLineSpace()
        val y2 = tokenizer.readIntOrThrow("y2")

        tokenizer.skipNonNewLineSpace()
        tokenizer.expectNewLine()
        return Line(id, x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())
    }

    private fun Tokenizer.readIntOrThrow(label: String): Int {
        return this.readInt()
            ?: throw InvalidFormatException("Premature end of input: <$label> is missing")
    }
}

class InvalidFormatException(message: String) : RuntimeException(message)