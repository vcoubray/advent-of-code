package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay02.txt")
    val lines = input.readLines()
    val passwords = lines.map{ it ->
        val (range, letter, password) = it.split(" ")
        val (min, max) = range.split("-").map{r -> r.toInt()}
        password(min, max, letter.first(), password)
    }

    println("Part 1 : ${passwords.filter{it.isValid()}.size}")
    println("Part 2 : ${passwords.filter{it.isValidPart2()}.size}")

}

data class password(val min :Int, val max: Int,val letter :Char, val password :String ) {

    fun isValid() = password.count{it == letter} in min .. max

    fun isValidPart2() = (password[min-1] == letter) xor (password[max-1] == letter)
}