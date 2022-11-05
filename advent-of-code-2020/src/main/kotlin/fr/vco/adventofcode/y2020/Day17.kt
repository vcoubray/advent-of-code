package fr.vco.adventofcode.y2020

fun main() {
    val input = getInputReader("/2020/inputDay17.txt")
    val lines = input.readLines()

    val area = Area().apply { init(lines) }
    println("Part 1 :${area.cycleUntil(6){it.expand()}.count()}")
    println("Part 2 :${area.cycleUntil(6){it.expand4D()}.count()}")

}

data class Cube(val x: Int, val y: Int, val z: Int = 0, val w: Int = 0) {

    fun getNeighbours(): List<Cube> {
        val neighbours = mutableListOf<Cube>()
        for (wi in w - 1..w + 1) for (zi in z - 1..z + 1) for (yi in y - 1..y + 1) for (xi in x - 1..x + 1) {
            val cube = Cube(xi, yi, zi, wi)
            if (cube != this) neighbours.add(cube)
        }
        return neighbours
    }
}

class Area(val cubes: MutableMap<Cube, Boolean> = mutableMapOf()) {

    lateinit var height: IntRange // z
    lateinit var depth: IntRange // y
    lateinit var width: IntRange // x
    lateinit var hyperspace: IntRange // w

    fun init(area: List<String>) {
        hyperspace = 0..0
        height = 0..0
        depth = area.indices
        width = area.first().indices

        area.forEachIndexed { y, line ->
            line.mapIndexed { x, it -> Cube(x, y) to (it == '#') }
                .forEach { (cube, activate) -> cubes[cube] = activate }
        }
    }

    fun getCube(cube: Cube) =
        if (!cubes.containsKey(cube)) false
        else cubes[cube]!!

    fun expand(): Area {
        val area = Area()
        area.height = height.expand(1)
        area.depth = depth.expand(1)
        area.width = width.expand(1)
        area.hyperspace = hyperspace
        for (z in area.height)
            for (y in area.depth)
                for (x in area.width)
                    area.cubes[Cube(x, y, z)] = false
        return area
    }

    fun expand4D(): Area {
        val area = Area()
        area.height = height.expand(1)
        area.depth = depth.expand(1)
        area.width = width.expand(1)
        area.hyperspace = hyperspace.expand(1)

        for (w in area.hyperspace)
            for (z in area.height)
                for (y in area.depth)
                    for (x in area.width)
                        area.cubes[Cube(x, y, z,w)] = false
        return area

    }

    fun IntRange.expand(expansion: Int) = this.first - expansion..this.last + expansion

    fun cycle(expandMethod : (Area) -> Area): Area {
        val area = expandMethod(this)
        for (w in area.hyperspace) for (z in area.height) for (y in area.depth) for (x in area.width) {
            val cube = Cube(x, y, z,w)
            val neighbours = cube.getNeighbours().map { getCube(it) }
            val cubeState = getCube(cube)
            when {
                cubeState && neighbours.count { it } in 2..3 -> area.cubes[cube] = true
                cubeState && neighbours.count { it } !in 2..3 -> area.cubes[cube] = false
                !cubeState && neighbours.count { it } == 3 -> area.cubes[cube] = true
                !cubeState && neighbours.count { it } != 3 -> area.cubes[cube] = false
                else -> area.cubes[cube] = cubeState
            }
        }
        return area
    }

    fun cycleUntil(turn: Int,expandMethod : (Area) -> Area ): Area {
        var area = this
        repeat(turn) {
            area = area.cycle(expandMethod)
        }
        return area
    }

    fun count() = cubes.count { it.value }
}


