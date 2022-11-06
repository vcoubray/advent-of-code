package fr.vco.adventofcode.y2020

fun main() {
    val input = getInputReader("/inputDay14.txt")
    val lines = input.readLines()

    lines.fold(Memory()) { mem, ins -> mem.applyInstruction(ins, mem::applyValueMask) }
        .run { println("Part 1 : ${mem.values.sum()}") }

    lines.fold(Memory()) { mem, ins -> mem.applyInstruction(ins, mem::applyAddrMask) }
        .run { println("Part 2 : ${mem.values.sum()}") }
}

class Memory {
    lateinit var mask: Mask
    val mem = mutableMapOf<Long, Long>()

    fun applyInstruction(instruction: String, applyMask: (Long, Long, Mask) -> Unit) = apply {
        val (ins, value) = instruction.split(" = ")
        if (ins == "mask") {
            mask = Mask(value)
        } else {
            val addr = ins.drop(4).dropLast(1).toLong()
            applyMask(addr, value.toLong(), mask)
        }
    }

    fun applyValueMask(addr: Long, value: Long, mask: Mask) {
        mem[addr] = mask.applyValueMask(value)
    }

    fun applyAddrMask(addr: Long, value: Long, mask: Mask) {
        mask.applyAddrMask(addr).forEach {
            mem[it] = value
        }
    }
}

class Mask(mask: String) {
    private val mask0: Long = mask.replace("X", "1").toLong(2)
    private val mask1: Long = mask.replace("X", "0").toLong(2)
    private val floatingBits = mask.mapIndexed { i, it -> it to i }
        .filter { (c, _) -> c == 'X' }
        .map { (_, index) -> index }
        .toList()

    fun applyValueMask(value: Long) = value and mask0 or mask1
    fun applyAddrMask(addr: Long): List<Long> {
        val masked = (addr or mask1).toString(2).padStart(36, '0')
        return floatingBits
            .fold(listOf(masked)) { acc, i -> acc.map { it.applyFloatingBit(i) }.flatten() }
            .map { it.toLong(2) }
    }
}

fun String.applyFloatingBit(index: Int) = listOf(
    String(this.toCharArray().also { it[index] = '0' }),
    String(this.toCharArray().also { it[index] = '1' })
)