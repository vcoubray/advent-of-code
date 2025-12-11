package fr.vco.aoc.y2025

fun main() {
    val input = readLines("Day10")

//    val input = listOf(
//        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
//        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
//        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}",
//    )

    val machines = input.map { it.toMachine() }

    println("Part 1: ${machines.sumOf { it.getMinimumPressesForLightDiagrams() }}")
    println("Part 2: ${machines.mapIndexed { i, it -> println("$i -> ${it.getMinimumPressesForJoltage()}") }}")
}


class Machine(val lightDiagram: Int, val buttons: List<Int>, val joltage: List<Int>) {

    fun getMinimumPressesForLightDiagrams(): Int {
        val toVisit = ArrayDeque<Int>().apply { add(0) }
        val visited = HashMap<Int, Int>().apply { this[0] = 0 }
        while (toVisit.isNotEmpty()) {
            val state = toVisit.removeFirst()
            if (state == lightDiagram) return visited[lightDiagram]!!

            buttons.map { button -> state.xor(button) }
                .filter { child -> visited[child] == null }
                .forEach { child ->
                    visited[child] = visited[state]!! + 1
                    toVisit.addLast(child)
                }

        }
        return -1
    }


    fun getMinimumPressesForJoltage(): Int {
        val toVisit = ArrayDeque<List<Int>>().apply { add(List(joltage.size) { 0 }) }
        val visited = HashMap<List<Int>, Int>().apply { this[List(joltage.size) { 0 }] = 0 }

        while (toVisit.isNotEmpty()) {
            val state = toVisit.removeFirst()
            if (state == joltage) return visited[joltage]!!

            buttons.map { button -> state.increaseCounter(button) }
                .filter { child -> child.isValid() }
                .filter { child -> visited[child] == null }
                .forEach { child ->
                    visited[child] = visited[state]!! + 1
                    toVisit.addFirst(child)
                }

        }
        return -1
    }

    fun List<Int>.increaseCounter(button: Int) = mapIndexed { i, it -> it + ((button shr i) and 1) }
    fun List<Int>.isValid() = zip(joltage).none { it.first > it.second }
}


val MACHINE_REGEX = """^\[([.#]+)] ((?:\([\d,]+\) ?)+) \{([\d,]+)}$""".toRegex()
fun String.toMachine(): Machine {
    val (lightDiagram, buttons, joltage) = MACHINE_REGEX.find(this)!!.destructured
    return Machine(lightDiagram.toLightDiagram(), buttons.toButtonList(), joltage.toJoltage())
}

fun String.toLightDiagram() = this
    .reversed()
    .map { if (it == '#') 1 else 0 }
    .reduce { acc, a -> (acc shl 1) + a }

fun String.toButtonList() = this
    .split(" ")
    .map {
        it.removePrefix("(")
            .removeSuffix(")")
            .split(",")
            .map { n -> n.toInt() }

    }
    .sortedByDescending { it.size }
    .map {
        it.fold(0) { acc, a -> acc or (1 shl a) }
    }

fun String.toJoltage() = this.split(",").map { it.toInt() }