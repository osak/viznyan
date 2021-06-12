package jp.osak.viznyan.rendering.command

import jp.osak.viznyan.rendering.DotGraph
import jp.osak.viznyan.rendering.State
import java.lang.RuntimeException

class AnimationRunner(private val frames: List<Frame>) {
    private var state: State = State()
    private var currentFrame: Int = -1

    fun runStep(): State? {
        if (currentFrame == frames.size - 1) {
            return null
        }

        val frame = frames[currentFrame+1]
        for (command in frame.commands) {
            when (command) {
                is AddNode -> {
                    val graph = getGraph(command.graphId)
                    graph.addNode(command.nodeId.toString())
                }
                is AddEdge -> {
                    val graph = getGraph(command.graphId)
                    graph.addEdge(command.from.toString(), command.to.toString())
                }
            }
        }

        currentFrame++
        return state
    }

    private fun getGraph(graphId: Int): DotGraph {
        val maybeGraph = state.get(graphId)
        if (maybeGraph == null) {
            val graph = DotGraph(graphId, 500)
            state.put(graph)
            return graph
        }
        return maybeGraph as? DotGraph
            ?: throw TypeMismatchException("Shape id $graphId is expected to be DotGraph, but actually ${maybeGraph::class}")
    }
}

class TypeMismatchException(message: String) : RuntimeException(message)