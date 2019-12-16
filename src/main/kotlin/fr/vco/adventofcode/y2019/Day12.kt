package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import kotlin.math.abs

fun main() {

    val input = getInputReader("/2019/inputDay12.txt")

    val moons = mutableListOf<Moon>()
    input.forEachLine{line->
        val coords = line.split(",").map{it.toInt()}
        moons.add(Moon(coords))
    }

    moons.move(1000)
    println("Part 1 : ${moons.sumBy { it.getEnergy() }}")

}

fun List<Moon>.move(steps: Int) {
    repeat(steps) {
        this.move()
    }
}

fun List<Moon>.move() {
    this.forEach { moon -> moon.applyGravities(this) }
    this.forEach { moon -> moon.move() }
}

data class Coord3D(
    var x: Int,
    var y: Int,
    var z: Int
) {
    constructor(coords: List<Int>) : this(coords[0], coords[1], coords[2])

    fun getEnergy() = abs(x) + abs(y) + abs(z)

}

data class Moon(
    val pos: Coord3D,
    val vel: Coord3D = Coord3D(0, 0, 0)
) {

    constructor(coords: List<Int>) : this(Coord3D(coords))

    fun applyGravity(other: Moon) {
        vel.x += calcAttraction(this.pos, other.pos) { it.x }
        vel.y += calcAttraction(this.pos, other.pos) { it.y }
        vel.z += calcAttraction(this.pos, other.pos) { it.z }
    }

    fun applyGravities(moons: List<Moon>) {
        moons.forEach { moon ->
            this.applyGravity(moon)
        }
    }


    fun move() {
        pos.x += vel.x
        pos.y += vel.y
        pos.z += vel.z
    }

    fun getEnergy() = pos.getEnergy() * vel.getEnergy()
}


fun calcAttraction(moon1: Coord3D, moon2: Coord3D, op: (Coord3D) -> Int) =
    when {
        op(moon1) < op(moon2) -> 1
        op(moon1) > op(moon2) -> -1
        else -> 0
    }
