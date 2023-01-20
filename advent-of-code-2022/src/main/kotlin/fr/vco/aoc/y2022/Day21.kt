package fr.vco.aoc.y2022

import java.lang.Exception

fun main() {
    val input = readLines("Day21")
    val monkeys = input.associate {
        val (name, value) = it.split(": ")
        name to value.toMonkeyNode()
    }

    monkeys.buildTree()
    println("Part 1 : ${monkeys["root"]!!.yell()}")
    monkeys.reverseTree("humn")
    println("Part 2 : ${monkeys["humn"]!!.yell()}")
}

fun String.toMonkeyNode(): MonkeyNode {
    val operation = this.split(" ")
    return if (operation.size == 1) MonkeyNode(value = operation.first().toLong())
    else {
        val (left, op, right) = operation
        MonkeyNode(leftName = left, rightName = right, operation = MonkeyOperation.get(op))
    }
}

fun Map<String, MonkeyNode>.buildTree() = forEach { (_, monkey) ->
    this[monkey.leftName]?.also {
        monkey.leftOp = it
        it.parent = monkey
    }
    this[monkey.rightName]?.also {
        monkey.rightOp = it
        it.parent = monkey
    }
}

fun Map<String, MonkeyNode>.reverseTree(name: String) = this[name]?.reverse()

enum class MonkeyOperation(val compute: (MonkeyNode, MonkeyNode) -> Long) {
    ADD({ a, b -> a.yell() + b.yell() }),
    MINUS({ a, b -> a.yell() - b.yell() }),
    TIME({ a, b -> a.yell() * b.yell() }),
    DIVIDE({ a, b -> a.yell() / b.yell() });

    companion object {
        fun get(operation : String) = when(operation) {
            "+" -> ADD
            "-" -> MINUS
            "*" -> TIME
            "/" -> DIVIDE
            else -> throw Exception("Unknown operation $operation")
        }
        fun reverse( operation : MonkeyOperation) = when(operation) {
            ADD -> MINUS
            MINUS -> ADD
            TIME -> DIVIDE
            DIVIDE -> TIME
        }
    }
}

class MonkeyNode(
    val leftName: String? = null,
    val rightName: String? = null,
    var operation: MonkeyOperation? = null,
    var value: Long? = null,
) {
    var leftOp: MonkeyNode? = null
    var rightOp: MonkeyNode? = null
    var parent: MonkeyNode? = null

    fun yell(): Long {
        if (value == null) value =
            operation?.run { compute(leftOp!!, rightOp!!) } ?: throw Exception("Operation shouldn't be null")
        return value!!
    }

    fun reverse() {
        if (parent == null) {
            value = 0
            operation = null
            leftOp = null
            rightOp = null
        }

        parent?.let {
            value = null
            if (it.leftOp == this || it.operation in listOf(MonkeyOperation.ADD, MonkeyOperation.TIME) ) {
                leftOp = it
                rightOp = if (it.rightOp == this) it.leftOp else it.rightOp
                operation = if(it.parent == null) MonkeyOperation.ADD else MonkeyOperation.reverse(it.operation!!)
            } else {
                leftOp = if (it.rightOp == this) it.leftOp else it.rightOp
                rightOp = it
                operation = if(it.parent == null) MonkeyOperation.ADD else it.operation!!
            }
            it.reverse()
        }
    }
}
