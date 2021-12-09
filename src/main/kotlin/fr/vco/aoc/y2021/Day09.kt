package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day09")

    val floor = input.map { line -> line.map { it.digitToInt() } }
    println(floor)
    var res = 0
    floor.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            println("$x $y -> $c ")
            println(floor.getNeighbors(x, y))

            if (c < floor.getNeighbors(x, y).minOrNull()!!) {
                res += c + 1
            }
        }
    }
    println("Part 1: $res")
}

fun List<List<Int>>.getNeighbors(x: Int, y: Int) = listOfNotNull(
    this.getOrNull(y - 1)?.getOrNull(x),
    this.getOrNull(y + 1)?.getOrNull(x),
    this.getOrNull(y)?.getOrNull(x - 1),
    this.getOrNull(y)?.getOrNull(x + 1)
)