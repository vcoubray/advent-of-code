package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader


fun main() {
    val input = getInputReader("/2020/inputDay11.txt")
    val lines = input.readLines()

    val seatLayout = SeatLayout(lines.toList())
    seatLayout.doRounds(seatLayout::getAdjacents)
    println("Part 1 : ${seatLayout.seats.count { it.isOccupied() }}")

    val seatLayout2 = SeatLayout(lines)
    seatLayout2.doRounds(seatLayout2::getVisibleAdjacents,5)
    println("Part 2 : ${seatLayout2.seats.count { it.isOccupied() }}")

}


class SeatLayout(
    val height: Int,
    val width: Int,
    var seats: String,
) {
    constructor(seats: List<String>) : this(
        seats.size,
        seats.first().length,
        seats.joinToString("")
    )

    fun getSeat(x: Int, y: Int) = if (x in 0 until width && y in 0 until height) seats[y * width + x] else null
    fun getAdjacents(i: Int) = getAdjacents(i % width, i / width)
    fun getAdjacents(x: Int, y: Int): List<Char> {
        val adjacents = mutableListOf<Char>()
        for (i in x - 1..x + 1) for (j in y - 1..y + 1) if (i != x || j != y) getSeat(i, j)?.let(adjacents::add)
        return adjacents
    }

    fun getVisibleAdjacents(i: Int) = getVisibleAdjacents(i % width, i / width)
    fun getVisibleAdjacents(x: Int, y: Int): List<Char> {
        val adjacents = mutableListOf<Char>()
        for (vx in -1..1) for (vy in -1..1) if (vx != 0 || vy != 0) nextSeat(x+vx,y+vy,vx, vy)?.let(adjacents::add)
        return adjacents
    }
    fun nextSeat(x :Int, y: Int, vx: Int, vy:Int) : Char? = getSeat(x,y)?.let{if(it.isSeat()) it else nextSeat(x+vx,y+vy,vx,vy)}

    fun doRounds(adjacentOp: (Int)->List<Char>, occupiedSeat : Int = 4){
        var changeCount = 1
        while (changeCount > 0) {
            changeCount = 0
            seats = seats.mapIndexed { i, seat ->
                val adjacents = adjacentOp(i)
                val value = when {
                    seat.isEmpty() && adjacents.none { it.isOccupied() } -> '#'
                    seat.isOccupied() && adjacents.count { it.isOccupied() } >= occupiedSeat -> 'L'
                    else -> seat
                }
                if (value != seat) changeCount++
                value
            }.joinToString("")
        }
    }
}

fun Char.isSeat() = this != '.'
fun Char.isEmpty() = this == 'L'
fun Char.isOccupied() = this == '#'