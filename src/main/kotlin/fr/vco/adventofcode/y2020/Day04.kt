package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay04.txt")
    val passports = mutableListOf(Passport())
    input.readLines().forEach {
        if (it.isEmpty()) passports.add(Passport())
        else it.split(" ").forEach { f -> f.split(":").let { (k, v) -> passports.last().fields[k] = v } }
    }

    println("Part 1 : ${passports.count { it.isValid() }}")
    println("Part 2 : ${passports.count { it.isValidPart2() }}")
}

class Passport {
    companion object {
        val RULES = mapOf(
            "byr" to ::byrIsValid,
            "iyr" to ::iyrIsValid,
            "eyr" to ::eyrIsValid,
            "hgt" to ::hgtIsValid,
            "hcl" to ::hclIsValid,
            "ecl" to ::eclIsValid,
            "pid" to ::pidIsValid
        )
    }

    val fields = mutableMapOf<String, String>()

    fun isValid() = fields.keys.containsAll(RULES.keys)
    fun isValidPart2() = isValid() && fields.all { (RULES[it.key] ?: (::ignored))(it.value) }
}

fun byrIsValid(byr: String) = byr.toInt() in 1920..2002
fun iyrIsValid(iyr: String) = iyr.toInt() in 2010..2020
fun eyrIsValid(eyr: String) = eyr.toInt() in 2020..2030
fun hgtIsValid(hgt: String) = "(\\d+)(cm|in)".toRegex().find(hgt)?.groupValues?.let { (_, h, unit) -> if (unit == "in") h.toInt() in 59..76 else h.toInt() in 150..193 } ?: false
fun hclIsValid(hcl: String) = hcl.matches("#([a-f]|\\d){6}".toRegex())
fun eclIsValid(ecl: String) = ecl.matches("amb|blu|brn|gry|grn|hzl|oth".toRegex())
fun pidIsValid(pid: String) = pid.matches("\\d{9}".toRegex())
fun ignored(f: String) = true
