package fr.vco.aoc.y2025

import kotlin.math.min
import kotlin.math.pow

fun main() {
    val input = readLines("Day10")
    val machines = input.map { it.toMachine() }

    println("Part 1: ${machines.sumOf { it.getMinimumPressesForLightDiagrams() }}")
    println("Part 2: ${machines.sumOf { it.getMinimumPressesForJoltage() }}")
}

data class Counter(val counters: List<Int>, val cost: Int = 1) {

    companion object {
        fun fromLightDiagrams(lightDiagram: String) = Counter(lightDiagram.map { if (it == '#') 1 else 0 })
        fun fromIds(ids: List<Int>, size: Int) = Counter(List(size) { if (it in ids) 1 else 0 })
    }

    val size = counters.size
    val lightDiagram = counters.reversed()
        .map { if (it % 2 == 1) 1 else 0 }
        .reduce { acc, a -> (acc shl 1) + a }

    operator fun plus(button: Counter) =
        Counter(counters.zip(button.counters).map { (a, b) -> a + b }, cost + button.cost)

    operator fun minus(button: Counter) = Counter(counters.zip(button.counters).map { (a, b) -> a - b })
    operator fun times(times: Int) = Counter(counters.map { it * times }, cost * times)
    operator fun div(divisor: Int) = Counter(counters.map { it / divisor })
}

class Machine(val lightDiagram: Counter, buttons: List<Counter>, val joltage: Counter) {

    private val lightDiagramCache : Map<Int, List<Counter>>
    private val costCache = HashMap<Counter, Int>()

    init {
        val totalCombination = 2.0.pow(buttons.size).toInt()
        lightDiagramCache = (1..<totalCombination)
            .map { buttons.filterIndexed { i, _ -> (it shr i) and 1 == 1 } }
            .map { buttons -> buttons.reduce { a, b -> a + b } }
            .flatMap { reducedButton -> listOf( reducedButton, reducedButton * 2) }
            .groupBy { it.lightDiagram }
    }

    fun getMinimumPressesForLightDiagrams(): Int {
        return lightDiagramCache[lightDiagram.lightDiagram]!!.minOf { it.cost }
    }

    fun getMinimumPressesForJoltage(): Int {
        return getMinimumPressesForAim(joltage)
    }

    fun getMinimumPressesForAim(aim: Counter): Int {
        if (aim.counters.all { it == 0 }) return 0
        if (costCache.containsKey(aim)) return costCache[aim]!!

        var bestCost = 1_000_000
        lightDiagramCache[aim.lightDiagram]?.forEach { button ->
            val newAim = aim - button
            if (newAim.counters.all { it >= 0 }) {
                val result = 2 * getMinimumPressesForAim(newAim / 2) + button.cost
                bestCost = min(bestCost, result)
            }
        }

        costCache[aim] = bestCost
        return bestCost
    }
}


val MACHINE_REGEX = """^\[([.#]+)] ((?:\([\d,]+\) ?)+) \{([\d,]+)}$""".toRegex()
fun String.toMachine(): Machine {
    val (lightDiagram, buttons, joltage) = MACHINE_REGEX.find(this)!!.destructured
    return Machine(
        lightDiagram = Counter.fromLightDiagrams(lightDiagram),
        buttons = buttons.toButtonList().map { Counter.fromIds(it, lightDiagram.length) },
        joltage = Counter(joltage.split(",").map { it.toInt() })
    )
}

fun String.toButtonList() = this
    .split(" ")
    .map {
        it.removePrefix("(")
            .removeSuffix(")")
            .split(",")
            .map { n -> n.toInt() }

    }
