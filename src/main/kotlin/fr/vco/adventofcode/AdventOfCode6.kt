package fr.vco.adventofcode

import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

fun main () {

    val input = getInputReader("/inputAoC6.txt")

    val origin = getOrbitTree(input)
    println("Part 1 : ${getOrbitCount(origin)}")
}

fun getOrbitTree( input: InputStreamReader) : Planet {
    val planetsMap = hashMapOf<String,Planet>()
    lateinit var origin : Planet
    input.readLines().forEach{
        val orbit = it.split(")")
        val parentName = orbit[0]
        val childName = orbit[1]

        val planetParent = planetsMap[parentName]?:Planet(parentName, mutableListOf(),null)
        val planetChild = planetsMap[childName]?:Planet(childName,mutableListOf(),planetParent)

        planetParent.childs.add(planetChild)

        planetsMap[parentName] = planetParent
        planetsMap[childName] = planetChild

        if (parentName == "COM") origin = planetParent
    }
    return origin
}


fun getOrbitCount(origin : Planet) : Int {
    var count = 0

    val planetToVisit = LinkedList<PlanetOrbit>()
    planetToVisit.add(PlanetOrbit(origin,0))

    while (planetToVisit.isNotEmpty()){
        val currentPlanetOrbit = planetToVisit.poll()!!
        val orbitChild = currentPlanetOrbit.orbitCount+1
        currentPlanetOrbit.planet.childs.forEach{ child ->
            count+= orbitChild
            planetToVisit.add(PlanetOrbit(child,orbitChild))
        }

    }

    return count
}


class Planet(
    val name : String,
    val childs : MutableList<Planet>,
    val parent: Planet?
)

class PlanetOrbit(
    val planet : Planet,
    val orbitCount : Int
)