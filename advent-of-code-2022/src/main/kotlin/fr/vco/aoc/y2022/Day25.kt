package fr.vco.aoc.y2022

import kotlin.math.pow

fun main() {
    val input = readLines("Day25")

    println("Part 1 : ${input.sumOf { it.toDecimal() }.apply(::println).toSnafu()}")
}


fun String.toDecimal() = this.reversed()
    .mapIndexed { i, n -> n.toDecimal() * 5.toDouble().pow(i) }
    .sumOf { it.toLong() }

fun Char.toDecimal() = when (this) {
    '=' -> -2
    '-' -> -1
    '0' -> 0
    '1' -> 1
    '2' -> 2
    else -> throw Exception("Not a SNAFU")
}

fun Long.toSnafu(): String {
    var res = ""
    var i = this
    while (i > 0) {
        res += (i % 5).toSnafuUnit()
        i = (i + 2) / 5
    }
    return res.reversed()
}

fun Long.toSnafuUnit(): Char {
    return when (this) {
        0L -> '0'
        1L -> '1'
        2L -> '2'
        3L -> '='
        4L -> '-'
        else -> throw Exception("Not a SNAFU")
    }
}