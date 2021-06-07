package jp.osak.viznyan.loader

import jp.osak.viznyan.rendering.Circle
import jp.osak.viznyan.rendering.Line
import jp.osak.viznyan.rendering.Rectangle
import jp.osak.viznyan.rendering.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class CompProgStateLoaderTest {

    @Test
    fun load() {
        val data = """
            2
            1 10 20 5
            2 30 40 6
            2
            3 10 20 30 40
            4 50 60 70 80
            2
            5 10 20 30 40
            6 50 60 70 80
            
        """.trimIndent()

        val loader = CompProgStateLoader()
        val state = loader.load(ByteArrayInputStream(data.toByteArray()))
        assertEquals(
            State(
                listOf(
                    Circle(1, 10.0, 20.0, 5.0),
                    Circle(2, 30.0, 40.0, 6.0),
                    Rectangle(3, 10.0, 20.0, 30.0, 40.0),
                    Rectangle(4, 50.0, 60.0, 70.0, 80.0),
                    Line(5, 10.0, 20.0, 30.0, 40.0),
                    Line(6, 50.0, 60.0, 70.0, 80.0),
                )
            ), state
        )
    }
}