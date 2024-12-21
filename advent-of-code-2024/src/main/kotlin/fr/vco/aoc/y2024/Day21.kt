package fr.vco.aoc.y2024

import kotlin.math.abs


fun main() {
    val input = readLines("Day21")

    println("Part 1: ${input.sumOf { CodeResolver.getComplexity(it) }}")
    println("Part 2: ${input.sumOf { CodeResolver.getComplexity(it, 25) }}")
}


object CodeResolver {
    private val digitalKeyPad = "789456123 0A".chunked(3)
    private val robotKeyPad = " ^A<v>".chunked(3)

    private val digitalKeyPaths = KeypadPathBuilder.build(digitalKeyPad)
    private val robotKeyPaths = KeypadPathBuilder.build(robotKeyPad)

    private val robotCodeSize = mutableMapOf<String, Long>()

    private fun getCodeSize(code: String, robotCount: Int = 2): Long {
        var current = 'A'
        var codeSize = 0L
        for (next in code) {
            codeSize += digitalKeyPaths[current]!![next]!!
                .minOf { getRobotCodeSize(it, robotCount) }
            current = next
        }
        return codeSize
    }

    private fun getRobotCodeSize(code: String, robotCount: Int): Long {
        if (robotCount <= 0) return code.length.toLong()
        if (robotCodeSize.keys.contains("$code-$robotCount")) return robotCodeSize["$code-$robotCount"]!!
        var current = 'A'
        var codeSize = 0L
        for (next in code) {
            codeSize += robotKeyPaths[current]!![next]!!.minOf { getRobotCodeSize(it, robotCount - 1) }
            current = next
        }
        robotCodeSize["$code-$robotCount"] = codeSize
        return codeSize
    }

    private fun String.numericPart() = this.removeSuffix("A").toInt()
    fun getComplexity(code: String, robotCount: Int = 2) = code.numericPart() * getCodeSize(code, robotCount)
}


object KeypadPathBuilder {
    private val SYMBOLS_DIRECTIONS = mapOf(
        '<' to WEST,
        '>' to EAST,
        '^' to NORTH,
        'v' to SOUTH
    )


    fun build(keypad: List<String>) = keypad.computePath()

    private fun List<String>.computePath(): Map<Char, Map<Char, List<String>>> {
        val keys = this.flatMap { it.toList() }.filterNot { it == ' ' }
        val keyPositions = this.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                c to Position(x, y)
            }
        }.toMap()

        val paths = keys.associateWith { start ->
            keys.associateWith { end ->
                keyPositions[start]!!.getPaths(keyPositions[end]!!)
                    .filter { isValidPath(keyPositions[start]!!, it) }
            }
        }
        return paths
    }

    private fun List<String>.isValidPath(start: Position, path: String): Boolean {
        var currentPos = start
        for (c in path) {
            if (this[currentPos] == ' ') return false
            val dir = SYMBOLS_DIRECTIONS[c] ?: break
            currentPos += dir
        }
        return true
    }

    private fun Position.getPaths(other: Position): List<String> {
        val x = this.x - other.x
        val xDir = if (x > 0) '<' else '>'
        val xPath = "".padEnd(abs(x), xDir)

        val y = this.y - other.y
        val yDir = if (y > 0) '^' else 'v'
        val yPath = "".padEnd(abs(y), yDir)

        return setOf("$xPath${yPath}A", "$yPath${xPath}A").toList()
    }
}