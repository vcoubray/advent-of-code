package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import java.util.*


fun main() {
    val input = getInputReader("/2020/inputDay18.txt")
    val lines = input.readLines()


    println(eval("1 + 2 * 3 + 4 * 5 + 6"))
    println(eval("1 + (2 * 3) + (4 * (5 + 6))"))
    // println(eval("2 + (3 + 7 + 7) * 5 + 9 + (5 + (4 + 5 + 3 + 6) + 8 * 5 + (7 + 4 + 7 + 6 + 9) + 9)"))
    //println("Part 1 : ${lines.map { eval(it) }.sum()}")

//    "6 5 4 3 2 1 + * + * +"
//    "1 2 + 3 4 + 5 6 + * *"
}


fun eval(exp: String): Long {
    val prefix = transformPrefix(exp)
    println(prefix)
    val numberStack = LinkedList<Long>()
    val operatorStack = LinkedList<Char>()
    prefix.forEach {
        when {
            it.isDigit() -> numberStack.addFirst("$it".toLong())
            //else -> operatorStack.addFirst(it)
            it == '+' -> (numberStack.pop() + numberStack.pop()).let(numberStack::addFirst)
            it == '*' -> (numberStack.pop() * numberStack.pop()).let(numberStack::addFirst)
        }
    }
    return numberStack.pop()
}

fun transformPrefix(exp: String): String {
    val outputQueue = LinkedList<Char>()
    val operatorStack = LinkedList<Char>()
    exp.replace(" ", "").reversed().forEach {
        when {
            it.isDigit() -> outputQueue.add(it)
            it == ')' -> {
                do {
                    val op = operatorStack.pop()
                    if (op != '(') outputQueue.add(op)
                } while (op != '(')

            }
//            it == '*' -> {
//                if (operatorStack.isNotEmpty() && operatorStack.first == '+') {
//                    outputQueue.add(operatorStack.pop())
//                }
//                operatorStack.add (it)
//            }
            else -> operatorStack.addFirst(it)
        }
    }
    while (operatorStack.isNotEmpty())
        outputQueue.add(operatorStack.pop())

    return outputQueue.joinToString(" ")
}





