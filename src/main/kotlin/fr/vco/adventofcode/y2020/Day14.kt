package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay14.txt")
    val lines = input.readLines()

    var mask = Mask()
    val mem = mutableMapOf<Int,Long>()
    lines.map{it.split(" = ")}
        .forEach{ (ins, value) ->
            if(ins == "mask") {
                mask = Mask(value)
            }else {
                val addr = ins.drop(4).dropLast(1).toInt()
                mem[addr] = value.toLong().applyMask(mask)
            }
        }

    println("Part 1 : ${mem.values.sum()}")


}


fun Long.applyMask(mask: Mask) = this and mask.mask0 or mask.mask1

class Mask(mask: String ="X") {
    var mask0: Long = mask.replace("X", "1").toLong(2)
    var mask1: Long = mask.replace("X", "0").toLong(2)
}