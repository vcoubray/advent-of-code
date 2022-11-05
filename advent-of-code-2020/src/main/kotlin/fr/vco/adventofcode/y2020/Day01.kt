package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("/2020/inputDay01.txt")
    val expenses = input.readLines().map { it.toInt() }

    println("Part 1 : ${find(expenses,2020,2)}")
    println("Part 2 : ${find(expenses,2020,3)}")

}

fun find (expenses : List<Int>, total :Int , count: Int) :Int{
    if (count == 1 && expenses.contains(total)) return total
    if (count >= 1) expenses.forEachIndexed{ i, it ->
        val search = total - it
        if(search >0) {
            val result = find(expenses.subList(i, expenses.size),search, count-1)
            if( result > 0 ) return result * it
        }
    }
    return 0
}