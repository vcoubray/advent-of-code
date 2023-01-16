package fr.vco.aoc.y2022

import kotlin.math.max

const val CLAY_MASK = 256
const val OBSIDIAN_MASK = 65536
const val GEODE_MASK = 16777216

fun main() {
    val input = readLines("Day19")

    val blueprints = input.map { line ->
        """^.* (\d+): .* (\d+) .* (\d+) .* (\d+) .* (\d+) .* (\d+) .* (\d+) .*$""".toRegex().find(line)!!.destructured
    }
        .map { (id, oreCost, clayCost, obsidianOreCost, obsidianClayCost, geodeOdeCost, geodeObsidianCost) ->
            Blueprint(
                id.toInt(),
                Resources(ore = oreCost.toInt()),
                Resources(ore = clayCost.toInt()),
                Resources(ore = obsidianOreCost.toInt(), clay = obsidianClayCost.toInt()),
                Resources(ore = geodeOdeCost.toInt(), obsidian = geodeObsidianCost.toInt())
            )
        }

    println("Part 1 : ${blueprints.sumOf { it.id * play(it, 24) }} ")
    println("Part 2 : ${blueprints.take(3).fold(1L) { acc, it -> acc * play(it, 32) }} ")

}

typealias Resources = Int

fun Resources(ore: Int = 0, clay: Int = 0, obsidian: Int = 0, geode: Int = 0) =
    ore + CLAY_MASK * clay + OBSIDIAN_MASK * obsidian + GEODE_MASK * geode

fun Resources.getGeodes() = this shr 24
fun Resources.getObsidian() = (this and GEODE_MASK - 1) shr 16
fun Resources.getClay() = (this and OBSIDIAN_MASK - 1) shr 8
fun Resources.getOre() = (this and CLAY_MASK - 1)
fun Resources.getString() =
    "ore = ${getOre()}, clay = ${getClay()}, obsidian = ${getObsidian()}, geode = ${getGeodes()}"

fun Resources.toBinaryString() = this.toString(2).padStart(32, '0').chunked(8).joinToString(" ")
fun Resources.isSuperior(r: Resources): Boolean {
    return this shr 24 > r shr 24 ||
            this and (GEODE_MASK - 1) > r and (GEODE_MASK - 1) ||
            this and (OBSIDIAN_MASK - 1) > r and (OBSIDIAN_MASK - 1) ||
            this and (CLAY_MASK - 1) > r and (CLAY_MASK - 1)
}


data class Robot(val cost: Resources, val gain: Resources)

class Blueprint(
    val id: Int,
    oreRobotCost: Resources,
    clayRobotCost: Resources,
    obsidianRobotCost: Resources,
    geodeRobotCost: Resources,
) {
    val robots = listOf(
        Robot(oreRobotCost, Resources(ore = 1)),
        Robot(clayRobotCost, Resources(clay = 1)),
        Robot(obsidianRobotCost, Resources(obsidian = 1)),
        Robot(geodeRobotCost, Resources(geode = 1))
    )
    val maxCost = robots.map { it.cost }.fold(Resources(geode = 127)) { acc, costs ->
        Resources(
            ore = max(costs.getOre(), acc.getOre()),
            clay = max(costs.getClay(), acc.getClay()),
            obsidian = max(costs.getObsidian(), acc.getObsidian()),
            geode = acc.getGeodes()
        )
    }
}

data class State(
    val minute: Int,
    val stock: Resources,
    val robots: Resources,
    val blueprint: Blueprint,
) {

    fun getNextStates(): List<State> {
        return blueprint.robots.mapNotNull { robot ->
            val newRobots = robots + robot.gain
            when {
                newRobots.isSuperior(blueprint.maxCost) -> null
                robot.cost.isSuperior(stock) -> null
                else -> State(this.minute + 1, stock + robots - robot.cost, newRobots, blueprint)
            }
        } +
                State(minute + 1, stock + robots, robots, blueprint)
    }
}

fun play(blueprint: Blueprint, minute: Int): Int {
    var maxGeode = 0
    val stock = Resources()
    val robots = Resources(ore = 1)


    val start = State(0, stock, robots, blueprint)
    val toVisit = ArrayDeque<State>().apply { add(start) }
    val visited = mutableMapOf<Int, Int>()
    visited[start.robots] = start.stock
    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        if (current.stock.getGeodes() > maxGeode) maxGeode = current.stock.getGeodes()
        current.getNextStates()
            .filter { it.minute <= minute && it.stock.isSuperior(visited[it.robots] ?: 0) }
            .forEach { state ->
                toVisit.add(state)
                visited[state.robots] = state.stock
            }
    }
    return maxGeode
}
