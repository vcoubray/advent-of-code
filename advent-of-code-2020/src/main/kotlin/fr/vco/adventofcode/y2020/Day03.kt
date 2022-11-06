package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("/inputDay03.txt")
    val forest = input.readLines().map { it.map { c -> c == '#' } }

    val toboggan = TobogganArea(forest)

    println("Part 1 : ${toboggan.countTree(3, 1)}")

    val result2 = toboggan.countTree(1,1) *
            toboggan.countTree(3,1)*
            toboggan.countTree(5,1)*
            toboggan.countTree(7,1)*
            toboggan.countTree(1,2)

    println("Part 2 : $result2")
}


class TobogganArea(private val forest: List<List<Boolean>>) {

    private val height: Int = forest.size
    private val width: Int = forest.first().size

    fun isTree(x: Int, y: Int) = forest[y][x % width]

    fun countTree(vx: Int, vy: Int): Int {
        var x = 0
        var y = 0
        var countTree = 0
        while (y < height) {
            if (isTree(x, y)) countTree++
            x += vx
            y += vy
        }
        return countTree
    }
}