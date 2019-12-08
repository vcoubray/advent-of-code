package fr.vco.adventofcode

fun main() {

    val input = getInputReader("/inputAoC8.txt")
    val image = input.readText().map { it.toString().toInt() }

    val width = 25
    val height = 6
    val layerSize = width * height


    val layerCount = image.size / layerSize
    val layers = mutableListOf<List<Int>>()
    repeat(layerCount) { i ->
        layers.add(image.subList(i * layerSize, (i + 1) * layerSize))

    }
    val layer = layers.minBy { it.count { it == 0 } }!!
    println("Part 1 : ${layer.count { it == 1 } * layer.count { it == 2 }}")


}
