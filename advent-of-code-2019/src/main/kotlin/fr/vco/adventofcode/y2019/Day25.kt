package fr.vco.adventofcode.y2019

fun main(){
    val input = readLines("Day25").first()
    val game = RpgGame(input.toOpCode())

    val commands = listOf(
        "north",
        "east",
        "take astrolabe",
        "south",
        "take space law space brochure",
        "north",
        "west",
        "north",
        "north",
        "north",
        "north",
        "take weather machine",
        "north",
        "take antenna",
        "west",
        "south"
    )
    game.play(commands)
}



class RpgGame(val opCode: OpCode) {

    fun play(commands : List<String> = emptyList()) {
        opCode.restart()
        opCode.exec()
        commands.forEach{
            println(output())
            playCommand(it)
        }

        while(!opCode.isEnded()) {
            println(output())
            playCommand(readln())
        }
        println(output())
    }


    val ANSI_GREEN = "\u001B[32m"
    val ANSI_RESET = "\u001B[0m"
    private fun playCommand(command: String) {
        val input = "${command}\n"
        println("${ANSI_GREEN}> $command${ANSI_RESET}")
        opCode.stream.input.addAll(input.map{it.code.toLong()})
        opCode.exec()
    }

    private fun output() : String {
        val output = opCode.stream.output
            .toList()
            .map{it.toInt().toChar()}
            .joinToString("")
        opCode.stream.output.clear()
        return output
    }

}