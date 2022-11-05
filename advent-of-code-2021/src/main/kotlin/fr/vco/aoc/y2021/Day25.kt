package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day25")
    val floor = input.flatMap { line -> line.toList() }
    val neighboursIndexes =
        input.flatMapIndexed { y, line -> line.mapIndexed { x, _ -> input.getNeighborsIndexes(x, y) } }

    println("Part 1 : ${floor.findBlockingStep(neighboursIndexes)}")
}

const val WEST_DIR = 0
const val EAST_DIR = 1
const val SOUTH_DIR = 2
const val NORTH_DIR = 3

enum class Herd(val type: Char, val forward: Int, val back: Int) {
    EAST('>', EAST_DIR, WEST_DIR),
    SOUTH('v', SOUTH_DIR, NORTH_DIR)
}

private fun List<String>.convertCoord(x: Int, y: Int) = this.first().length * y + x
private fun List<String>.getNeighborsIndexes(x: Int, y: Int) = listOf(
    this.convertCoord(getCircularIndex(x - 1, this.first().length), y),
    this.convertCoord(getCircularIndex(x + 1, this.first().length), y),
    this.convertCoord(x, getCircularIndex(y + 1, size)),
    this.convertCoord(x, getCircularIndex(y - 1, size))
)

private fun getCircularIndex(i: Int, size: Int) = when {
    i >= size -> 0
    i < 0 -> size - 1
    else -> i
}

private fun List<Char>.findBlockingStep(neigbhours: List<List<Int>>): Int {
    var stepId = 0
    var current = this
    do {
        val last = current
        current = current.step(neigbhours)
        stepId++
    } while (current != last)
    return stepId
}

private fun List<Char>.step(neighbours: List<List<Int>>) =
    moveHerd(Herd.EAST, neighbours).moveHerd(Herd.SOUTH, neighbours)

private fun List<Char>.moveHerd(herd: Herd, neighbours: List<List<Int>>) = this.mapIndexed { i, cell ->
    when {
        cell == herd.type && this[neighbours[i][herd.forward]] == '.' -> '.'
        cell == '.' && this[neighbours[i][herd.back]] == herd.type -> herd.type
        else -> cell
    }
}