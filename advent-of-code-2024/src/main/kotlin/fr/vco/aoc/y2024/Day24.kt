package fr.vco.aoc.y2024


fun main() {
    val input = readLines("Day24")

    val (values, logicalOperator) = input.split { it.isBlank() }
    val operators = logicalOperator.toLogicalOperator()
    val (xInput, yInput) = values.toGateValues().partition { (name, _) -> name.startsWith("x") }

    val adder = BinaryAdder(xInput.size, operators)
    val x = xInput.sortedBy { (name, _) -> name }.foldIndexed(0L) { i, acc, (_, bit) -> acc or (bit shl i) }
    val y = yInput.sortedBy { (name, _) -> name }.foldIndexed(0L) { i, acc, (_, bit) -> acc or (bit shl i) }

    println("Part 1: ${adder.add(x, y)}")
    println("Part 2: ${findValidSwaps(adder, findPotentialSwaps(adder).toList()).sorted().joinToString(",")}")
}


fun findPotentialSwaps(adder: BinaryAdder): Set<Pair<String, String>> {
    val wrongFullAdders = adder.identifyInvalidFullAdders()
    val gates = adder.parents.keys.toList()

    val potentialSwaps = mutableSetOf<Pair<String, String>>()
    for (i in 0..<gates.size - 1) {
        for (j in i + 1..<gates.size) {
            if (gates[i] == gates[j]) continue
            val swaps = listOf(gates[i], gates[j])

            if (adder.isCyclic(swaps)) {
                continue
            }

            for (fullAdderId in wrongFullAdders) {
                if (adder.isFullAdderValid(fullAdderId, swaps)) {
                    potentialSwaps.add(gates[i] to gates[j])
                }
            }
        }
    }
    return potentialSwaps
}

fun findValidSwaps(adder: BinaryAdder, potentialSwaps: List<Pair<String, String>>): List<String> {
    for (i in 0..<potentialSwaps.size - 3) {
        for (j in i + 1..<potentialSwaps.size - 2) {
            for (k in j + 1..<potentialSwaps.size - 1) {
                for (l in k + 1..<potentialSwaps.size) {
                    val swaps = listOf(
                        potentialSwaps[i],
                        potentialSwaps[j],
                        potentialSwaps[k],
                        potentialSwaps[l]
                    ).flatMap { it.toList() }
                    if (swaps.toSet().size != 8) continue
                    if (adder.isCyclic(swaps)) continue

                    val z = adder.identifyInvalidFullAdders(swaps).count()
                    if (z == 0) {
                        return swaps
                    }
                }
            }
        }
    }
    return emptyList()
}


sealed interface LogicalGate {
    fun getValue(gates: Map<String, LogicalGate>): Boolean
}

class LogicalValue(var value: Boolean) : LogicalGate {
    override fun getValue(gates: Map<String, LogicalGate>): Boolean {
        return value
    }
}

class LogicalOperator(val operand1: String, val operand2: String, val operator: (Boolean, Boolean) -> Boolean) :
    LogicalGate {
    override fun getValue(gates: Map<String, LogicalGate>): Boolean {
        return operator(gates[operand1]!!.getValue(gates), gates[operand2]!!.getValue(gates))
    }
}

fun List<String>.toGateValues(): List<Pair<String, Long>> {
    return this.map {
        val (name, value) = it.split(": ")
        name to value.toLong()
    }
}

fun List<String>.toLogicalOperator(): Map<String, LogicalOperator> {
    return this.associate {
        val (operand1, operator, operand2, name) = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex().find(it)!!.destructured
        name to LogicalOperator(operand1, operand2, BinaryAdder.OPERATORS[operator]!!)
    }
}

