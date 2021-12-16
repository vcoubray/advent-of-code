package fr.vco.aoc.y2021

fun main() {
    val input = readFirstLine("Day16")

    val packet = input.map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")
        .toPacket()

    println("Part 1 : ${packet.sumVersion()}")
    println("Part 2 : ${packet.calculate()}")
}

private fun String.toPacket() = extractFirstPacket(this)

sealed class Packet(val version: Int, val type: Int, val size: Int) {

    abstract fun sumVersion(): Int
    abstract fun calculate(): Long

    class ValuePacket(version: Int, type: Int, size: Int, private val value: Long) : Packet(version, type, size) {
        override fun sumVersion() = version
        override fun calculate() = value
        override fun toString() = "(version=$version, type=$type, size=$size, value=$value)"
    }

    class OperatorPacket(version: Int, type: Int, length: Int, val packets: List<Packet>) :
        Packet(version, type, packets.sumOf { it.size } + 7 + length) {
        override fun toString() = "(version=$version, type=$type, size=$size, packets=$packets)"
        override fun sumVersion() = packets.sumOf { it.sumVersion() } + version
        override fun calculate() = when (type) {
            0 -> packets.sumOf { it.calculate() }
            1 -> packets.fold(1L){acc, a -> acc * a.calculate()}
            2 -> packets.minOf{it.calculate()}
            3 -> packets.maxOf{it.calculate()}
            5 -> packets.let{(p1, p2) -> if (p1.calculate() > p2.calculate()) 1L else 0L}
            6 -> packets.let{(p1, p2) -> if (p1.calculate() < p2.calculate()) 1L else 0L}
            7 -> packets.let{(p1, p2) -> if (p1.calculate() == p2.calculate()) 1L else 0L}
            else -> 0L // Should not happen
        }
    }
}

fun String.getInt(position: Int, size: Int) = this.substring(position,position +size).toInt(2)
fun String.getVersion() = this.getInt(0,3)
fun String.getType() = this.getInt(3,3)
fun String.getLengthType() = this.getInt(6,1)
fun String.getLength() = this.getInt(7, 15)
fun String.packetsCount() = this.getInt(7,11)


fun extractFirstPacket(packet: String): Packet {
    val version = packet.getVersion()
    val type = packet.getType()
    if (type == 4) {
        return parseValuePacket(version, type, packet.drop(6))
    } else {
        if (packet.getLengthType() == 0) {
            val totalLength = packet.getLength()
            var subPacketString = packet.drop(7 + 15)
            val subPackets = mutableListOf<Packet>()
            var lenght = 0
            while (lenght < totalLength) {
                val subPacket = extractFirstPacket(subPacketString)
                subPacketString = subPacketString.drop(subPacket.size)
                subPackets.add(subPacket)
                lenght += subPacket.size
            }
            return Packet.OperatorPacket(version, type, 15, subPackets)
        } else {
            val subPacketsCount = packet.packetsCount()
            var subPacketString = packet.drop(7 + 11)
            val subPackets = mutableListOf<Packet>()
            repeat(subPacketsCount) {
                val subPacket = extractFirstPacket(subPacketString)
                subPacketString = subPacketString.drop(subPacket.size)
                subPackets.add(subPacket)
            }
            return Packet.OperatorPacket(version, type, 11, subPackets)
        }
    }
}

fun parseValuePacket(version: Int, type: Int, packet: String): Packet.ValuePacket {
    var current = 0
    var binary = ""
    do {
        val isFinish = packet.getInt(0,1) == 0
        binary += packet.substring(current + 1, current + 5)
        current += 5
    } while (!isFinish)
    return Packet.ValuePacket(version, type, current + 6, binary.toLong(2))
}
