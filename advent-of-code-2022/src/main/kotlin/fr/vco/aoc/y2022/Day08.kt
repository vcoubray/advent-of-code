package fr.vco.aoc.y2022


fun main() {
    val input = readLines("Day08")

    val forest = Forest(input)
    println("Part 1 : ${forest.countVisibleTrees()}")
    println("Part 2 : ${forest.findBestScenicScore()}")
}

class Forest(input: List<String>) {
    private val horizontalTrees: List<List<Int>> = input.map { it.map { tree -> tree.digitToInt() } }
    private val verticalTrees = horizontalTrees.transposed()

    private val height = horizontalTrees.size
    private val width = horizontalTrees.first().size

    fun findBestScenicScore(): Int {
        return horizontalTrees.indices.maxOf { h ->
            horizontalTrees.first().indices.maxOf { w ->
                getScenicPoints(h, w)
            }
        }
    }


    private fun getScenicPoints(h: Int, w: Int): Int {
        val currentTree = horizontalTrees[h][w]
        return listOf(
            horizontalTrees[h].drop(w + 1),
            horizontalTrees[h].dropLast(width - w).reversed(),
            verticalTrees[w].drop(h + 1),
            verticalTrees[w].dropLast(height - h).reversed()
        )
            .map { countVisibleTrees(it, currentTree) }
            .reduce { acc, a -> acc * a }
    }

    private fun countVisibleTrees(trees: List<Int>, maxHeight: Int): Int {
        var count = 0
        for (tree in trees) {
            count++
            if (tree >= maxHeight) break
        }
        return count
    }

    fun countVisibleTrees(): Int {
        val visibleTrees = List(height) { MutableList(width) { false } }

        repeat(height) { h ->
            var maxHeight = -1
            var maxHeightLast = -1
            repeat(width) { w ->
                if (horizontalTrees[h][w] > maxHeight) {
                    maxHeight = horizontalTrees[h][w]
                    visibleTrees[h][w] = true
                }
                if (horizontalTrees[h][width - w - 1] > maxHeightLast) {
                    maxHeightLast = horizontalTrees[h][width - w - 1]
                    visibleTrees[h][width - w - 1] = true
                }
            }
        }

        repeat(width) { w ->
            var maxHeight = -1
            var maxHeightLast = -1
            repeat(height) { h ->
                if (horizontalTrees[h][w] > maxHeight) {
                    maxHeight = horizontalTrees[h][w]
                    visibleTrees[h][w] = true
                }
                if (horizontalTrees[height - h - 1][w] > maxHeightLast) {
                    maxHeightLast = horizontalTrees[height - h - 1][w]
                    visibleTrees[height - h - 1][w] = true
                }
            }
        }
        return visibleTrees.flatten().count { it }
    }

}


