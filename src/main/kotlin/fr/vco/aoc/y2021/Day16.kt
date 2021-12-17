package fr.vco.aoc.y2021

fun main() {
    val input = readFirstLine("Day16")
    val packet = input.map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")
        .toPacket()

    println("Part 1 : ${packet.sumVersion()}")
    println("Part 2 : ${packet.calculate()}")
}

private fun String.toPacket() = PacketBuilder(this).buildPacket()

sealed class Packet(val version: Int, val type: Int, val size: Int) {
    abstract fun sumVersion(): Int
    abstract fun calculate(): Long

    class ValuePacket(version: Int, type: Int, size: Int, private val value: Long) : Packet(version, type, size) {
        override fun sumVersion() = version
        override fun calculate() = value
    }

    class OperatorPacket(version: Int, type: Int, headerSize: Int, private val packets: List<Packet>) :
        Packet(version, type, packets.sumOf { it.size } + headerSize) {

        override fun sumVersion() = packets.sumOf { it.sumVersion() } + version
        override fun calculate() = when (type) {
            0 -> packets.sumOf { it.calculate() }
            1 -> packets.fold(1L) { acc, a -> acc * a.calculate() }
            2 -> packets.minOf { it.calculate() }
            3 -> packets.maxOf { it.calculate() }
            5 -> packets.let { (p1, p2) -> if (p1.calculate() > p2.calculate()) 1L else 0L }
            6 -> packets.let { (p1, p2) -> if (p1.calculate() < p2.calculate()) 1L else 0L }
            7 -> packets.let { (p1, p2) -> if (p1.calculate() == p2.calculate()) 1L else 0L }
            else -> 0L // Should not happen
        }
    }
}

class PacketBuilder(binary: String) {
    private val version: Int
    private val type: Int
    private val length: Int
    private val headerSize: Int
    private val content: String
    private val predicate: (List<Packet>) -> Boolean

    init {
        version = binary.getVersion()
        type = binary.getType()
        when {
            type == 4 -> {
                length = 0
                headerSize = 6
                predicate = { false }
            }
            binary.getLengthType() == 0 -> {
                length = binary.getLength()
                headerSize = 22
                predicate = { subPackets -> subPackets.sumOf { it.size } < length }
            }
            else -> {
                length = binary.getPacketsCount()
                headerSize = 18
                predicate = { subPackets -> subPackets.size < length }
            }
        }
        content = binary.drop(headerSize)
    }

    private fun String.getInt(position: Int, size: Int) = this.substring(position, position + size).toInt(2)
    private fun String.getVersion() = this.getInt(0, 3)
    private fun String.getType() = this.getInt(3, 3)
    private fun String.getLengthType() = this.getInt(6, 1)
    private fun String.getLength() = this.getInt(7, 15)
    private fun String.getPacketsCount() = this.getInt(7, 11)

    fun buildPacket() =
        if (type == 4) parseValuePacket()
        else Packet.OperatorPacket(version, type, headerSize, parseSubPacket())

    private fun parseValuePacket(): Packet.ValuePacket {
        var currentId = 0
        var binaryValue = ""
        do {
            val isFinish = content.getInt(currentId, 1) == 0
            binaryValue += content.substring(currentId + 1, currentId + 5)
            currentId += 5
        } while (!isFinish)
        return Packet.ValuePacket(version, type, headerSize + currentId, binaryValue.toLong(2))
    }

    private fun parseSubPacket(): List<Packet> {
        return buildList {
            var subPacketString = content
            while (predicate(this)) {
                val subPacket = PacketBuilder(subPacketString).buildPacket()
                subPacketString = subPacketString.drop(subPacket.size)
                this.add(subPacket)
            }
        }
    }

}
