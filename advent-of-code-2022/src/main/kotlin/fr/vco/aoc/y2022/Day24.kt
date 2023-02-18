package fr.vco.aoc.y2022

import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day24")

    val valley = Valley(input)
    println("Part 1 : ${valley.part1()}")
    println("Part 2 : ${valley.part2()}")
}

class Valley(val map: List<String>) {
    companion object {
        private val ACTIONS = listOf(0 to 0, 0 to 1, 0 to -1, -1 to 0, 1 to 0)
    }

    class Tile(
        val neighbours: List<Int> = emptyList(),
        private val isVerticalBlizzard: List<Boolean> = emptyList(),
        private val isHorizontalBlizzard: List<Boolean> = emptyList(),
    ) {
        fun isFree(turn: Int) =
            !isVerticalBlizzard[turn % (isVerticalBlizzard.size)] && !isHorizontalBlizzard[turn % isHorizontalBlizzard.size]
    }

    private val width = map.first().length
    private val height = map.size

    private val isVerticalBlizzard = List(width * height) { MutableList(height - 2) { false } }
    private val isHorizontalBlizzard = List(width * height) { MutableList(width - 2) { false } }

    init {
        repeat(width * height) {
            val x = it % width
            val y = it / width
            when (map[y][x]) {
                '>' -> {
                    for (i in 1 until width - 1) {
                        val turn = (i - x).mod(width - 2)
                        isHorizontalBlizzard[y * width + i][turn] = true
                    }
                }

                '<' -> {
                    for (i in 1 until width - 1) {
                        val turn = (x - i).mod(width - 2)
                        isHorizontalBlizzard[y * width + i][turn] = true
                    }
                }

                'v' -> {
                    for (i in 1 until height - 1) {
                        val turn = (i - y).mod(height - 2)
                        isVerticalBlizzard[i * width + x][turn] = true
                    }
                }

                '^' -> {
                    for (i in 1 until height - 1) {
                        val turn = (y - i).mod(height - 2)
                        isVerticalBlizzard[i * width + x][turn] = true
                    }
                }
            }
        }
    }

    private val tiles = List(width * height) {
        val x = it % width
        val y = it / width
        if (map[y][x] == '#') Tile()
        else Tile(
            ACTIONS.map { (dx, dy) -> (dx + x) to (dy + y) }
                .filter { (nx, ny) -> nx in 0 until width && ny in 0 until height }
                .filter { (nx, ny) -> map[ny][nx] != '#' }
                .map { (nx, ny) -> ny * width + nx },
            isVerticalBlizzard[it],
            isHorizontalBlizzard[it]
        )
    }

    private val start = tiles.indexOfFirst { it.neighbours.isNotEmpty() }
    private val end = tiles.indexOfLast { it.neighbours.isNotEmpty() }

    private val blizzardCycle = lcm(height - 2, width - 2)

    private fun bfs(start: Int, end: Int, startTurn: Int): Int {
        val toVisit = ArrayDeque<Int>().apply { add(start * blizzardCycle + (startTurn % blizzardCycle)) }
        val visited = MutableList(tiles.size * blizzardCycle) { -1 }
        visited[start * blizzardCycle + startTurn] = startTurn

        while (toVisit.isNotEmpty()) {
            val cur = toVisit.removeFirst()
            val id = cur / blizzardCycle
            if (id == end) {
                return visited[cur]
            }
            val turn = visited[cur] + 1
            tiles[id].neighbours.forEach { n ->
                val index = n * blizzardCycle + (turn % blizzardCycle)
                if (tiles[n].isFree(turn) && visited[index] == -1) {
                    visited[index] = turn
                    toVisit.addLast(index)
                }
            }
        }
        throw Exception("No Path Found")
    }

    fun part1() = bfs(start, end, 0)
    fun part2() = bfs(start, end, bfs(end, start, part1()))
}

fun lcm(a: Int, b: Int): Int {
    var divider = min(a, b)
    var dividend = max(a, b)

    var rem = dividend % divider
    while (rem != 0) {
        dividend = divider
        divider = rem
        rem = dividend % divider
    }
    return a * b / divider
}