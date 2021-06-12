package jp.osak.viznyan.rendering

import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class DotGraph(override val id: Int, val width: Int) : Shape {
    private var graph: MutableGraph = Factory.mutGraph().graphAttrs().add("layout", "fdp")

    constructor(id: Int, width: Int, source: InputStream) : this(id, width) {
        val original = Parser().read(source)
        val buffer = ByteArrayOutputStream()
        Graphviz.fromGraph(original).width(width).render(Format.DOT).toOutputStream(buffer)
        graph = Parser().read(ByteArrayInputStream(buffer.toByteArray()))
    }

    fun addNode(name: String) {
        graph.add(Factory.mutNode(name).add(Label.of(name)))
    }

    fun addEdge(from: String, to: String): Boolean {
        val fromNode = graph.nodes().find { it.name().value() == from }
            ?: return false
        fromNode.addLink(to)
        return true
    }

    fun render(gc: GraphicsContext) {
        val buffer = ByteArrayOutputStream()
        Graphviz.fromGraph(graph).width(width).render(Format.PNG).toOutputStream(buffer)
        val image = Image(ByteArrayInputStream(buffer.toByteArray()))
        gc.drawImage(image, 0.0, 0.0)
    }
}