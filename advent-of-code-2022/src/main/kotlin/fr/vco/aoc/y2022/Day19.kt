package fr.vco.aoc.y2022

import kotlin.math.max
import kotlin.system.measureTimeMillis

const val ORE_MASK = 1
const val CLAY_MASK = 256
const val OBSIDIAN_MASK = 65536
const val GEODE_MASK = 16777216
const val STOCK_MASK = 4294967296L


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


data class Resources(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0) {
    operator fun plus(r: Resources) = Resources(ore + r.ore, clay + r.clay, obsidian + r.obsidian, geode + r.geode)
    operator fun minus(r: Resources) = Resources(ore - r.ore, clay - r.clay, obsidian - r.obsidian, geode - r.geode)
    fun isValid() = ore >= 0 && clay >= 0 && obsidian >= 0 && geode >= 0
    fun isSuperior(r: Resources) = ore > r.ore || clay > r.clay || obsidian > r.obsidian

    fun getId() = ore + CLAY_MASK * clay + OBSIDIAN_MASK * obsidian + GEODE_MASK * geode
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
    val maxCost = robots.map { it.cost }.fold(Resources(geode = Int.MAX_VALUE)) { acc, costs ->
        acc.copy(
            ore = max(costs.ore, acc.ore),
            clay = max(costs.clay, acc.clay),
            obsidian = max(costs.obsidian, acc.obsidian)
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
                !(stock - robot.cost).isValid() -> null
                else -> State(this.minute + 1, stock + robots - robot.cost, newRobots, blueprint)
            }
        } +
                State(minute + 1, stock + robots, robots, blueprint)
    }

    fun getId() = stock.getId() * STOCK_MASK + robots.getId()
}

fun play(blueprint: Blueprint, minute: Int): Int {
    var maxGeode = 0
    measureTimeMillis {
        val stock = Resources()
        val robots = Resources(ore = 1)


        val start = State(0, stock, robots, blueprint)
        val toVisit = ArrayDeque<State>().apply { add(start) }
        val visited = mutableMapOf<Long, Boolean>()
        visited[start.getId()] = true
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current.stock.geode > maxGeode) maxGeode = current.stock.geode
            current.getNextStates()
                .filter { it.minute <= minute && visited[it.getId()] != true }
                .forEach { state ->
                    toVisit.add(state)
                    visited[state.getId()] = true
                }
        }
    }.let { println("${blueprint.id} in ${it}ms ") }
    return maxGeode
}
