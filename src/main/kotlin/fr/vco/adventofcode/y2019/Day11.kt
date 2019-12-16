package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import java.lang.StringBuilder


const val BLACK = 0
const val WHITE = 1

const val UP = 0
const val RIGHT = 1
const val DOWN = 2
const val LEFT = 3

fun main() {

    val input = getInputReader("/2019/inputDay11.txt")
    val intCode = input.readText().trim().split(",").map { it.toLong() }

    val panels = Panels(BLACK)

    paint(intCode, panels)
    println("Part 1 : ${panels.size()-1}")

    val panelsPart2 = Panels(WHITE)
    paint(intCode, panelsPart2)
    println("Part 2 : ")
    println(panelsPart2)


}

fun paint(intCode: List<Long>,panels:Panels) {
    val stream = OpCodeStream()
    val opCode = OpCode(intCode, stream)
    val bot = Robot(opCode, panels)
    while (!bot.hasFinish()){
        bot.paint()
        bot.move()
    }
}


class Robot(
    val opCode: OpCode,
    val panels : Panels,
    var direction: Int = LEFT

) {
    private var panel = panels.get(0,0)

    fun hasFinish() = opCode.isEnded()

    fun paint() {
        opCode.stream.input.add(panel.color.toLong())
        opCode.exec()
        if(! hasFinish())
            panel.color = opCode.stream.output.poll()!!.toInt()
    }

    fun move() {
        opCode.exec()
        if(! hasFinish()) {
            direction = getDirection(opCode.stream.output.poll()!!.toInt())
        }
        panel = nextPanel()
    }

    private fun getDirection(rotation : Int) =
        when (val dir = (direction + if (rotation == 1 ) 1 else -1)) {
            4 -> 0
            -1 -> 3
            else -> dir
        }

    private fun nextPanel() =
        when(direction) {
            UP -> panels.get(panel.x, panel.y-1)
            DOWN -> panels.get(panel.x, panel.y+1)
            LEFT -> panels.get(panel.x-1, panel.y)
            RIGHT -> panels.get(panel.x+1, panel.y)
            else -> panel // Should never happen
        }
}

data class Panel(
    val x: Int,
    val y: Int,
    var color: Int = BLACK
)

data class Position (
    val x : Int,
    val y: Int
)

class Panels(firstPanelColor:Int ) {

    private val panels = mutableMapOf(Position(0,0) to Panel(0, 0,firstPanelColor))

    fun get(x : Int, y : Int) = get(Position(x,y))

    fun get(pos : Position) :Panel{
        if(!panels.containsKey(pos))
            panels[pos] = Panel(pos.x,pos.y)
        return panels[pos]!!
    }

    fun size() = panels.size


    override fun toString() :String {
        val sb = StringBuilder()
        val map = panels.values.groupBy{it.y}
        map.toSortedMap().values.forEach{
            val line = it.sortedBy{panel -> panel.x}.joinToString(""){p -> if (p.color== 1) "##" else "  "}
            sb.append(line).append("\n")
        }
        return sb.toString()
    }
}