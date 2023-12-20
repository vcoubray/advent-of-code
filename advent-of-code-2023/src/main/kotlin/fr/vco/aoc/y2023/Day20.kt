package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day20")
    val modules = Modules(input)

    modules.broadcast()
}

data class Pulse(val source: String, val dest: String, val low: Boolean)

abstract class Module(val name: String, val destinations: List<String>) {
    abstract fun propagate(pulse: Pulse): List<Pulse>

    class Broadcaster(name: String, destinations: List<String>) : Module(name, destinations) {
        override fun propagate(pulse: Pulse): List<Pulse> {
            return destinations.map { Pulse(name, it, pulse.low) }
        }
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
    }

    class Conjunction(name: String, destinations: List<String>, inputs: List<String>) : Module(name, destinations) {
        val state = inputs.associateWith { true }.toMutableMap()
        override fun propagate(pulse: Pulse): List<Pulse> {
            state[pulse.source] = pulse.low
            return destinations.map { dest -> Pulse(name, dest, state.values.all { !it }) }
        }
    }
}


class Modules(input: List<String>) {

    private val modules: Map<String, Module>

    init {
        val inputs = mutableMapOf<String, MutableList<String>>()

        modules = input.map {
            val (type, name, dest) = """(^%|&|broadcaster)(.*) -> (.*$)""".toRegex().find(it)!!.destructured
            val dests = dest.split(", ")
            dests.forEach { d -> inputs.computeIfAbsent(d) { mutableListOf() }.add(name) }
            name to (type to dests)
        }
            .associate { (name, module) ->
                val (type, dest) = module
                when (type) {
                    "%" -> name to Module.FlipFlop(name, dest)
                    "&" -> name to Module.Conjunction(name, dest, inputs[name] ?: emptyList())
                    else -> "broadcaster" to Module.Broadcaster(name, dest)
                }
            }
    }

    fun broadcast() {
        val start = Pulse("button", "broadcaster", true)
        val toVisit = ArrayDeque<Pulse>().apply { addLast(start) }

        var lows = 1
        var highs = 0
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            modules[current.dest]?.propagate(current)
                ?.onEach { if (it.low) lows++ else highs++ }
                ?.forEach(toVisit::addLast)
        }

        println("low : $lows, high: $highs")
    }
}
