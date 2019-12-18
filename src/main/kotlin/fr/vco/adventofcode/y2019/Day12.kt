package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import kotlin.math.abs

fun main() {

    val input = getInputReader("/2019/inputDay12.txt")

    val moons = mutableListOf<Moon>()
    input.forEachLine { line ->
        val coords = line.split(",").map { it.toInt() }
        moons.add(Moon(coords))
    }
    val orbits = Orbits(moons)
    println("Part 1 : ${orbits.getEnergy(1000)}")
    println("Part 2 : ${orbits.cycle()}")

}


fun List<Moon>.move() {
    this.forEach { moon -> moon.applyGravities(this) }
    this.forEach { moon -> moon.move() }
}


class Orbits(val originMoons: List<Moon>) {
    private var repeatX: Long? = null
    private var repeatY: Long? = null
    private var repeatZ: Long? = null

    fun getEnergy(steps: Int): Int {
        val moons = originMoons.map { Moon(it.pos.copy(), it.vel.copy()) }
        repeat(steps) {
            moons.move()
        }
        return moons.sumBy { it.getEnergy() }
    }

    fun cycle(): Long {
        val moons = originMoons.map { Moon(it.pos.copy(), it.vel.copy()) }
        var step = 0L
        while (repeatX == null || repeatY == null || repeatZ == null) {
            step++
            moons.move()
            if (repeatX == null && isSameOriginAxe(moons) { it.x }) repeatX = step
            if (repeatY == null && isSameOriginAxe(moons) { it.y }) repeatY = step
            if (repeatZ == null && isSameOriginAxe(moons) { it.z }) repeatZ = step
        }
        return PPMC(PPMC(repeatX!!, repeatY!!), repeatZ!!)
    }


    fun isSameOriginAxe(moons: List<Moon>, axe: (Coord3D) -> Int): Boolean {
        moons.forEachIndexed { i, it ->
            if (axe(it.vel) != axe(originMoons[i].vel) || axe(it.pos) != axe(originMoons[i].pos)) return false
        }
        return true
    }

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


fun PPDC(a: Long, b: Long): Long {
    if (a < b) return PPDC(b, a)
    if(b == 0L) return a
    return  PPDC(b, a % b)
}

fun PPMC(a: Long, b: Long): Long {
    val ppdc = PPDC(a, b)
    return a * b / ppdc
}
