package fr.vco.aoc.y2025


fun main() {
    val input = readLines("Day12").split { it.isBlank() }
    val pieces = input.dropLast(1).map { it.toPiece() }
    val surfaces = input.last().map { it.toSurface() }

    val pieceSurface = pieces.first().height * pieces.first().width
    println("Part 1: ${surfaces.count { it.isValid(pieceSurface) }}")
}

data class Surface(val height: Int, val width: Int, val pieces: List<Int>) {
    fun isValid(pieceSurface: Int): Boolean {
        val maxPiece = height * width / pieceSurface
        val pieceCount = pieces.sum()
        return maxPiece >= pieceCount
    }
}

data class Piece(val shape: List<String>) {
    val height = shape.size
    val width = shape.first().length
}

fun String.toSurface(): Surface {
    val (dimension, pieces) = this.split(":")
    val (h, w) = dimension.split("x").map { it.toInt() }
    return Surface(h, w, pieces.trim().split(" ").map { it.toInt() })
}

fun List<String>.toPiece() = Piece(this.drop(1))
