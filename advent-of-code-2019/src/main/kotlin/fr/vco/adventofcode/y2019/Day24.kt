package fr.vco.adventofcode.y2019

import kotlin.collections.fold

fun main() {
    val input = readLines("Day24")
    val grid = BugGrid(input)

    println("Part 1: ${grid.findFirstRepetition()}")
    println("Part 2: ${grid.countBugsAfter(200)}")
}

class BugGrid(val initialState: Int = 0) {
    companion object {
        const val GRID_SIZE = 5

        val NEIGHBOURS_MASK: List<Int>
        val OUTER_RING_MASK: Int
        val INNER_RING_MASK: Int
        val FRACTAL_NEIGHBOURS_MASK: Map<Int, List<Pair<Int, Int>>>

        init {
            val neighbours = listOf(
                Position(0, 1),
                Position(0, -1),
                Position(-1, 0),
                Position(1, 0),
            )

            NEIGHBOURS_MASK = List(GRID_SIZE * GRID_SIZE) {
                val x = it % GRID_SIZE
                val y = it / GRID_SIZE
                val curr = Position(x, y)
                neighbours.map { n -> n + curr }
                    .filter { (nx, ny) -> nx in 0..<GRID_SIZE && ny in 0..<GRID_SIZE }
                    .map { (nx, ny) -> ny * GRID_SIZE + nx }
                    .fold(0) { acc, i -> acc + 2.pow(i) }
            }

            val middle = Position(GRID_SIZE / 2, GRID_SIZE / 2)
            val layerNeighbours = neighbours.associate { n ->
                val innerPos = n + middle
                val outerMask = List(25) { it }
                    .filter {
                        when {
                            n.y == -1 -> it / GRID_SIZE == 0
                            n.y == 1 -> it / GRID_SIZE == GRID_SIZE - 1
                            n.x == 1 -> it % GRID_SIZE == GRID_SIZE - 1
                            else -> it % GRID_SIZE == 0
                        }
                    }
                (innerPos.x + GRID_SIZE * innerPos.y) to outerMask.fold(0) { acc, i -> acc + 2.pow(i) }
            }

            OUTER_RING_MASK = layerNeighbours.values.reduce { acc, a -> acc or a }
            INNER_RING_MASK = layerNeighbours.keys.fold(0) { acc, i -> acc + 2.pow(i) }

            FRACTAL_NEIGHBOURS_MASK = NEIGHBOURS_MASK.mapIndexed { i, nMask -> i to nMask }
                .filterNot { (id, _) -> id == middle.x + GRID_SIZE * middle.y }
                .associate { (id, nMask) ->
                    val neighbors = buildList {
                        add(0 to nMask)
                        if (id in layerNeighbours) {
                            add(-1 to layerNeighbours[id]!!)
                        }
                        val outerNeighbours = layerNeighbours
                            .filter { (_, nNeighbour) -> nNeighbour and (1 shl id) != 0 }
                            .keys
                            .fold(0) { acc, i -> acc + 2.pow(i) }
                        add(1 to outerNeighbours)

                    }
                    id to neighbors
                }
        }
    }

    constructor(grid: List<String>) : this(
        grid.joinToString("")
            .map { if (it == '#') 1 else 0 }
            .reversed()
            .foldIndexed(0) { i, acc, a -> acc + a * 2.pow(i) }
    )

    fun findFirstRepetition(): Int {
        val history = mutableSetOf<Int>()
        var curr = initialState

        while (curr !in history) {
            history.add(curr)
            curr = curr.next()
        }
        return curr
    }

    private fun Int.next(): Int {
        var newValue = 0
        repeat(GRID_SIZE * GRID_SIZE) { id ->
            val hasBug = (this shr id and 1) == 1
            val neighboursCount = (this and NEIGHBOURS_MASK[id]).countOneBits()
            val newBug = when {
                hasBug && neighboursCount == 1 -> true
                hasBug -> false
                else -> neighboursCount in 1..2
            }
            if (newBug) {
                newValue += 1 shl id
            }
        }
        return newValue
    }


    var layers = mutableMapOf(0 to initialState)
    fun countBugsAfter(repetition: Int): Long {
        var outerLayer = 0
        var innerLayer = 0
        repeat(repetition) {
            // Expand innerLayer if necessary
            if (layers[innerLayer]!! and INNER_RING_MASK != 0) {
                innerLayer--
                layers[innerLayer] = 0
            }
            // Expand outerLayer if necessary
            if (layers[outerLayer]!! and OUTER_RING_MASK != 0) {
                outerLayer++
                layers[outerLayer] = 0
            }

            layers = layers.map { (id, _) -> id to layers.nextFractal(id) }
                .toMap().toMutableMap()
        }

        return layers.toList().fold(0L) { acc, (_, grid) -> acc + grid.countOneBits() }
    }

    fun Map<Int, Int>.nextFractal(layer: Int): Int {
        var newValue = 0
        FRACTAL_NEIGHBOURS_MASK.forEach { (id, neighbours) ->
            val hasBug = (this[layer]!! shr id and 1) == 1
            val neighboursCount = neighbours.sumOf { (nLayer, nMask) ->
                ((this[layer + nLayer] ?: 0) and nMask).countOneBits()
            }
            val newBug = when {
                hasBug && neighboursCount == 1 -> true
                hasBug -> false
                else -> neighboursCount in 1..2
            }
            if (newBug) {
                newValue += 1 shl id
            }
        }
        return newValue
    }
}