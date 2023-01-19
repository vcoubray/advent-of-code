package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day20")
        .map { it.toLong() }

    println("Part 1 : ${CircularLinkedList(input).apply { mix() }.getCoordinate()}")
    println("Part 2 : ${CircularLinkedList(input, 811589153).apply { mix(10) }.getCoordinate()}")

}

class CircularLinkedList(values: List<Long>, private val key: Int = 1) {

    private val elements = values.map { Element(it * key) }

    init {
        for (i in 0 until values.size - 1) {
            elements[i].next = elements[i + 1]
            elements[i + 1].prev = elements[i]
        }
        elements.last().next = elements.first()
        elements.first().prev = elements.last()
    }

    fun mix(time: Int = 1) {
        repeat(time) {
            elements.forEach {
                val distance = (it.value % (elements.size - 1)).toInt()
                if (distance > 0) shift(it, distance)
                else if (distance < 0) shift(it, elements.size + distance - 1)
            }
        }
    }

    fun getCoordinate(): Long {
        var current = elements.first { it.value == 0L }
        return (0..2).map { current = getElementAt(current, 1000); current }.sumOf { it.value }
    }

    private fun shift(element: Element, distance: Int) {
        element.prev.next = element.next
        element.next.prev = element.prev

        val target = getElementAt(element, distance)

        element.next = target.next
        element.prev = target
        target.next.prev = element
        target.next = element
    }

    private fun getElementAt(start: Element, offset: Int): Element {
        var current = start
        repeat(offset) {
            current = current.next
        }
        return current
    }

    class Element(val value: Long) {
        lateinit var prev: Element
        lateinit var next: Element

    }

}
