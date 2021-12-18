package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day18")

    val numbers = input.map{it.toSnailFishNumber()}
    numbers.forEach(::println)
}

fun String.toSnailFishNumber() : SnailFishNumber {
    if (!this.startsWith('[') ) return SnailFishNumber.Number(this.toInt())
    val s = this.drop(1).dropLast(1)
    val sepId = s.findSeparatorIndex()
    return SnailFishNumber.Pair(s.take(sepId).toSnailFishNumber(), s.drop(sepId+1).toSnailFishNumber())
}

fun String.findSeparatorIndex() :Int {
    var counter = 0
    this.forEachIndexed { i, it ->
        when {
            it == '[' -> counter++
            it == ']' -> counter--
            it == ',' && counter == 0 -> return i
        }
    }
    return -1
}

abstract class SnailFishNumber (val parent : SnailFishNumber? = null) {

    operator fun plus(number: SnailFishNumber) = Pair(this, number)
    operator fun plus(number: Int) = Pair(this, Number(number))
    abstract fun magnitude() : Int

    class Number(val value: Int) : SnailFishNumber() {
        override fun magnitude() = value
        override fun toString() = "$value"
    }

    class Pair(val left: SnailFishNumber, val right: SnailFishNumber) : SnailFishNumber() {
        override fun magnitude()= 3 * left.magnitude()+ 2 * right.magnitude()
        override fun toString() = "[$left,$right]"
    }
}