package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import kotlin.math.abs

fun main() {

    val input = getInputReader("/2019/inputDay16.txt")

    println("Part 1 - Test 1 : ${fft("80871224585914546619083218645595".map{it.toString().toInt()},100)}") //Should start with 24176176
    println("Part 1 - Test 2 : ${fft("19617804207202209144916044189917".map{it.toString().toInt()},100)}") //Should start with 73745418.
    println("Part 1 - Test 3 : ${fft("69317163492948606335995924319873".map{it.toString().toInt()},100)}") //Should start with 52432133
    println("Part 1 : ${fft(input.readText().trim().map{it.toString().toInt()},100)}")

}


fun fft(originalList : List<Int>,phaseCount : Int):String{
    var list = originalList
    repeat(phaseCount){
        list = fftForPhase(list)
    }
    return list.joinToString("")
}

fun fftForPhase (list : List<Int>) : List<Int>{

    val result = mutableListOf<Int>()
    repeat(list.size) { iteration ->
        val pattern = getPattern(iteration+1)
        var value = 0
        repeat(list.size) {
            value+= list[it] * pattern[(it + 1) % pattern.size]
        }
        result.add(abs(value)%10)
    }
    return result
}

fun getPattern(phase : Int) :List<Int>{
    val pattern = listOf(0,1,0,-1)
    val phasePattern = mutableListOf<Int>()
    pattern.forEach {p ->
        phasePattern.addAll(List(phase){p})
    }
    return phasePattern
}