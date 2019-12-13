package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = getInputReader("/2019/inputDay03.txt")
    val lines = input.readLines()

    solve(
        "R75,D30,R83,U83,L12,D49,R71,U7,L72",
        "U62,R66,U55,R34,D71,R55,D58,R83"
    )
    // Part 1 = 159
    // Part 2 = 610

    solve(
        "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
        "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
    )
    // Part 1 = 135
    // Part 2 = 410

    solve(lines[0], lines[1])


}

fun solve(w1 : String, w2 : String){
    val wire1 = Wire(w1)
    val wire2 = Wire(w2)
    val crossingsCoord = wire1.crossingCoords(wire2)

    println("Part 1 : ${crossingsCoord.map { it.coord.distance(
        Coord(
            0,
            0
        )
    ) }.min()!!}")
    println("Part 2 : ${crossingsCoord.minBy{it.steps}?.steps}")
}


data class Coord(val x: Int, val y :Int) {
    fun distance(other: Coord) = abs(this.x-other.x) + abs(this.y-other.y)
}

data class Segment(val start : Coord, val end : Coord, val length:Int) {
    fun isHorizontal() = this.start.y - this.end.y == 0
    fun isParallel(other : Segment) = this.isHorizontal() == other.isHorizontal()

    fun getIntersection(other: Segment) : Coord? {
        if (this.isParallel(other)) return null
        val inter = if(this.isHorizontal()) {
            getIntersection(this, other)
        } else {
            getIntersection(other, this)
        }
        return if (inter != Coord(0, 0)) inter else null
    }

}


fun getIntersection(hor : Segment, vert : Segment) : Coord? {
    val x = vert.start.x
    val y = hor.start.y
    val firstY = min(vert.start.y, vert.end.y)
    val lastY = max (vert.start.y, vert.end.y)
    val firstX = min(hor.start.x, hor.end.x)
    val lastX = max (hor.start.x, hor.end.x)
    return if (y in firstY until lastY && x in firstX until lastX) Coord(
        x,
        y
    )
    else null
}


class Wire() {

    constructor(path :String) : this(){
        var origin= Coord(0, 0)
        val paths = path.trim().split(",")
        paths.forEach{ p ->
            val dir = p[0]
            val dist = p.substring(1,p.length).toInt()

            val end = when(dir){
                'U' -> origin.copy(y = origin.y+dist)
                'R' -> origin.copy(x = origin.x+dist)
                'D' -> origin.copy(y = origin.y-dist)
                'L' -> origin.copy(x = origin.x-dist)
                else -> Coord(0, 0)
            }
            segments.add(Segment(origin, end, dist))
            origin = end.copy()
        }
    }

    private val segments = mutableListOf<Segment>()


    fun crossingCoords(other : Wire) : List<Intersection>{
        val intersections = mutableListOf<Intersection>()
        var dist1 = 0
        segments.forEach{s ->
            var dist2 = 0
            for (os in other.segments) {
                val c = s.getIntersection(os)
                if ( c != null) {
                    val steps = dist1 + c.distance(s.start) + dist2 + c.distance(os.start)
                    intersections.add(Intersection(c, steps))
                }
                dist2+=os.length
            }
            dist1 += s.length
        }
        return intersections
    }

}


data class Intersection(val coord: Coord, val steps : Int)