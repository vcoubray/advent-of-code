package fr.vco.adventofcode.y2019

fun main() {

    val input = readText("Day19")
    val tractorBeam = TractorBeam(input)

    println("Part 1: ${tractorBeam.countPulledInArea(50, 50)}")
    println("Part 2: ${tractorBeam.findClosestSquare(99)}")
}

class TractorBeam(intCode: String) {
    private val intCode = intCode.toOpCode()

    private fun isPulled(x: Int, y: Int): Boolean {
        intCode.restart()
        intCode.stream.input.add(x.toLong())
        intCode.stream.input.add(y.toLong())
        intCode.exec()
        return intCode.stream.output.poll().toInt() == 1
    }

    fun countPulledInArea(width: Int, height: Int): Int {
        return List(width * height) { isPulled(it % width, it / width) }.count { it }
    }

    fun findClosestSquare(size: Int): Int {
        var startX = 0
        var y = 25

        var pulledLine = false
        while (true) {
            var x = startX
            while (!pulledLine || isPulled(x, y)) {
                if (isPulled(x, y)) {
                    if (!pulledLine) startX = x
                    pulledLine = true

                    if (isPulled(x + size, y) && isPulled(x, y + size) && isPulled(x + size, y + size)) {
                        return x * 10_000 + y
                    }
                }
                x++
            }
            y++
            pulledLine = false
        }
    }
}
