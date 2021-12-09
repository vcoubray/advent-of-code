package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day09")

    val floor = input.toCellList()
    println("Part 1: ${floor.fold(0) { acc, a -> acc + if(a.isMin(floor)) a.risk else 0 }}")
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