package jp.osak.viznyan.rendering.command

data class AddNode (
    val graphId: Int,
    val nodeId: Int,
    val x: Double? = null,
    val y: Double? = null,
) : Command

data class AddEdge (
    val graphId: Int,
    val from: Int,
    val to: Int
) : Command

data class DeleteNode(
    val graphId: Int,
    val nodeId: Int,
) : Command

data class DeleteEdge (
    val graphId: Int,
    val from: Int,
    val to: Int
) : Command