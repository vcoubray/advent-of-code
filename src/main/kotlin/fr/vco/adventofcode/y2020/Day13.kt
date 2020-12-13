package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay13.txt")
    val lines = input.readLines()

    val timestamp = lines.first().toInt()
    val bus = lines.last().split(",").filterNot{it == "x"}.map{it.toInt()}

    bus.map{
        var i=timestamp/it
        while(it*i<=timestamp) i++
        it to (i*it)-timestamp
    }.minByOrNull { (bus,time)-> time }?.let{(bus,time)->println("Part 1 : ${bus*time}")}

}