package fr.vco.aoc.y2025


fun main() {
    val input = readLines("Day04")

    val rolls = Rolls(input)
    println("Part 1: ${rolls.countAccessibleRolls()}")
    println("Part 2: ${rolls.countRolls() - rolls.removeUntilBlocked().countRolls()}")
}

class Rolls(val width: Int, val height: Int, val rolls: String) {

    constructor(input: List<String>) : this(
        width = input.first().length,
        height = input.size,
        rolls = input.joinToString("")
    )

    val neighbours = List(height * width) { i ->
        buildList {
            val x = i % width
            val y = i / width
            for (nx in -1..1) {
                for (ny in -1..1) {
                    if (!(ny == 0 && nx == 0) && ny + y in 0..<height && nx + x in 0..<width) {
                        add((y + ny) * width + (x + nx))
                    }
                }
            }
        }
    }

    private fun isAccessible(id: Int, maxNeighbours: Int): Boolean {
        return neighbours[id].count { n -> rolls[n] == '@' } < maxNeighbours
    }

    fun countAccessibleRolls(maxNeighbours: Int = 4): Int {
        return rolls.indices.count { rolls[it] == '@' && isAccessible(it, maxNeighbours) }
    }

    fun countRolls() = rolls.count { it == '@' }

    fun removeAccessibleRolls(maxNeighbours: Int = 4): Rolls {
        return Rolls(
            width,
            height,
            rolls.mapIndexed { i, cell -> if (isAccessible(i, maxNeighbours)) '.' else cell }
                .joinToString("")
        )
    }

    fun removeUntilBlocked(maxNeighbours: Int = 4): Rolls {
        var currentRolls = this
        do {
            val rollCount = currentRolls.countRolls()
            currentRolls = currentRolls.removeAccessibleRolls(maxNeighbours)
        } while (rollCount != currentRolls.countRolls())
        return currentRolls
    }

    override fun toString() = rolls.chunked(width).joinToString("\n")
}

