package jp.osak.viznyan.rendering.command

import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.DotGraph
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.State
import jp.osak.viznyan.rendering.Text
import java.lang.RuntimeException

class AnimationRunner() {
    private val frames: MutableList<Frame> = mutableListOf()
    private var state: State = State()
    private var currentFrame: Int = -1

    val maxFrame: Int
        get() = frames.size - 1

    fun runStep(): State? {
        if (currentFrame == frames.size - 1) {
            return null
        }

        val frame = frames[currentFrame+1]
        for (command in frame.commands) {
            when (command) {
                is AddCircle -> {
                    state.put(Circle(command.id, command.x.toDouble(), command.y.toDouble(), command.radius.toDouble()))
                }
                is AddRectangle -> {
                    state.put(Rectangle(command.id, command.x1.toDouble(), command.y1.toDouble(), command.x2.toDouble(), command.y2.toDouble()))
                }
                is AddLine -> {
                    state.put(Line(command.id, command.x1.toDouble(), command.y1.toDouble(), command.x2.toDouble(), command.y2.toDouble()))
                }
                is AddText -> {
                    state.put(Text(command.id, command.x.toDouble(), command.y.toDouble(), command.text))
                }
                is AddNode -> {
                    val graph = getGraph(command.graphId)
                    if (command.x != null && command.y != null) {
                        graph.addNode(command.nodeId.toString(), command.x, command.y)
                    } else {
                        graph.addNode(command.nodeId.toString())
                    }
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

    fun addFrame(frame: Frame) {
        frames.add(frame)
    }

    fun getState(frame: Int): State {
        require (frame in 0..frames.size-1) { "frame is out of range: $frame" }

        if (currentFrame > frame) {
            state = State()
            currentFrame = -1
        }
        while (currentFrame < frame) {
            runStep()
        }
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