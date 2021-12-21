package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day19")

    val scanners = input.split {it == "" }.map { scanner ->
        println("aaa" + scanner.first())
        val (id) = """(\d+)""".toRegex().find(scanner.first())!!.destructured
        Scanner(id.toInt(), scanner.drop(1).map { line ->
            line.split(",").map { it.toInt() }.let { (x, y, z) -> Point3D(x, y, z) }
        })
    }
    println(scanners)
}


data class Point3D(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(p: Point3D) = Point3D(x + p.x, y + p.y, z + p.z)
    operator fun minus(p: Point3D) = Point3D(x - p.x, y - p.y, z - p.z)
    operator fun unaryMinus() = Point3D(-x, -y, -z)
    fun rotateX() = copy(y = z, z = -y)
    fun rotateY() = copy(x = -z, z = x)
    fun rotateZ() = copy(x = y, y = -x)
}

data class Transformation(val translation: Point3D = Point3D(), val rotate: Point3D = Point3D()) {
    fun translate(p: Point3D) = p + translation
}

data class Scanner(val id: Int, val beacon: List<Point3D>, val transformation: Transformation = Transformation()) {
    override fun equals(other: Any?) = if (other is Scanner) id == other.id else false
    override fun hashCode() = id.hashCode()
}