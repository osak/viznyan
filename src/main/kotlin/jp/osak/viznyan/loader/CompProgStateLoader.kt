package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.Shape
import jp.osak.viznyan.rendering.State
import jp.osak.viznyan.rendering.Text
import java.io.InputStream

class CompProgStateLoader {
    fun loadOne(tokenizer: Tokenizer): State? {
        if (tokenizer.isEof()) {
            return null
        }
        return readState(tokenizer)
    }

    fun load(inputStream: InputStream): List<State> {
        return inputStream.bufferedReader().use { reader ->
            val states = mutableListOf<State>()
            Tokenizer(reader).use {
                while (true) {
                    it.skipSeparators()
                    if (it.isEof()) {
                        break
                    }
                    states.add(readState(it))
                }
            }
            states
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
     * <n-texts>
     * [text...]
     * ```
     */
    private fun readState(tokenizer: Tokenizer): State {
        val shapes = mutableListOf<Shape>()

        readRepeatedBlock("circles", tokenizer) {
            shapes.add(readCircle(tokenizer))
        }
        readRepeatedBlock("rectangles", tokenizer) {
            shapes.add(readRectangle(tokenizer))
        }
        readRepeatedBlock("lines", tokenizer) {
            shapes.add(readLine(tokenizer))
        }
        readRepeatedBlock("texts", tokenizer) {
            shapes.add(readText(tokenizer))
        }

        return State(shapes)
    }

    private fun readRepeatedBlock(label: String, tokenizer: Tokenizer, body: (Tokenizer) -> Unit) {
        val n = tokenizer.readIntOrThrow("n-$label")
        tokenizer.expectNewLine()

        repeat(n) {
            body(tokenizer)
        }
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
        val id = tokenizer.readIntOrThrow("id")
        val x = tokenizer.readIntOrThrow("x")
        val y = tokenizer.readIntOrThrow("y")
        val radius = tokenizer.readIntOrThrow("radius")
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
        val id = tokenizer.readIntOrThrow("id")
        val x1 = tokenizer.readIntOrThrow("x1")
        val y1 = tokenizer.readIntOrThrow("y1")
        val x2 = tokenizer.readIntOrThrow("x2")
        val y2 = tokenizer.readIntOrThrow("y2")
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
        val id = tokenizer.readIntOrThrow("id")
        val x1 = tokenizer.readIntOrThrow("x1")
        val y1 = tokenizer.readIntOrThrow("y1")
        val x2 = tokenizer.readIntOrThrow("x2")
        val y2 = tokenizer.readIntOrThrow("y2")

        tokenizer.expectNewLine()
        return Line(id, x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())
    }

    /**
     * Reads a Text object.
     *
     * Format:
     * ```
     * <id> <x> <y> <text>'\n'
     */
    private fun readText(tokenizer: Tokenizer): Text {
        val id = tokenizer.readIntOrThrow("id")
        val x = tokenizer.readIntOrThrow("x")
        val y = tokenizer.readIntOrThrow("y")
        val text = tokenizer.readUntilNewLine()
            ?: throw InvalidFormatException("Premature end of input: <text> is missing")

        tokenizer.expectNewLine()
        return Text(id, x.toDouble(), y.toDouble(), text)
    }

    private fun Tokenizer.readIntOrThrow(label: String): Int {
        return this.readInt()
            ?: throw InvalidFormatException("Premature end of input: <$label> is missing")
    }
}

class InvalidFormatException(message: String) : RuntimeException(message)