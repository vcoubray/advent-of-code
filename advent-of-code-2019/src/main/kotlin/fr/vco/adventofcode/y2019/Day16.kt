package fr.vco.adventofcode.y2019

import kotlin.math.abs

fun main() {

    val input = getInputReader("/inputDay16.txt")
    val inputList = input.readText().trim().map{it.toString().toInt()}


    println("Part 1 - Test 1 : ${fft("80871224585914546619083218645595".map{it.toString().toInt()},100).substring(0,8)}") //Should start with 24176176
    println("Part 1 - Test 2 : ${fft("19617804207202209144916044189917".map{it.toString().toInt()},100).substring(0,8)}") //Should start with 73745418.
    println("Part 1 - Test 3 : ${fft("69317163492948606335995924319873".map{it.toString().toInt()},100).substring(0,8)}") //Should start with 52432133
    println("Part 1 : ${fft(inputList,100).substring(0,8)}")


    val inputList2 = mutableListOf<Int>()
    repeat(10000) {
        inputList2.addAll(inputList)
    }
    val offset = inputList2.subList(0,7).joinToString("").toInt()
    println("Part 2 : ${fftAtIndex(inputList2,100,offset)}")


}


fun fft(originalList : List<Int>,phaseCount : Int):String{
    var list = originalList
    repeat(phaseCount){
        list = fft(list)
    }
    return list.joinToString("")
}

fun fftAtIndex(originalList : List<Int>,phaseCount : Int, index: Int): String {
    var list = originalList.subList(index,originalList.size)
    repeat(phaseCount){
        list = fftAtIndex(list)
    }
    return list.subList(0,8).joinToString("")
}

fun fftAtIndex(list : List<Int>): List<Int> {
    val result = MutableList(list.size){0}
    var sum =0
    for (i in list.size-1 downTo 0 ){
        sum += list[i]
        result[i] =sum %10
    }
    return result
}


fun fft (list : List<Int>) : List<Int>{

    val result = mutableListOf<Int>()
    repeat(list.size) { iteration ->
        val pattern = getPattern(iteration+1)
        var value = 0
        repeat(list.size) {
            value+= list[it] * pattern[(it + 1) % pattern.size]%10
        }
        result.add(abs(value))
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

fun getPattern(iteration: Int, position : Int) {
    (position+1) % (iteration+1)
}