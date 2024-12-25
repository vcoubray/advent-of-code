package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day24")

    val (values, logicalOperator) = input.split { it.isBlank() }
    val gates = values.toGateValues() + logicalOperator.toLogicalOperator()

    val x = gates.getBooleanArray("x").toLong()
    val y = gates.getBooleanArray("y").toLong()
    val z = gates.getBooleanArray("z").toLong()


    println("Part 1: $z")
    val expectedZ = x + y
    println(z.toString(2))
    println(expectedZ.toString(2))
    println(z.xor(expectedZ).toString(2).padStart(46, '0'))
    val zDiff = z.xor(expectedZ).toString(2).padStart(46, '0').reversed()
    val logicalGateDiff = """1+""".toRegex().findAll(zDiff)
        .map { it.range }
        .map { it.map { i -> "z" + i.toString().padStart(2, '0') } }
        .toList()

}


interface LogicalGate {
    fun getValue(gates: Map<String, LogicalGate>): Boolean
}

class LogicalValue(private val value: Boolean) : LogicalGate {
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

fun List<String>.toGateValues(): Map<String, LogicalGate> {
    return this.associate {
        val (name, value) = it.split(": ")
        name to LogicalValue(value == "1")
    }
}

val OPERATORS = mapOf(
    "AND" to { a: Boolean, b: Boolean -> a && b },
    "OR" to { a: Boolean, b: Boolean -> a || b },
    "XOR" to { a: Boolean, b: Boolean -> a.xor(b) },
)

fun List<String>.toLogicalOperator(): Map<String, LogicalGate> {
    return this.associate {
        val (operand1, operator, operand2, name) = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex().find(it)!!.destructured
        name to LogicalOperator(operand1, operand2, OPERATORS[operator]!!)
    }
}

fun List<Boolean>.toLong() = this.joinToString("") { if (it) "1" else "0" }.toLong(radix = 2)
fun Map<String, LogicalGate>.getBooleanArray(prefix: String) =
    this.keys.filter { name -> name.startsWith(prefix) }.sortedDescending()
        .map { this[it]!!.getValue(this) }

fun Map<String, LogicalGate>.getAllParents(name: String): List<String> {
    val toVisit = ArrayDeque<String>().apply { add(name) }
    val parents = mutableListOf<String>()

    while (toVisit.isNotEmpty()) {
        val curr = this[toVisit.removeFirst()]!!

        if (curr is LogicalOperator) {
            if (curr.operand1 !in parents) {
                toVisit.add(curr.operand1)
                parents.add(curr.operand1)
            }
            if (curr.operand2 !in parents) {
                toVisit.add(curr.operand2)
                parents.add(curr.operand2)
            }
        }
    }

    return parents
}

