package jp.osak.viznyan.rendering

interface Shape {
    val id: Int
}

data class Circle(
    override val id: Int,
    val x: Double,
    val y: Double,
    val radius: Double,
) : Shape

data class Rectangle(
    override val id: Int,
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
) : Shape

data class Line(
    override val id: Int,
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
) : Shape

data class Text(
    override val id: Int,
    val x: Double,
    val y: Double,
    val text: String
) : Shape