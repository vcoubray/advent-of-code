package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day09")

    val floor = input.toCellList()
    println("Part 1: ${floor.fold(0) { acc, a -> acc + if (a.isMin(floor)) a.risk else 0 }}")
    println("Part 2: ${floor.getPoolsSize().sortedDescending().take(3).reduce { acc, a -> acc * a }}")
}

private fun List<String>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
private fun List<String>.convertCoord(x: Int, y: Int) = this.get(x, y)?.let { this.first().length * y + x }
private fun List<String>.getNeighbors(x: Int, y: Int) = listOfNotNull(
    this.convertCoord(x - 1, y),
    this.convertCoord(x + 1, y),
    this.convertCoord(x, y - 1),
    this.convertCoord(x, y + 1),
)

private fun List<String>.toCellList() = this.flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Cell(c.digitToInt(), this.getNeighbors(x, y))
    }
}

private data class Cell(val height: Int, val neighborsId: List<Int>) {
    val risk = height + 1

    fun isMin(floor: List<Cell>) = this.height < this.neighborsId.minOf { floor[it].height }
}

private fun List<Cell>.getPool(id: Int): Set<Int> {
    if (this[id].height == 9) return emptySet()
    val visited = mutableSetOf<Int>()
    val toVisit = LinkedList<Int>()
    toVisit.add(id)
    while (!toVisit.isEmpty()) {
        val current = toVisit.pop()
        visited.add(current)
        this[current].neighborsId.forEach {
            if (this[it].height != 9 && !visited.contains(it))
                toVisit.addLast(it)
        }
    }
    return visited
}

private fun List<Cell>.getPoolsSize(): List<Int> {
    val pools = MutableList(this.size) { -1 }
    val poolSize = mutableListOf<Int>()
    this.forEachIndexed { i, cell ->
        if (pools[i] < 0) {
            if (cell.height == 9) pools[i] = 0
            val pool = this.getPool(i)
            poolSize.add(pool.size)
            pool.forEach { pools[it] = poolSize.size - 1 }
        }
    }
    return poolSize
}
