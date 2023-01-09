package fr.vco.aoc.y2022

const val MAX_TIME = 30
fun main() {
    val input = readLines("Day16")
    val valves = input.map { """^Valve (\w+) .* rate=(\d+); .* valves? (.+)$""".toRegex().find(it)!!.destructured }
        .map { (id, flow, neighbours) -> Valve(id, flow.toInt(), neighbours.split(", ")) }
        .associateBy { it.id }

    val distances = valves.computeDistances()
    val maxReleasedPressure = valves.maxReleasedPressure("AA",
        0,
        valves.filterNot { (_, v) -> v.flow == 0 }.keys,
        distances
    )

    println("Part 1 : $maxReleasedPressure")
}

data class Valve(val id: String, val flow: Int, val neighbours: List<String>) {
    fun releasedFlow(elapsedMinutes: Int) = (MAX_TIME - elapsedMinutes) * flow
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
    currentValve: String,
    elapsedMinutes: Int,
    openedValves: Set<String>,
    distances: Map<String, Map<String, Int>>,
): Int {
    if (elapsedMinutes >= MAX_TIME) return 0
    val releasedPressure = this[currentValve]!!.releasedFlow(elapsedMinutes)
    val newOpenedValves = openedValves.filterNot { it == currentValve }.toSet()
    return releasedPressure + (newOpenedValves.filter { distances[currentValve]!![it]!! + elapsedMinutes <= MAX_TIME }
        .maxOfOrNull {
            maxReleasedPressure(
                it,
                distances[currentValve]!![it]!! + elapsedMinutes + 1,
                newOpenedValves,
                distances
            )
        } ?: 0)
}