package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day24")
    val monad = Monad(input)
    val max = 19_999_999_999_999L
    val min = 11_111_111_111_111L


    for (i in max downTo min) {
        println(i)
        if( monad.validNumber(i)){
            println("valid : $i")
            break
        }
    }

}

class Monad(private val instructions: List<String>) {
     val variables = mutableMapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)

    val input = ArrayDeque<Int>()

    private fun exec(instruction: String) {
        val parameters = instruction.split(" ")
        val ins = parameters[0]
        when (ins) {
            "inp" -> variables[parameters[1]] = input.removeFirst()
            "add" -> variables[parameters[1]] =
                (variables[parameters[1]] ?: 0) + (parameters[2].toIntOrNull() ?: variables[parameters[2]]!!)
            "mul" -> variables[parameters[1]] =
                (variables[parameters[1]] ?: 0) * (parameters[2].toIntOrNull() ?: variables[parameters[2]]!!)
            "div" -> variables[parameters[1]] =
                (variables[parameters[1]] ?: 0) / (parameters[2].toIntOrNull() ?: variables[parameters[2]]!!)
            "mod" -> variables[parameters[1]] =
                (variables[parameters[1]] ?: 0) % (parameters[2].toIntOrNull() ?: variables[parameters[2]]!!)
            "eql" -> variables[parameters[1]] = if ((variables[parameters[1]] ?: 0) == (parameters[2].toIntOrNull()
                    ?: variables[parameters[2]]!!)
            ) 1 else 0
        }
    }

    fun validNumber(number: Long): Boolean {
        reset()
        number.toString().forEach { input.add(it.digitToInt()) }
        instructions.forEach(::exec)
        return variables["z"] == 0
    }

    fun reset() {
        input.clear()
        variables.keys.forEach { variables[it] = 0 }
    }

}