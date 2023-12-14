package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day14")

    println("Part 1 : ${ControlPanel(input).moveNorth().score()}")
    println("Part 2 : ${ControlPanel(input).spin(1_000_000_000)}")
}

class ControlPanel(input: List<String>) {
    val grid: List<MutableList<Char>> = input.map { it.toMutableList() }
    val width = grid.first().size
    val height = grid.size

    val logs = mutableListOf(gridToString())
    val logScores = mutableListOf(score())

    fun spin(repetition: Int = 1) : Int {
        var i = 1
        while (i <= repetition) {
            moveNorth()
            moveWest()
            moveSouth()
            moveEast()
            val log = gridToString()
            val logId = logs.indexOf(log)
            if (logId != -1) {
                val loopSize = i - logId
                val remainingSpins = repetition - i
                val finalId = (remainingSpins % loopSize) + logId
                return logScores[finalId]
            }
            logs.add(gridToString())
            logScores.add(score())
            i++
        }
        return 0
    }

    private fun gridToString() = grid.joinToString("") { it.joinToString("") }

    fun moveNorth() = apply {
        for (x in 0..<width) {
            var freeSpace = 0
            for (y in 0..<height) {
                when (grid[y][x]) {
                    '#' -> freeSpace = y + 1
                    'O' -> {
                        grid[y][x] = '.'
                        grid[freeSpace][x] = 'O'
                        freeSpace += 1
                    }
                }
            }
        }
    }

    fun moveSouth() = apply {
        for (x in 0..<width) {
            var freeSpace = height - 1
            for (y in height - 1 downTo 0) {
                when (grid[y][x]) {
                    '#' -> freeSpace = y - 1
                    'O' -> {
                        grid[y][x] = '.'
                        grid[freeSpace][x] = 'O'
                        freeSpace -= 1
                    }
                }
            }
        }
    }

    fun moveWest() = apply {
        for (y in 0..<height) {
            var freeSpace = 0
            for (x in 0..<width) {
                when (grid[y][x]) {
                    '#' -> freeSpace = x + 1
                    'O' -> {
                        grid[y][x] = '.'
                        grid[y][freeSpace] = 'O'
                        freeSpace += 1
                    }
                }
            }
        }
    }

    fun moveEast() = apply {
        for (y in 0..<height) {
            var freeSpace = width - 1
            for (x in width - 1 downTo 0) {
                when (grid[y][x]) {
                    '#' -> freeSpace = x - 1
                    'O' -> {
                        grid[y][x] = '.'
                        grid[y][freeSpace] = 'O'
                        freeSpace -= 1
                    }
                }
            }
        }
    }

    fun score() = grid.foldIndexed(0) { i, score, line -> score + (line.count { c -> c == 'O' } * (height - i)) }
}