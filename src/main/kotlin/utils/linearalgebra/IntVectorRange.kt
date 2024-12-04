package nl.joozd.aoc2023.common.linearalgebra

import kotlin.math.absoluteValue

/**
 * This counts all values that are inside min and max values in every dimension as "in" and the rest as "out"
 * Using vectors of different dimensions is undefined and will probably cause errors.
 */
class IntVectorRange(start: IntVector, endInclusive: IntVector) : ClosedRange<IntVector> {
    //This is done so smallest values always come before larger, so comparisons work.
    override val start: IntVector =
        IntVector(*IntArray(start.size) { minOf(start[it], endInclusive[it]) })
    override val endInclusive: IntVector =
        IntVector(*IntArray(start.size) { maxOf(start[it], endInclusive[it]) })

    override fun contains(value: IntVector): Boolean =
        value.size == start.size
                && value.indices.all{ value[it] in start[it].. endInclusive[it] }

    infix fun overlaps(other: IntVectorRange): Boolean =
        start.indices.all{
            start[it] <= other.endInclusive[it] && endInclusive[it] >= other.start[it]
        }

    override fun toString() = "IntVector($start)..($endInclusive)"


    fun measure(): Long{
        if(start.size == 0) return 0
        var result = 1L
        start.indices.forEach { i ->
            val l = (start[i]-endInclusive[i]).absoluteValue
            result *= l
        }
        return result
    }
}