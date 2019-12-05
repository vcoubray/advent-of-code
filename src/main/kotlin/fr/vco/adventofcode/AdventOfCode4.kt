package fr.vco.adventofcode

fun main() {

    val start = 256310
    val end = 732736

    var countCode = 0
    for (code in start until end) {
        if(code.isValid()) countCode++
    }
    println ("Part 1 : $countCode")
}


fun Int.isValid() : Boolean {
    val code = this.toString()
    var prevDigit = 0
    var hasDouble = false
    code.forEach {
        val digit = it.toInt()
        if (digit < prevDigit) return false
        if (digit == prevDigit) hasDouble = true
        prevDigit = digit
    }
    return hasDouble
}