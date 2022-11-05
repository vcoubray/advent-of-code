package fr.vco.aoc.y2021

import java.util.*
import kotlin.math.absoluteValue

const val OVERLAP_BEACON_COUNT = 12

fun main() {
    val input = readLines("Day19")

    val scanners = input.split { it == "" }.map { scanner ->
        val (id) = """(\d+)""".toRegex().find(scanner.first())!!.destructured
        Scanner(
            id.toInt(),
            scanner.drop(1).map { line ->
                line.split(",").map { it.toInt() }.let { (x, y, z) -> Point3D(x, y, z) }
            }.toSet()
        )
    }

    val map = scanners.toSynchronizedScanners()
    println("Part 1 : ${map.getBeacons().size}")
    println("Part 2 : ${map.maxDistance()}")
}


data class Point3D(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(p: Point3D) = Point3D(x + p.x, y + p.y, z + p.z)
    operator fun minus(p: Point3D) = Point3D(x - p.x, y - p.y, z - p.z)
    operator fun unaryMinus() = Point3D(-x, -y, -z)
    private fun rotateX() = copy(y = z, z = -y)
    private fun rotateY() = copy(x = -z, z = x)
    private fun rotateZ() = copy(x = y, y = -x)

    fun rotateX(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateX() }
    fun rotateY(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateY() }
    fun rotateZ(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateZ() }

    fun dist(point: Point3D) = (this - point).dist()
    fun dist() = x.absoluteValue + y.absoluteValue + z.absoluteValue
}

data class Transformation(val translation: Point3D = Point3D(), val rotation: Point3D = Point3D()) {
    fun rotate(p: Point3D) = p.rotateX(rotation.x).rotateY(rotation.y).rotateZ(rotation.z)
    fun transform(p: Point3D) = rotate(p) + translation

}

data class Scanner(val id: Int, val beacons: Set<Point3D>, val position: Point3D = Point3D()) {
    override fun equals(other: Any?) = if (other is Scanner) id == other.id else false
    override fun hashCode() = id.hashCode()
    private fun transform(transformation: Transformation) =
        Scanner(id, beacons.map(transformation::transform).toSet(), transformation.transform(position))

    private fun isOverlap(scanner: Scanner): Scanner? {
        this.beacons.forEach { beaconRef ->
            scanner.beacons.forEach {
                val translatedScanner = scanner.transform(Transformation(beaconRef - it))
                if (translatedScanner.beacons.intersect(this.beacons).size >= OVERLAP_BEACON_COUNT)
                    return translatedScanner
            }
        }
        return null
    }

    fun transformScannerIfOverlap(scanner: Scanner): Scanner? {
        repeat(4) { rotateX ->
            repeat(4) { rotateY ->
                repeat(4) { rotateZ ->
                    val transformation = Transformation(rotation = Point3D(rotateX, rotateY, rotateZ))
                    val s = scanner.transform(transformation)
                    this.isOverlap(s)?.let { return it }
                }
            }
        }
        return null
    }
}

fun List<Scanner>.toSynchronizedScanners(): List<Scanner> {
    val visited = LinkedList<Scanner>().also { it.add(this.first()) }
    val toVisit = this.drop(1).toMutableList()
    var visitedIndex = 0
    while (toVisit.isNotEmpty()) {
        val refScanner = visited[visitedIndex]
        toVisit.forEach { refScanner.transformScannerIfOverlap(it)?.let(visited::add) }
        toVisit.removeIf { it in visited }
        visitedIndex++
    }
    return visited
}

fun List<Scanner>.getBeacons() = this.fold(mutableSetOf<Point3D>()) { acc, a -> acc.also { it.addAll(a.beacons) } }

fun List<Scanner>.maxDistance(): Int {
    var dist = 0
    this.forEachIndexed { i, scanner ->
        dist = this.drop(i).maxOf { scanner.position.dist(it.position) }.takeIf { it > dist } ?: dist
    }
    return dist
}