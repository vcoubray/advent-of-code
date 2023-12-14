package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day14")
    val panel = ControlPanel(input)

    println("Part 1 : ${panel.moveNorth().score()}")
}

class ControlPanel(input: List<String>) {
    val grid: List<MutableList<Char>> = input.map { it.toMutableList() }
    val width = grid.first().size
    val height = grid.size

    fun moveNorth() = apply {
        for (x in 0..<width){
            var freeSpace = 0
            for (y in 0..<height) {
                when(grid[y][x]) {
                    '#' -> freeSpace = y+1
                    'O' -> {
                        grid[y][x] = '.'
                        grid[freeSpace][x] = 'O'

                        freeSpace += 1
                    }
                }
            }
        }
    }

    fun score() = grid.foldIndexed(0) { i, score, line -> score + (line.count { c -> c == 'O' } * (height - i)) }

}