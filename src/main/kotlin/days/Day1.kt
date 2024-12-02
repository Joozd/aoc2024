package nl.joozd.days

import nl.joozd.utils.findFirstIndexInSorted
import kotlin.math.abs

class Day1(isTest: Boolean = false): Day(1, isTest) {
    private val lines = input.lines()

    //lazy for timing
    private val twoArrays by lazy { makeTwoSortedArrays() }
    private val left by lazy { twoArrays.first }
    private val right by lazy { twoArrays.second }


    override fun first(): Long {
        var result = 0L
        left.indices.forEach { i ->
            val diff = abs(left[i] - right[i])
            result += diff
        }
        return result
    }

    /*
     * For reference: This takes about 3 times as long to run as current implementation
     */
//    fun secondSlow(): Long{
//        var result = 0L
//        left.forEach { l ->
//            right.forEach { r ->
//                if (l == r) result += l
//            }
//        }
//        return result
//    }

    override fun second(): Long =
        // secondSlow()
        left.sumOf { v -> v * countValueIn(v, right) }


    private fun countValueIn(value: Long, array: LongArray): Long{
        var count = 0L
        var i = findFirstIndexInSorted(value, array)
            ?: return 0L

        while(array[i++] == value)
            count++

        return count
    }



    /**
     * Make two [LongArray]s from input and sort them
     */
    private fun makeTwoSortedArrays(): Pair<LongArray, LongArray>{
        val first = LongArray(lines.size) { -1 }
        val second = LongArray(lines.size) { -1 }

        lines.forEachIndexed{ i, line ->
            line.split(' ').let{
                first[i] = it.first().toLong()
                second[i] = it.last().toLong()
            }
        }
        first.sort()
        second.sort()

        return first to second
    }
}