class BinaryAdder(
    private val inputSize: Int,
    initialLinks: Map<String, LogicalOperator>,
) {
    companion object {
        val OPERATORS = mapOf(
            "AND" to { a: Boolean, b: Boolean -> a && b },
            "OR" to { a: Boolean, b: Boolean -> a || b },
            "XOR" to { a: Boolean, b: Boolean -> a.xor(b) },
        )
    }

    private val outputSize = initialLinks.keys.count { it.startsWith("z") }

    private val xInput = List(inputSize) { getGateName("x", it) to LogicalValue(false) }.toMap()
    private val yInput = List(inputSize) { getGateName("y", it) to LogicalValue(false) }.toMap()
    private val zOutput = List(outputSize) { getGateName("z", it) }

    val parents = mutableMapOf<String, Set<String>>()
    private val fullAdders = mutableMapOf<String, Set<String>>()

    private var initialGates: Map<String, LogicalGate> = xInput + yInput + initialLinks

    init {

        initialLinks.keys.forEach { name ->
            parents[name] = getParents(name, initialLinks)
        }

        val alreadyAdded = mutableSetOf<String>()
        zOutput.forEach {
            fullAdders[it] = parents[it]!! - alreadyAdded
            alreadyAdded += parents[it]!!
        }
    }

    fun add(x: Long, y: Long, swaps: List<String> = emptyList()): Long {
        for (i in 0..<inputSize) {
            xInput[getGateName("x", i)]!!.value = (x shr i) and 1 == 1L
            yInput[getGateName("y", i)]!!.value = (y shr i) and 1 == 1L
        }

        val gates = swapLinks(swaps)

        return zOutput.sortedDescending().map { gates[it]!!.getValue(gates) }.joinToString("") { if (it) "1" else "0" }
            .toLong(radix = 2)
    }

    fun isCyclic(swaps: List<String>): Boolean {
        val gates = swapLinks(swaps)

        swaps.forEach { start ->
            val toVisit = ArrayDeque<String>().apply { add(start) }
            val visited = mutableMapOf(start to true)
            while (toVisit.isNotEmpty()) {
                val curr = toVisit.removeFirst()

                val children = when (val gate = gates[curr]) {
                    is LogicalOperator -> listOf(gate.operand1, gate.operand2)
                    else -> listOf(null)
                }
                children.forEach { operand ->
                    if (operand == start) return true
                    if (operand != null && operand !in visited) {
                        toVisit.add(operand)
                        visited[operand] = true
                    }
                }
            }
        }
        return false
    }


    private fun swapLinks(swaps: List<String>): MutableMap<String, LogicalGate> {
        val swappedLinks = initialGates.toMutableMap()
        swaps.chunked(2).forEach { (name1, name2) ->
            swappedLinks[name1] = initialGates[name2]!!
            swappedLinks[name2] = initialGates[name1]!!
        }
        return swappedLinks
    }

    private fun getParents(name: String, gates: Map<String, LogicalOperator>): Set<String> {
        val toVisit = ArrayDeque<String>().apply { add(name) }
        val visited = mutableMapOf(name to true)

        while (toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()

            val children = listOf(gates[curr]?.operand1, gates[curr]?.operand2)
            children.forEach { child ->
                if (child != null) {
                    if (parents.containsKey(child)) {
                        parents[child]?.forEach {
                            visited[it] = true
                        }
                    } else {
                        visited[child] = true
                        toVisit.add(child)
                    }
                }
            }
        }
        return visited.keys
    }

    private fun getGateName(prefix: String, i: Int) = prefix + i.toString().padStart(2, '0')

    fun isFullAdderValid(i: Int, swaps: List<String> = emptyList()): Boolean {
        val tests = listOf(
            "00" to "00",
            "00" to "10",
            "10" to "00",
            "10" to "10",
            "11" to "01",
            "01" to "11",
            "11" to "11"
        ).map { (x, y) -> x.toLong(radix = 2) to y.toLong(radix = 2) }

        tests.forEach { (x, y) ->
            val xi = x shl (i - 1)
            val yi = y shl (i - 1)

            if (xi in 0..<(1L shl inputSize) && yi in 0..<(1L shl inputSize)) {
                val z = add(xi, yi, swaps)
                if (z != xi + yi) {
                    return false
                }
            }
        }
        return true
    }

    fun identifyInvalidFullAdders(swaps: List<String> = emptyList()): List<Int> {
        val incorrectAdders = mutableListOf<Int>()
        for (i in 0..<outputSize) {
            if (!isFullAdderValid(i, swaps)) {
                incorrectAdders.add(i)
            }
        }
        return incorrectAdders
    }

}

