package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day16")
    val valves = input.map { """^Valve (\w+) .* rate=(\d+); .* valves? (.+)$""".toRegex().find(it)!!.destructured }
        .map { (id, flow, neighbours) -> Valve(id, flow.toInt(), neighbours.split(", ")) }
        .associateBy { it.id }

    val distances = valves.computeDistances()
    val openValves = valves.filterNot { (_, v) -> v.flow == 0 }.keys
    println("Part 1 : ${valves.maxReleasedPressure("AA", "AA", 30, 30, openValves, distances, 1)}")
    println("Part 2 : ${valves.maxReleasedPressure("AA", "AA", 26, 26, openValves, distances, 2)}")
}

data class Valve(val id: String, val flow: Int, val neighbours: List<String>) {
    fun releasedFlow(minutesLeft: Int) = minutesLeft * flow
}

fun Map<String, Valve>.computeDistances(): Map<String, Map<String, Int>> {
    return this.keys.associateWith(this::computeDistances)
}

fun Map<String, Valve>.computeDistances(start: String): Map<String, Int> {
    val toVisit = ArrayDeque<String>().apply { add(start) }
    val distances = keys.associateWith { -1 }.toMutableMap()
    distances[start] = 0
    while (toVisit.isNotEmpty()) {
        val currId = toVisit.removeFirst()
        val curr = this[currId]!!
        curr.neighbours.filter { distances[it] == -1 }
            .forEach { n ->
                distances[n] = distances[currId]!! + 1
                toVisit.addLast(n)
            }
    }
    return distances
}

fun Map<String, Valve>.maxReleasedPressure(
    originValve: String,
    currentValve: String,
    initialMinutes: Int,
    minutesLeft: Int,
    openValves: Set<String>,
    distances: Map<String, Map<String, Int>>,
    personCount: Int
): Int {
    if (personCount <= 0) return 0
    val releasedPressure = this[currentValve]!!.releasedFlow(minutesLeft)
    val newOpenValves = openValves.filterNot { it == currentValve }.toSet()

    val possibleNeighbours = newOpenValves.filter { minutesLeft - distances[currentValve]!![it]!! > 0 }
    if (possibleNeighbours.isNotEmpty()) {
        return releasedPressure + possibleNeighbours.maxOf {
            maxReleasedPressure(
                originValve,
                it,
                initialMinutes,
                minutesLeft - distances[currentValve]!![it]!! - 1,
                newOpenValves,
                distances,
                personCount
            )
        }
    } else {
        return releasedPressure + maxReleasedPressure(
            originValve,
            originValve,
            initialMinutes,
            initialMinutes,
            newOpenValves,
            distances,
            personCount - 1
        )
    }
}
