package fr.vco.aoc.y2024

import java.math.BigInteger

fun main() {
    val input = readLines("Day07")
    val equations = input.map{it.toEquation()}

    println("Part 1 : ${equations.filter{it.isValid(OPERATORS_PART_1)}.sumOf{it.expected}}")
    println("Part 2 : ${equations.filter{it.isValid(OPERATORS_PART_2)}.sumOf{it.expected}}")
}

typealias Operator = (BigInteger, BigInteger) -> BigInteger
val OPERATORS_PART_1 = listOf<Operator>(
    { a, b -> a + b },
    { a, b -> a * b }
)
val OPERATORS_PART_2 = OPERATORS_PART_1 +  { a, b -> (a.toString() + b.toString()).toBigInteger()}

fun List<Operator>.isEquationValid(expected: BigInteger, startValue: BigInteger, values: List<BigInteger>): Boolean {
    if (values.isEmpty()) return expected == startValue
    return any{operator -> isEquationValid(expected, operator(startValue, values.first()), values.drop(1))}
}

class Equation(val expected: BigInteger, private val values: List<BigInteger>) {
    fun isValid(operators: List<Operator>) = operators.isEquationValid(expected, values.first(), values.drop(1))
}

fun String.toEquation(): Equation {
    val (expected, values) = """(\d+): (.*)""".toRegex().find(this)!!.destructured
    return Equation(expected.toBigInteger(), values.split(" ").map { it.toBigInteger() })
}

