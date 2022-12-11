package fr.vco.aoc.y2022

import java.lang.Exception

fun main() {
    val input = readLines("Day11")

    println("Part 1 : ${input.toMonkeys().apply { play(20) { it / 3 } }.getMonkeyBusiness()}")
    val monkeys = input.toMonkeys()
    val globalModulo = monkeys.map{it.testDivider}.reduce { acc, a -> acc * a }
    println("Part 2 : ${input.toMonkeys().apply { play(10000) { it % globalModulo} }.getMonkeyBusiness()}")
}


fun List<String>.toMonkeys() = this.split { it.isBlank() }.map {
    val items = it[1].toItems()
    val operation = it[2].toOperation()
    val test = it[3].toTestDivider()
    val dest1 = it[4].toDest()
    val dest2 = it[5].toDest()
    Monkey(ArrayDeque(items), operation, test, dest1, dest2)
}

fun String.toItems() = trim().removePrefix("Starting items: ").split(", ").map { it.toLong() }
fun String.toDest() = """^.* (\d+)$""".toRegex().find(this)!!.destructured.component1().toInt()

fun String.toOperation(): WorriedOperation {
    val (operand1, operator, operand2) = """^.*Operation: new = (.+) ([+*]) (.+)""".toRegex().find(this)!!.destructured
    return WorriedOperation(operand1, operator, operand2)
}

fun String.toTestDivider(): Long {
    val (divider) = """^.* (\d+)""".toRegex().find(this)!!.destructured
    return divider.toLong()
}

fun List<Monkey>.getMonkeyBusiness() =
    map { it.inspectedItems.toLong() }.sortedByDescending { it }.take(2).reduce { acc, a -> acc * a }

fun List<Monkey>.play(turn: Int, worryModifier: (Long) -> Long = { it }) {
    repeat(turn) {
        this.forEach { monkey -> monkey.play(this, worryModifier) }
    }
}

class WorriedOperation(operand1: String, operator: String, operand2: String) {

    private val operation: (Long, Long) -> Long = when (operator) {
        "+" -> { a, b -> a + b }
        "*" -> { a, b -> a * b }
        else -> throw Exception("oh no")
    }

    private val operand1 = getOperand(operand1)
    private val operand2 = getOperand(operand2)

    private fun getOperand(operand: String): (Long) -> Long =
        if (operand == "old") { it -> it } else { _ -> operand.toLong() }

    fun compute(value: Long) = operation(operand1(value), operand2(value))
}

class Monkey(
    private val items: ArrayDeque<Long>,
    private val operation: WorriedOperation,
    val testDivider: Long,
    private val dest1: Int,
    private val dest2: Int,
) {

    var inspectedItems = 0
    private var itemsToProceed = items.size

    fun play(monkeys: List<Monkey>, worryModifier: (Long) -> Long = { it }) {
        repeat(itemsToProceed) {
            val item = worryModifier(operation.compute(items.removeFirst()))
            val dest = if (item % testDivider == 0L) monkeys[dest1] else monkeys[dest2]
            dest.addItem(item)
        }
        inspectedItems += itemsToProceed
        itemsToProceed = 0
    }

    private fun addItem(item: Long) {
        itemsToProceed++
        items.addLast(item)
    }
}