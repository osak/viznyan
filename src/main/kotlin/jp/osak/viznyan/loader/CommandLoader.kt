package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.command.AddEdge
import jp.osak.viznyan.rendering.command.AddNode
import jp.osak.viznyan.rendering.command.Command
import jp.osak.viznyan.rendering.command.DeleteEdge
import jp.osak.viznyan.rendering.command.DeleteNode
import jp.osak.viznyan.rendering.command.Frame

class CommandLoader {
    fun readFrame(tokenizer: Tokenizer): Frame {
        val nCommands = tokenizer.readInt()
            ?: throw InvalidFormatException("Premature end of input: <nCommands> is missing")

        val commands = mutableListOf<Command>()
        repeat(nCommands) {
            commands.add(readCommand(tokenizer))
        }

        return Frame(commands)
    }

    private fun readCommand(tokenizer: Tokenizer): Command {
        val type = tokenizer.readToken()
        return when (type) {
            "add_node" -> readAddNode(tokenizer)
            "add_edge" -> readAddEdge(tokenizer)
            "delete_node" -> readDeleteNode(tokenizer)
            "delete_edge" -> readDeleteEdge(tokenizer)
            else -> throw InvalidFormatException("Unknown command type: '$type'")
        }
    }

    private fun readAddNode(tokenizer: Tokenizer): AddNode {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val nodeId = tokenizer.readIntOrThrow("nodeId")
        return AddNode(graphId, nodeId)
    }

    private fun readAddEdge(tokenizer: Tokenizer): AddEdge {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val from = tokenizer.readIntOrThrow("from")
        val to = tokenizer.readIntOrThrow("to")
        return AddEdge(graphId, from, to)
    }

    private fun readDeleteNode(tokenizer: Tokenizer): DeleteNode {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val nodeId = tokenizer.readIntOrThrow("nodeId")
        return DeleteNode(graphId, nodeId)
    }

    private fun readDeleteEdge(tokenizer: Tokenizer): DeleteEdge {
        val graphId = tokenizer.readIntOrThrow("graphId")
        val from = tokenizer.readIntOrThrow("from")
        val to = tokenizer.readIntOrThrow("to")
        return DeleteEdge(graphId, from, to)
    }

    private fun Tokenizer.readIntOrThrow(label: String): Int {
        return this.readInt()
            ?: throw InvalidFormatException("Premature end of input: <$label> is missing")
    }
}