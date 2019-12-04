package fr.vco.adventofcode

import kotlin.math.abs

fun main() {
    val input = getInputReader("/inputAoC3.txt")
    val lines = input.readLines()

    val s1 = Segment(Coord(5,1), Coord(5,6))
    val s2 = Segment(Coord(4,3), Coord (6,3))

    println( s1.crossingCord(s2))

    val wire1 = Wire(lines[0])
    val wire2 = Wire(lines[1])

    val crossingsCoord = wire1.crossingCoords(wire2)

    val dist = crossingsCoord.map { it.distance(Coord(0, 0)) }.min()

    println(dist)
}

data class Coord(val x: Int, val y :Int) {
    fun distance(other: Coord) = abs(this.x-other.x) + abs(this.y-other.y)
}

data class Segment(val start : Coord, val end :Coord) {

    fun crossingCord(other: Segment) : Coord? {
        val dx = this.start.x - this.end.x
        val dy = this.start.y - this.end.y
        val odx = other.start.x - other.end.x
        val ody = other.start.y - other.end.y

        val inter = if (dx == 0) {
            if (odx == 0) return null
            val range = IntRange(this.start.y, this.end.y)
            if (range.contains(other.start.y)) Coord(this.start.x,other.start.y)
            else null
        } else if (dy == 0) {
            if (ody == 0) return null
            val range = IntRange(this.start.x, this.end.x)
            if (range.contains(other.start.x)) Coord(other.start.x,this.start.y)
            else null
        } else return null
        return if (inter != Coord(0,0)) inter else null
    }
}


class Wire() {

    constructor(path :String) : this(){
        var origin= Coord(0,0)
        val paths = path.trim().split(",")

        paths.forEach{ p ->
            val dir = p[0]
            val dist = p.substring(1,p.length).toInt()

            val end = when(dir){
                'U' -> origin.copy(y = origin.y+dist)
                'R' -> origin.copy(x = origin.x+dist)
                'D' -> origin.copy(y = origin.y-dist)
                'L' -> origin.copy(x = origin.x-dist)
                else -> Coord(0,0)
            }
            segments.add(Segment(origin,end))
            origin = end.copy()
        }
        println(segments)

    }

    private val segments = mutableListOf<Segment>()

    fun crossingCoords(other : Wire) : List<Coord>{
        val crossingCoords = mutableListOf<Coord>()
        segments.forEach{s ->
            for (os in other.segments) {

                val c = s.crossingCord(os)
                if ( c != null) crossingCoords.add(c)
            }
        }
        return crossingCoords
    }

}