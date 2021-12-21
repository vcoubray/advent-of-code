package fr.vco.aoc.y2021

fun main() {


}


data class Point3D(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(p: Point3D) = Point3D(x + p.x, y + p.y, z + p.z)
    operator fun minus(p: Point3D) = Point3D(x - p.x, y - p.y, z - p.z)
    operator fun unaryMinus() = Point3D(-x,-y,-z)

}

data class Transformation(val translation: Point3D = Point3D(), val rotate: Point3D = Point3D()) {
    fun translate(p : Point3D) = p + translation
    fun rotate(p: Point3D) = Point3D(

    )
}
