package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day20")

    var start = Position(-1, -1)
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                'S' -> start = Position(x, y)
            }
        }
    }

    val distances = input.computeDistances(start)

    println("Part 1: ${input.findCheats(distances, 2).count { (_, d) -> d >= 100 }}")
    println("Part 2: ${input.findCheats(distances, 20).count { (_, d) -> d >= 100 }}")
}

fun List<String>.computeDistances(start: Position): Map<Position, Int> {
    val toVisit = ArrayDeque<Position>().apply { add(start) }
    val distances = mutableMapOf(start to 0)

    while (toVisit.isNotEmpty()) {
        val curr = toVisit.removeFirst()
        DIRECTION_MANHATTAN.map { it + curr }
            .filter { (x, y) -> x in indices && y in 0..this.first().length }
            .forEach { n ->
                if (this[n] != '#' && !distances.contains(n)) {
                    distances[n] = distances[curr]!! + 1
                    toVisit.addLast(n)
                }
            }
    }
    return distances
}

fun List<String>.getScopeCells(scopeSize: Int): Map<Position, List<Position>> {
    val scopes = mutableMapOf<Position, List<Position>>()
    val height = this.size
    val width = this.first().length

    repeat(height) { y ->
        repeat(width) { x ->
            val start = Position(x, y)
            val toVisit = ArrayDeque<Position>().apply { add(start) }
            val scope = mutableMapOf(start to 0)

            while (toVisit.isNotEmpty()) {
                val curr = toVisit.removeFirst()

                DIRECTION_MANHATTAN.map { it + curr }
                    .filter { (x, y) -> x in 0..<width && y in 0..<height }
                    .forEach {
                        if (!scope.contains(it)) {
                            val distance = scope[curr]!! + 1
                            if (distance <= scopeSize) {
                                toVisit.add(it)
                                scope[it] = distance
                            }
                        }
                    }
            }
            scopes[start] = scope.keys.filter { this[it] != '#' }.toList()
        }
    }
    return scopes
}

fun List<String>.findCheats(distances: Map<Position, Int>, cheatSize: Int): List<Pair<Position, Int>> {
    val scopes = this.getScopeCells(cheatSize)
    return distances.flatMap { (pos, distance) ->
        scopes[pos]!!.map { pos to distances[it]!! - distance - pos.distanceManhattan(it) }
            .filter { it.second > 0 }
    }
}