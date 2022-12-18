package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day13")

    val packets = input
        .filter { it.isNotBlank() }
        .map(::toPacket)

    packets.chunked(2)
        .mapIndexed { i, (a, b) -> i + 1 to a.compare(b) }
        .fold(0) { acc, (i, a) -> acc + if (a <= 0) i else 0 }
        .let { println("Part 1 : $it") }

    val dividers = listOf("[[2]]", "[[6]]").map(::toPacket)
    val packetPart2 = packets + dividers
    println("Part 2 : ${dividers.fold(1) { acc, divider -> acc * packetPart2.count { divider.compare(it) >= 0 } }}")

}

class PacketNode(
    var value: String = "",
    val children: MutableList<PacketNode> = mutableListOf(),
) {
    override fun toString(): String {
        return if (children.isNotEmpty()) children.joinToString(",", "[", "]")
        else value
    }

    fun compare(node: PacketNode): Int {
        return if (this.value != "" && node.value != "") this.value.toInt() - node.value.toInt()
        else compareList(this.children, node.children)
    }

    private fun compareList(list1: List<PacketNode>, list2: List<PacketNode>): Int {
        for ((a, b) in list1.zip(list2)) {
            if (a.compare(b) == 0) continue
            else return a.compare(b)
        }
        return list1.size - list2.size
    }

    fun updateValue(input: Char) {
        value += input
        if (children.isEmpty()) children.add(this)
    }
}

fun toPacket(input: String): PacketNode {
    val stack = ArrayDeque<PacketNode>()
    var currentPacket = PacketNode()
    input.forEach {
        when (it) {
            '[' -> {
                stack.addFirst(currentPacket)
                currentPacket = PacketNode()
            }

            ',' -> {
                stack.first().children.add(currentPacket)
                currentPacket = PacketNode()
            }

            ']' -> {
                stack.first().children.add(currentPacket)
                currentPacket = stack.removeFirst()
            }

            else -> currentPacket.updateValue(it)
        }
    }
    return currentPacket
}