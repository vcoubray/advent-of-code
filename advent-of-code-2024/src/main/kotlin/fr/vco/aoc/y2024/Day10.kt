package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day10")

    val grid = input.map { line -> line.map { it.digitToInt() } }
    var hikingCount = 0
    grid.forEachIndexed { y, line ->
        line.forEachIndexed { x, n ->
            if (n == 0) {
                hikingCount += grid.countHiking(Position(x, y))
            }
        }
    }
    println(hikingCount)
}

operator fun <T> List<List<T>>.get(p: Position) = this[p.y][p.x]
fun <T> List<List<T>>.getOrNull(p: Position) = this.getOrNull(p.y)?.getOrNull(p.x)
operator fun <T> List<MutableList<T>>.set(p: Position, value: T) {
    this[p.y][p.x] = value
}

fun List<List<Int>>.countHiking(start: Position): Int {

    val toVisit = ArrayDeque<Position>().apply { add(start) }
    val visited = this.map { line -> line.map { false }.toMutableList() }
    visited[start] = true

    var hikingCount = 0
    while (toVisit.isNotEmpty()) {

        val curr = toVisit.removeFirst()
        if (this[curr] == 9) {
            hikingCount++
        }

        DIRECTION_MANHATTAN.map { curr + it }.filter { visited.getOrNull(it) == false }.forEach { n ->
            println("$curr (${this.getOrNull(curr)}) -> $n (${this.getOrNull(n)}) ")
            if (this.getOrNull(n) == this[curr] + 1) {
                toVisit.addLast(n)

            }

        }


    }
    return hikingCount
}