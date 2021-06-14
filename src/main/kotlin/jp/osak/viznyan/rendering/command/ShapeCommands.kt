package jp.osak.viznyan.rendering.command

data class AddCircle (
    val id: Int,
    val x: Int,
    val y: Int,
    val radius: Int,
) : Command

data class MoveCircle (
    val id: Int,
    val x: Int,
    val y: Int,
) : Command

data class AddRectangle (
    val id: Int,
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
) : Command

data class AddLine (
    val id: Int,
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
) : Command

data class AddText (
    val id: Int,
    val x: Int,
    val y: Int,
    val text: String,
) : Command