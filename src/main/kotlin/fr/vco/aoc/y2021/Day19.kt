package fr.vco.aoc.y2021

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

    val scanner = scanners.first()
    val transform = Transformation(Point3D(800, -95, 567))
    val scanner2 = scanner.transform(transform)


    scanner.isOverlap(scanner2)
}


data class Point3D(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(p: Point3D) = Point3D(x + p.x, y + p.y, z + p.z)
    operator fun minus(p: Point3D) = Point3D(x - p.x, y - p.y, z - p.z)
    operator fun unaryMinus() = Point3D(-x, -y, -z)
    fun rotateX() = copy(y = z, z = -y)
    fun rotateY() = copy(x = -z, z = x)
    fun rotateZ() = copy(x = y, y = -x)

    fun rotateX(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateX() }
    fun rotateY(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateY() }
    fun rotateZ(times: Int) = (0 until times).fold(this) { acc, _ -> acc.rotateZ() }
}

data class Transformation(val translation: Point3D = Point3D(), val rotate: Point3D = Point3D()) {
    fun translate(p: Point3D) = p + translation
    fun rotate(p: Point3D) = p.rotateX(rotate.x).rotateY(rotate.y).rotateZ(rotate.z)
    fun transform(p: Point3D) = rotate(p).apply(::println) + translation

}

data class Scanner(val id: Int, val beacons: Set<Point3D>, val transformation: Transformation = Transformation()) {
    override fun equals(other: Any?) = if (other is Scanner) id == other.id else false
    override fun hashCode() = id.hashCode()
    fun transform(transformation: Transformation) =
        Scanner(id, beacons.map(transformation::transform).toSet(), transformation)

    fun isOverlap(scanner: Scanner): Boolean {
        this.beacons.forEach { beaconRef ->
            scanner.beacons.forEach {
                val translate = beaconRef - it
                println(translate)
                println(scanner.transform(Transformation(translate)).beacons.intersect(this.beacons).size)
            }
        }
        return false
    }


}