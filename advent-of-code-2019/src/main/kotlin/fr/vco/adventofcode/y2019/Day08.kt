package fr.vco.adventofcode.y2019

fun main() {

    val input = getInputReader("Day08")

    val image =
        Image(25, 6, input.readText().map { it.toString().toInt() })

    val layer = image.layers.minBy { it.count { it == 0 } }!!
    println("Part 1 : ${layer.count { it == 1 } * layer.count { it == 2 }}")

    println("Part 2 :")
    image.printImage()

}

class Image(
    val width: Int,
    val height: Int,
    val image: List<Int>
) {
    val layers = mutableListOf<List<Int>>()

    init {
        val layerSize = width * height
        val layerCount = image.size / layerSize
        repeat(layerCount) { i ->
            layers.add(image.subList(i * layerSize, (i + 1) * layerSize))
        }
    }

    fun printImage() {
        val finalImage = getFinalImage().map { convertToString(it) }

        repeat(height) { y ->
            repeat(width) { x ->
                print(finalImage[y * width + x])
            }
            println()
        }
    }

    private fun getFinalImage(): List<Int> {
        val finaleImage = mutableListOf<Int>()
        repeat(width * height) { i ->
            finaleImage.add(getPixelColor(i))
        }
        return finaleImage
    }


    private fun getPixelColor(index: Int): Int {
        var color = 2
        while (color == 2) {
            layers.forEach { layer ->
                if (layer[index] != 2) return layer[index]
            }
        }
        return color
    }

    fun convertToString(color: Int) = if (color == 1) "@@" else "  "
}
