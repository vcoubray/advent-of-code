package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day24")
    val monad = Monad(input)

    println("Part 1 : ${monad.getMaxNumber()}")
    println("Part 2 : ${monad.getMinNumber()}")
}

class SubRoutine(instructions: List<String>) {
    val x = instructions[5].split(" ")[2].toInt()
    val y = instructions[15].split(" ")[2].toInt()
    val isIncreaseZ = x > 0

    fun getValidInputs( ) : Map<Int, Int> {
        return if( isIncreaseZ ) (1..9).associateBy {  it + y }
        else (0..25).filter { it + x in (1..9) }.associateWith { it + x }
    }
}

class Monad(instructions: List<String>) {

    private val validPatterns = instructions.chunked(18)
        .map(::SubRoutine)
        .getValidPatterns()

    fun getMinNumber() = validPatterns.joinToString("") {it.first().toString()}
    fun getMaxNumber() = validPatterns.joinToString("") {it.last().toString()}

    private fun List<SubRoutine>.getValidPatterns() : List<List<Int>> {
        val patterns = MutableList(14){ listOf<Int>() }
        val zStack = ArrayDeque<Pair<Int,Map<Int,Int>>>()
        this.forEachIndexed{ i, subRoutine ->
            if(subRoutine.isIncreaseZ) zStack.add(i to subRoutine.getValidInputs())
            else{
                val (stackedIndex, stackedInputs) = zStack.removeLast()
                val inputs = subRoutine.getValidInputs()
                val commonZInputs = stackedInputs.keys.intersect(inputs.keys)
                patterns[stackedIndex] = commonZInputs.mapNotNull{stackedInputs[it]}
                patterns[i] = commonZInputs.mapNotNull{inputs[it]}
            }
        }
        return patterns
    }
}