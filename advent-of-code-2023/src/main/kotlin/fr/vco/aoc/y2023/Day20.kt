package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day20")

    println("Part 1: ${Modules(input).broadcast(1_000)}")
    println("Part 2: ${Modules(input).countPropagateUntilHighPulse()}")
}

data class Pulse(val source: String, val dest: String, val low: Boolean)

abstract class Module(val name: String, val destinations: List<String>) {
    abstract fun propagate(pulse: Pulse): List<Pulse>
    abstract fun getState(): List<Boolean>

    class Broadcaster(name: String, destinations: List<String>) : Module(name, destinations) {
        override fun propagate(pulse: Pulse): List<Pulse> {
            return destinations.map { Pulse(name, it, pulse.low) }
        }

        override fun getState(): List<Boolean> = emptyList()
    }

    class FlipFlop(name: String, destinations: List<String>) : Module(name, destinations) {
        private var isOn: Boolean = false

        override fun propagate(pulse: Pulse): List<Pulse> {
            if (pulse.low) {
                isOn = !isOn
                return destinations.map { Pulse(name, it, !isOn) }
            }
            return emptyList()
        }

        override fun getState() = listOf(isOn)
    }

    class Conjunction(name: String, destinations: List<String>, inputs: List<String>) : Module(name, destinations) {
        private val state = inputs.associateWith { true }.toMutableMap()
        override fun propagate(pulse: Pulse): List<Pulse> {
            state[pulse.source] = pulse.low
            return destinations.map { dest -> Pulse(name, dest, state.values.all { !it }) }
        }

        override fun getState(): List<Boolean> = state.values.toList()
    }
}

class Modules(input: List<String>) {

    private val modules: Map<String, Module>
    private var lowCount: Long = 0
    private var highCount: Long = 0

    private val start = "broadcaster"
    private lateinit var end: String

    init {
        val inputs = mutableMapOf<String, MutableList<String>>()

        modules = input.map {
            val (type, name, dest) = """(^%|&|broadcaster)(.*) -> (.*$)""".toRegex().find(it)!!.destructured
            val dests = dest.split(", ")
            if ("rx" in dests) end = name
            dests.forEach { d -> inputs.computeIfAbsent(d) { mutableListOf() }.add(name) }
            name to (type to dests)
        }
            .associate { (name, module) ->
                val (type, dest) = module
                when (type) {
                    "%" -> name to Module.FlipFlop(name, dest)
                    "&" -> name to Module.Conjunction(name, dest, inputs[name] ?: emptyList())
                    else -> start to Module.Broadcaster(name, dest)
                }
            }
    }

    private fun broadcastOnePulse(startModule: String = this.start): Map<String, Pair<Long, Long>> {

        val start = Pulse("", startModule, true)
        val toVisit = ArrayDeque<Pulse>().apply { addLast(start) }

        val lowPulses = mutableMapOf<String, Long>()
        val highPulses = mutableMapOf<String, Long>()
        lowPulses[startModule] = 1L

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            modules[current.dest]?.propagate(current)
                ?.onEach {
                    if (it.low) lowPulses[it.dest] = (lowPulses[it.dest] ?: 0) + 1
                    else highPulses[it.dest] = (highPulses[it.dest] ?: 0) + 1
                }
                ?.forEach(toVisit::addLast)
        }
        lowCount += lowPulses.values.sum()
        highCount += highPulses.values.sum()
        return (lowPulses.keys + highPulses.keys).associateWith { (lowPulses[it] ?: 0) to (highPulses[it] ?: 0) }
    }

    fun broadcast(count: Int): Long {
        for (i in 0..<count) {
            broadcastOnePulse()
        }
        return lowCount * highCount
    }

    fun countPropagateUntilHighPulse(): Long {
        return modules["broadcaster"]!!.destinations.map { name ->
            var counter = 0L
            do {
                val pulses = broadcastOnePulse(name)
                counter++
            } while (pulses[end]!!.second == 0L)
            counter
        }.ppcm()
    }
}
