package fr.vco.adventofcode.y2020


fun main(){
    val input = getInputReader("Day25")
    val lines = input.readLines()
    val (cardKey, doorKey ) = lines.map{Key(it.toInt())}

    println("Part 1 : ${cardKey.encrypt(doorKey)}")

}

class Key(private val publicKey: Int) {

    private companion object{
        const val BASE_SUBJECT_NUMBER = 7
        const val DIVIDER = 20201227
    }

    private val loop : Int = findLoop()

    private fun findLoop(subjectNumber : Int = BASE_SUBJECT_NUMBER ) : Int {
        var value = 1
        var loop = 0
        while (value != publicKey){
            value *= subjectNumber
            value %= DIVIDER
            loop++
        }
        return loop
    }

    fun encrypt( key : Key ) : Long{
        var encryptionKey = 1L
        repeat(loop){
            encryptionKey *= key.publicKey
            encryptionKey %= DIVIDER
        }
        return encryptionKey
    }

}