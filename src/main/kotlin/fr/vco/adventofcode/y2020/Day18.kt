package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import java.util.*


fun main() {
    val input = getInputReader("/2020/inputDay18.txt")
    val lines = input.readLines()

    println("Part 1 : ${lines.map { transformPostfix(it) { 0 } }.map(::evalPostfix).sum()}")
    println("Part 2 : ${lines.map { transformPostfix(it, ::precedence) }.map(::evalPostfix).sum()}")

}

fun precedence(op: Char) = if (op == '+') 1 else 0
fun Char.isOperator() = this == '+' || this == '*'

fun transformPostfix(exp: String, precedence: (Char) -> Int): String {
    val postfixExp = mutableListOf<Char>()
    val operatorStack = LinkedList<Char>()
    exp.replace(" ", "").reversed().forEach {
        when {
            it.isDigit() -> postfixExp.add(it)
            it == '(' -> {
                while(operatorStack.first != ')')
                    postfixExp.add(operatorStack.pop())
                operatorStack.pop()
            }
            it.isOperator() -> {
                while (operatorStack.isNotEmpty() && precedence(operatorStack.first) > precedence(it))
                    postfixExp.add(operatorStack.pop())
                operatorStack.addFirst(it)
            }
            else -> operatorStack.addFirst(it)
        }
    }
    while (operatorStack.isNotEmpty())
        postfixExp.add(operatorStack.pop())

    return postfixExp.joinToString(" ")
}

fun evalPostfix(postfix: String): Long {
    val numberStack = LinkedList<Long>()
    postfix.forEach {
        when {
            it.isDigit() -> numberStack.addFirst("$it".toLong())
            it == '+' -> (numberStack.pop() + numberStack.pop()).let(numberStack::addFirst)
            it == '*' -> (numberStack.pop() * numberStack.pop()).let(numberStack::addFirst)
        }
    }
    return numberStack.pop()
}
