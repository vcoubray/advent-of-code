package fr.vco.aoc.y2022

import kotlin.math.absoluteValue

fun main() {
    val input = readLines("Day18")

    val lavaDroplets = input.map { it.split(",") }
        .map { (x, y, z) -> Droplet(x.toInt(), y.toInt(), z.toInt()) }

    println("Part 1 : ${lavaDroplets.totalSurface()}")
    println("Part 2 : ${lavaDroplets.exteriorSurface()}")
}

data class Droplet(val x: Int, val y: Int, val z: Int) {
    operator fun plus(droplet: Droplet) = Droplet(x + droplet.x, y + droplet.y, z + droplet.z)
    fun isAdjacent(droplet: Droplet): Boolean {
        val distances = listOf(x - droplet.x, y - droplet.y, z - droplet.z)
        return distances.count { it == 0 } == 2 && distances.sum().absoluteValue == 1
    }
}

fun List<Droplet>.totalSurface() :Int{
    var totalSurface = size * 6
    for (i in this.indices) {
        for (j in i until size) {
            if (this[i].isAdjacent(this[j])) {
                totalSurface -= 2
            }
        }
    }
    return totalSurface
}

fun List<Droplet>.buildMatrix(): Map<Int, Map<Int, MutableMap<Int, Int>>> {
    val min = Droplet(minOf { it.x }, minOf { it.y }, minOf { it.z })
    val max = Droplet(maxOf { it.x }, maxOf { it.y }, maxOf { it.z })

    val matrix = buildMap {
        for (x in min.x - 1..max.x + 1) {
            put(x, buildMap {
                for (y in min.y - 1..max.y + 1) {
                    put(y, buildMap {
                        for (z in min.z - 1..max.z + 1) {
                            put(z, 0)
                        }
                    }.toMutableMap())
                }
            })
        }
    }

    forEach { (x, y, z) ->
        matrix[x]!![y]!![z] = 1
    }

    return matrix
}

operator fun <T> Map<Int, Map<Int, Map<Int, T>>>.get(droplet: Droplet): T? =
    this[droplet.x]?.get(droplet.y)?.get(droplet.z)

operator fun <T> Map<Int, Map<Int, MutableMap<Int, T>>>.set(droplet: Droplet, value: T) {
    this[droplet.x]?.get(droplet.y)?.get(droplet.z)?.let {
        this[droplet.x]!![droplet.y]!![droplet.z] = value
    }
}

fun List<Droplet>.exteriorSurface(): Int {

    val directions = listOf(
        Droplet(1, 0, 0), Droplet(-1, 0, 0),
        Droplet(0, 1, 0), Droplet(0, -1, 0),
        Droplet(0, 0, 1), Droplet(0, 0, -1)
    )

    val start = Droplet(minOf { it.x }, minOf { it.y }, minOf { it.z })
    val toVisit = ArrayDeque<Droplet>().apply { add(start) }

    val dropletMatrix = buildMatrix()
    dropletMatrix[start.x]!![start.y]!![start.z] = -1

    var exteriorSurface = 0
    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        directions.map { it + current }
            .filter { dropletMatrix[it] != null }
            .forEach {
                when (dropletMatrix[it]!!) {
                    0 -> {
                        dropletMatrix[it] = -1
                        toVisit.add(it)
                    }
                    1 -> exteriorSurface += 1
                }
            }
    }
    return exteriorSurface
}
