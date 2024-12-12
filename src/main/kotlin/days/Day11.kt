package nl.joozd.days

import java.util.*


class Day11(isTest: Boolean = false): Day(11, isTest) {
    /**
     * Part one we do the naive way, as I like LinkedLists
     */
    override fun first(): Long {
        val stones = makeInputList(input)
        repeat(25){
            blink(stones)
        }
        return stones.size.toLong()
//        val cache = HashMap<Pair<Long, Int>, Long>()
//        return makeInputList(input).sumOf {
//            countRecursive(it, 25, cache)
//        }
    }

    /**
     * Part 2 we do the fast way as it will not fit otherwise
     */
    override fun second(): Long {
        val cache = HashMap<Pair<Long, Int>, Long>()
        return makeInputList(input).sumOf {
            countRecursive(it, 75, cache)
        }
    }

    private fun makeInputList(input: String): LinkedList<Long> = LinkedList<Long>().apply{
        input.split(" ").forEach{
            add(it.toLong())
        }
    }

    fun blink(stones: LinkedList<Long>){
        with(stones.listIterator()){
            while(hasNext()){
                val stone = next()
                when{
                    testRule1(stone) -> set(1) // replace stone with 0 by 1
                    testRule2(stone) -> doRule2(stone)
                    else -> set(stone * 2024)
                }
            }
        }
    }

    /**
     * Rule 1: If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
     */
    private fun testRule1(stone: Long): Boolean =
        stone == 0L

    /**
     * Rule 2: If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
     * The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved
     * on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
     */
    private fun testRule2(stone: Long): Boolean =
        stone.toString().length %2 == 0

    private fun MutableListIterator<Long>.doRule2(stone: Long){
        val stoneString = stone.toString()
        val left = stoneString.take(stoneString.length/2)
        val right = stoneString.drop(stoneString.length/2)
        set(left.toLong())
        add(right.toLong())
    }

    private fun countRecursive(number: Long, stepsToGo: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long =
        cache[number to stepsToGo] ?: when {
        0 == stepsToGo -> 1L
        testRule1(number) -> countRecursive(1L, stepsToGo-1, cache)
        testRule2(number) -> {
            val s = number.toString()
            val left = s.take(s.length/2).toLong()
            val right = s.drop(s.length/2).toLong()
            countRecursive(left, stepsToGo-1, cache) +countRecursive(right, stepsToGo-1, cache)
        }
        else -> countRecursive(number * 2024, stepsToGo - 1, cache)
    }.also{
        cache[number to stepsToGo] = it
        }
}