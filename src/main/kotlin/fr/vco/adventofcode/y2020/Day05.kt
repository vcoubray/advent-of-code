package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

const val COLUMN_SIZE = 8
const val ROW_SIZE = 128

fun main() {
    val input = getInputReader("/2020/inputDay05.txt")
    val lines = input.readLines()

    println("Part 1 : ${lines.map(::BoardingPass).maxBy { it.id }!!.id}")


}

class BoardingPass(passCode: String) {
    val row: Int = dichotomy(0, ROW_SIZE - 1, passCode.substring(0..6))
    val column: Int = dichotomy(0, COLUMN_SIZE - 1, passCode.substring(7..9))

    val id = row * 8 + column

    fun dichotomy(start: Int, end: Int, passCode: String): Int {
        var min = start
        var max = end
        passCode.forEach{ c->
            val middle = min + (max - min) / 2
            if (c == 'F' || c == 'L') max = middle
            else min = middle + 1
        }
        return min
    }

}