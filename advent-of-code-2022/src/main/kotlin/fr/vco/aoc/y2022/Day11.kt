package fr.vco.aoc.y2022

import java.lang.Exception

fun main() {
    val input = readLines("Day11")

    println("Part 1 : ${input.toMonkeys().apply{play(20) { it / 3 }}.getMonkeyBusiness()}")
    println("Part 2 : ${input.toMonkeys().apply{play(10_000)}.getMonkeyBusiness()}")
}


fun List<String>.toMonkeys() = this.split { it.isBlank() }.map {
    val items = it[1].toItems()
    val operation = it[2].toOperation()
    val test = it[3].toTest()
    val dest1 = it[4].toDest()
    val dest2 = it[5].toDest()
    Monkey(ArrayDeque(items), operation, test, dest1, dest2)
}
fun String.toItems() = trim().removePrefix("Starting items: ").split(", ").map { it.toInt() }
fun String.toDest() = """^.* (\d+)$""".toRegex().find(this)!!.destructured.component1().toInt()

fun String.toOperation(): WorriedOperation {
    val (operand1, operator, operand2) = """^.*Operation: new = (.+) ([+*]) (.+)""".toRegex().find(this)!!.destructured
    return WorriedOperation(operand1, operator, operand2)
}

fun String.toTest(): (Int) -> Boolean {
    val (test, value) = """^.* (.+) by (\d+)""".toRegex().find(this)!!.destructured
    return when (test) {
        "divisible" -> { it -> it % value.toInt() == 0 }
        else -> { _ -> false }
    }
}

fun List<Monkey>.getMonkeyBusiness() =
    map { it.inspectedItems }.sortedByDescending { it }.take(2).reduce { acc, a -> acc * a }

fun List<Monkey>.play(turn: Int, worryModifier: (Int) -> Int = { it }) {
    repeat(turn) {
        this.forEach { monkey -> monkey.play(this, worryModifier) }
    }
}

class WorriedOperation(operand1: String, operator: String, operand2: String) {

    private val operation: (Int, Int) -> Int = when (operator) {
        "+" -> { a, b -> a + b }
        "*" -> { a, b -> a * b }
        else -> throw Exception("oh no")
    }

    private val operand1 = getOperand(operand1)
    private val operand2 = getOperand(operand2)

    private fun getOperand(operand: String): (Int) -> Int =
        if (operand == "old") { it -> it } else { _ -> operand.toInt() }

    fun compute(value: Int) = operation(operand1(value), operand2(value))
}

class Monkey(
    private val items: ArrayDeque<Int>,
    private val operation: WorriedOperation,
    private val test: (Int) -> Boolean,
    private val dest1: Int,
    private val dest2: Int,
) {

    var inspectedItems = 0L
    private var itemsToProceed = items.size

    fun play(monkeys: List<Monkey>, worryModifier: (Int) -> Int = { it }) {
        repeat(itemsToProceed) {
            var item = items.removeFirst()
            item = worryModifier(operation.compute(item))

            val dest = if (test(item)) monkeys[dest1] else monkeys[dest2]
            dest.addItem(item)
        }
        inspectedItems += itemsToProceed
        itemsToProceed = 0
    }

    private fun addItem(item: Int) {
        itemsToProceed++
        items.addLast(item)
    }
}