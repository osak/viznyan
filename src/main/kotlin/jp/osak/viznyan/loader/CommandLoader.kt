package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.command.AddCircle
import jp.osak.viznyan.rendering.command.AddEdge
import jp.osak.viznyan.rendering.command.AddLine
import jp.osak.viznyan.rendering.command.AddNode
import jp.osak.viznyan.rendering.command.AddRectangle
import jp.osak.viznyan.rendering.command.AddText
import jp.osak.viznyan.rendering.command.Command
import jp.osak.viznyan.rendering.command.DeleteEdge
import jp.osak.viznyan.rendering.command.DeleteNode
import jp.osak.viznyan.rendering.command.Frame

class CommandLoader {
    fun readFrame(tokenizer: Tokenizer): Frame? {
        val nCommands = tokenizer.readInt() ?: return null
        tokenizer.expectNewLine()

        val commands = mutableListOf<Command>()
        repeat(nCommands) {
            commands.add(readCommand(tokenizer))
        }

        return Frame(commands)
    }

    private fun readCommand(tokenizer: Tokenizer): Command {
        val type = tokenizer.readToken()
        return when (type) {
            "add_circle" -> readAddCircle(tokenizer)
            "add_rect" -> readAddRectangle(tokenizer)
            "add_line" -> readAddLine(tokenizer)
            "add_text" -> readAddText(tokenizer)
            "add_node" -> readAddNode(tokenizer)
            "add_node_pos" -> readAddNodePos(tokenizer)
            "add_edge" -> readAddEdge(tokenizer)
            "delete_node" -> readDeleteNode(tokenizer)
            "delete_edge" -> readDeleteEdge(tokenizer)
            else -> throw InvalidFormatException("Unknown command type: '$type'")
        }
    }

    private fun readAddCircle(tokenizer: Tokenizer): AddCircle {
        val id = tokenizer.readIntOrThrow("id")
        val x = tokenizer.readIntOrThrow("x")
        val y = tokenizer.readIntOrThrow("y")
        val radius = tokenizer.readIntOrThrow("radius")
        tokenizer.expectNewLine()
        return AddCircle(id, x, y, radius)
    }

    private fun readAddRectangle(tokenizer: Tokenizer): AddRectangle {
        val id = tokenizer.readIntOrThrow("id")
        val x1 = tokenizer.readIntOrThrow("x1")
        val y1 = tokenizer.readIntOrThrow("y1")
        val x2 = tokenizer.readIntOrThrow("x2")
        val y2 = tokenizer.readIntOrThrow("y2")
        tokenizer.expectNewLine()
        return AddRectangle(id, x1, y1, x2, y2)
    }

    private fun readAddLine(tokenizer: Tokenizer): AddLine {
        val id = tokenizer.readIntOrThrow("id")
        val x1 = tokenizer.readIntOrThrow("x1")
        val y1 = tokenizer.readIntOrThrow("y1")
        val x2 = tokenizer.readIntOrThrow("x2")
        val y2 = tokenizer.readIntOrThrow("y2")
        tokenizer.expectNewLine()
        return AddLine(id, x1, y1, x2, y2)
    }

    private fun readAddText(tokenizer: Tokenizer): AddText {
        val id = tokenizer.readIntOrThrow("id")
        val x = tokenizer.readIntOrThrow("x")
        val y = tokenizer.readIntOrThrow("y")
        val text = tokenizer.readUntilNewLine()
            ?: throw InvalidFormatException("Premature end of input: <text> is missing")
        tokenizer.expectNewLine()
        return AddText(id, x, y, text)
    }

    private fun readAddNode(tokenizer: Tokenizer): AddNode {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val nodeId = tokenizer.readIntOrThrow("nodeId")
        tokenizer.expectNewLine()
        return AddNode(graphId, nodeId)
    }

    private fun readAddNodePos(tokenizer: Tokenizer): AddNode {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val nodeId = tokenizer.readIntOrThrow("nodeId")
        val x = tokenizer.readIntOrThrow("x")
        val y = tokenizer.readIntOrThrow("y")
        tokenizer.expectNewLine()
        return AddNode(graphId, nodeId, x / 72.0, y / 72.0)
    }

    private fun readAddEdge(tokenizer: Tokenizer): AddEdge {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val from = tokenizer.readIntOrThrow("from")
        val to = tokenizer.readIntOrThrow("to")
        tokenizer.expectNewLine()
        return AddEdge(graphId, from, to)
    }

    private fun readDeleteNode(tokenizer: Tokenizer): DeleteNode {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val nodeId = tokenizer.readIntOrThrow("nodeId")
        tokenizer.expectNewLine()
        return DeleteNode(graphId, nodeId)
    }

    private fun readDeleteEdge(tokenizer: Tokenizer): DeleteEdge {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val from = tokenizer.readIntOrThrow("from")
        val to = tokenizer.readIntOrThrow("to")
        tokenizer.expectNewLine()
        return DeleteEdge(graphId, from, to)
    }

    private fun Tokenizer.readIntOrThrow(label: String): Int {
        return this.readInt()
            ?: throw InvalidFormatException("Premature end of input: <$label> is missing")
    }
}