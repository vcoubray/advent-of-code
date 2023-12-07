package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("Day02")
    val lines = input.readLines()
    val passwords = lines.map{
        val (range, letter, password) = it.split(" ")
        val (min, max) = range.split("-").map{r -> r.toInt()}
        Password(min, max, letter.first(), password)
    }

    println("Part 1 : ${passwords.count{it.isValid()}}")
    println("Part 2 : ${passwords.count{it.isValidPart2()}}")

}

data class Password(val min :Int, val max: Int,val letter :Char, val password :String ) {

    fun isValid() = password.count{it == letter} in min .. max
    fun isValidPart2() = (password[min-1] == letter) xor (password[max-1] == letter)
}