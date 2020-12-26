package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import java.util.*

fun main() {

    val input = getInputReader("/2020/inputDay23.txt")
    val lines = input.readLines()

    val cups = LinkedList(lines.first().map{"$it".toInt()})

    println("Part 1 : ${(0..99).fold(CupCircle(cups,0)){acc, _ -> acc.move()}}")


}


data class CupCircle(val cups: LinkedList<Int>, val currentIndex: Int) {


    fun move() : CupCircle{

        val current = cups.getClockwise(currentIndex)

        // pickUp 3 cups
        val pickup = mutableListOf<Int>()
        val newCircle = LinkedList(cups)
        for (i in (currentIndex + 1)..currentIndex + 3) {
            pickup.add(cups.getClockwise(i))
        }
        newCircle.removeAll(pickup)
        // select destination
        val dest = newCircle.filter { it < current }
            .maxByOrNull { it }
            ?: newCircle.filterNot { it == current }
                .maxByOrNull { it }!!
        val destIndex = newCircle.indexOf(dest)

        // place picked up cups
        pickup.forEachIndexed{i, it ->
            newCircle.add(destIndex+1+i,it)
        }

        return CupCircle(newCircle, newCircle.getClockwiseIndex(newCircle.indexOf(current)+1))
    }

    override fun toString() =
        StringBuffer().apply {
            val index = cups.indexOf(1)
            for (i in 1 until cups.size) {
                this.append(cups.getClockwise( index  + i))
            }
        }.toString()

}

fun LinkedList<Int>.getClockwiseIndex(index: Int) = (index % this.size).let{if(it >=0 ) it else this.size+it }
fun LinkedList<Int>.getClockwise(index: Int) = this[this.getClockwiseIndex(index)]

