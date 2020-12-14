package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay14.txt")
    val lines = input.readLines()

    lateinit var mask : Mask
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


    val mem2 = mutableMapOf<Long,Long>()
    lateinit var addrMask : AddrMask
    lines.map{it.split(" = ")}
        .forEach{ (ins, value) ->
            if(ins == "mask") {
                addrMask = AddrMask(value)
            }else {
                val addr = ins.drop(4).dropLast(1).toLong()
                addr.applyAddrMask(addrMask).forEach{
                    mem2[it] = value.toLong()
                }
            }
        }
    println("Part 2 : ${mem2.values.sum()}")

}


fun Long.applyMask(mask: Mask) = this and mask.mask0 or mask.mask1

fun Long.applyAddrMask(mask : AddrMask ) : List<Long> {
    val masked = (this or mask.mask1).toString(2).padStart(36, '0')
    return mask.floatingBits
        .fold(listOf(masked)){ acc, i -> acc.map{it.applyFloatingBit(i)}.flatten() }
        .map{it.toLong(2)}
}

fun String.applyFloatingBit(index : Int) = listOf(
    String(this.toCharArray().also{it[index] = '0' }),
    String(this.toCharArray().also{it[index] = '1' })
)

class Mask(mask: String ) {
    val mask0: Long = mask.replace("X", "1").toLong(2)
    val mask1: Long = mask.replace("X", "0").toLong(2)
}

class AddrMask(mask : String) {
    val mask1: Long = mask.replace("X", "0").toLong(2)
    val floatingBits = mask.mapIndexed{i, it -> it to i}
        .filter{(c,_) -> c == 'X'}
        .map{(_,index) -> index}
        .toList()
}