package fr.vco.adventofcode.y2019

import java.util.LinkedList

fun main() {
    val input = readLines("Day23").first()

    println("Part 1: ${Network(input).getFirstNatPacket()}")
    println("Part 2: ${Network(input).getLastNatPacket()}")
}


class Network(input: String, computerCount: Int = 50) {

    companion object {
        const val NAT_ADDRESS = 255
    }

    val computers = List(computerCount) { input.toOpCode() }
    var lastNatPacket: Packet? = null
    var lastNatSent: Packet? = null
    val inputQueues = List(computerCount) { LinkedList<Long>().apply { add(it.toLong()) } }

    fun getFirstNatPacket(): Long {
        runNetworkWhile { lastNatPacket == null }
        return lastNatPacket!!.y
    }

    fun getLastNatPacket(): Long {
        runNetworkWhile { true }
        return lastNatPacket!!.y
    }

    private fun runNetworkWhile(endPredicate: () -> Boolean) {
        while (endPredicate()) {

            computers.forEachIndexed { i, computer ->
                computer.stream.input.addLast(
                    inputQueues[i].removeFirstOrNull() ?: -1
                )
            }
            computers.forEach { it.exec() }

            // READ OUPUT
            computers.mapIndexed { i, it -> i to it.stream.output.toList() }
                .mapNotNull { (source, values) ->
                    values.takeIf { values.isNotEmpty() }
                        ?.chunked(3)
                        ?.map { (dest, x, y) -> Packet(source, dest.toInt(), x, y) }
                }.flatten()
                .forEach {
                    if (it.dest != NAT_ADDRESS) {
                        inputQueues[it.dest].addLast(it.x)
                        inputQueues[it.dest].addLast(it.y)
                    } else {
                        lastNatPacket = it
                    }
                }
            computers.forEach { it.stream.output.clear() }

            if (inputQueues.all { it.isEmpty() } && lastNatPacket != null) {
                if (lastNatSent?.y == lastNatPacket?.y) {
                    break
                }
                inputQueues[0].addLast(lastNatPacket!!.x)
                inputQueues[0].addLast(lastNatPacket!!.y)
                lastNatSent = lastNatPacket
            }
        }
    }

}

data class Packet(val source: Int, val dest: Int, val x: Long, val y: Long)

