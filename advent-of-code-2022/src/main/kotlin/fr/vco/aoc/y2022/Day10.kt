package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day10")

    val deviceCpu = DeviceCpu().exec(input)
    println("Part 1: ${deviceCpu.getSignalStrength()}")
    println("Part 2:")
    deviceCpu.screen().forEach(::println)
}

class DeviceCpu {
    var x = 1
    val registry = mutableListOf(x)

    fun exec(instructions: List<String>) = apply { instructions.forEach(::exec) }

    private fun exec(instruction: String) {
        if (instruction.startsWith("noop")) {
            noop()
        } else {
            val (_, value) = instruction.split(" ")
            addX(value.toInt())
        }
    }

    private fun noop() = registry.add(x)
    private fun addX(value: Int) {
        noop()
        x += value
        noop()
    }

    fun getSignalStrength(): Int {
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * registry[it - 1] }
    }

    fun screen(): List<String> {
        return registry.chunked(40).map { line ->
            line.mapIndexed { i, pixel ->
                if (i in (pixel - 1)..(pixel + 1)) '#'
                else ' '
            }.joinToString(" ")
        }
    }
}