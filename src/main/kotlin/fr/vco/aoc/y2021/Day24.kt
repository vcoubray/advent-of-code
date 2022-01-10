package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day24")
    val monad = Monad(input)

    println("Part 1 : ${monad.getMaxValidPattern()}")

}

data class MonadInstruction(val ins :String, val param1:String, val param2: String?)

class SubMonad(instructions: List<String>) {
    var variables = mutableMapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)

    private val instructions = instructions.map{it.split(" ")}.map{MonadInstruction(it[0],it[1],it.getOrNull(2)) }

    fun exec(input: Int) {
        instructions.forEach {
            when (it.ins) {
                "inp" -> variables[it.param1] = input
                "add" -> variables[it.param1] =
                    (variables[it.param1] ?: 0) + (it.param2!!.toIntOrNull() ?: variables[it.param2]!!)
                "mul" -> variables[it.param1] =
                    (variables[it.param1] ?: 0) * (it.param2!!.toIntOrNull() ?: variables[it.param2]!!)
                "div" -> variables[it.param1] =
                    (variables[it.param1] ?: 0) / (it.param2!!.toIntOrNull() ?: variables[it.param2]!!)
                "mod" -> variables[it.param1] =
                    (variables[it.param1] ?: 0) % (it.param2!!.toIntOrNull() ?: variables[it.param2]!!)
                "eql" -> variables[it.param1] = if ((variables[it.param1] ?: 0) == (it.param2!!.toIntOrNull()
                        ?: variables[it.param2]!!)
                ) 1 else 0
            }
        }
    }

}

class Monad(instructions: List<String>) {

    private val subMonads = instructions.chunked { it.startsWith("inp") }.filterNot { it.isEmpty() }.map(::SubMonad)

    fun validNumber(number: Long): Boolean {

        var variables = mapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)
        val input = number.toString().map { it.digitToInt() }
        subMonads.forEachIndexed { i, subMonad ->
            subMonad.variables = variables.toMutableMap()
            subMonad.exec(input[i])
            variables = subMonad.variables
        }
        return variables["z"] == 0
    }

    fun getMaxValidPattern(
        subMonadId: Int = 0,
        variables: Map<String, Int> = mapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)
    ): String? {
        if (subMonadId == subMonads.size) {
            return "".takeIf { variables["z"] == 0 }
        }

        for (i in 9 downTo 1) {
            subMonads[subMonadId].variables = variables.toMutableMap()
            subMonads[subMonadId].exec(i)
            getMaxValidPattern(subMonadId + 1, subMonads[subMonadId].variables)?.let { return "$i$it" }
        }
        return null
    }

}