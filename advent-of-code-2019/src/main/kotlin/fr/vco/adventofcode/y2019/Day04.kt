package fr.vco.adventofcode.y2019

fun main() {

    val start = 256310
    val end = 732736
    var countCode = 0
    var countCode2 = 0
    for (code in start until end) {
        if(code.isValid(false)) countCode++
        if(code.isValid(true )) countCode2++
    }
    println ("Part 1 : $countCode")
    println ("Part 2 : $countCode2")
}

fun Int.isValid(strict : Boolean ) : Boolean {
    val code = this.toString()
    var prevDigit = 0
    code.forEach {
        val digit = it.digitToInt()
        if (digit < prevDigit) return false
        prevDigit = digit
    }
    return if(strict) code.groupBy{it}.filter{it.value.size == 2 }.isNotEmpty()
    else code.groupBy{it}.filter{it.value.size >= 2 }.isNotEmpty()
}

