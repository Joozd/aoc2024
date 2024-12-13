package nl.joozd.utils.linearalgebra

import kotlin.math.absoluteValue

/**
 * This counts all values that are inside min and max values in every dimension as "in" and the rest as "out"
 * Using vectors of different dimensions is undefined and will probably cause errors.
 */
class LongVectorRange(start: LongVector, endInclusive: LongVector) : ClosedRange<LongVector> {
    //This is done so smaller values always come before larger, so comparisons work.
    override val start: LongVector =
        LongVector(*LongArray(start.size) { minOf(start[it], endInclusive[it]) })
    override val endInclusive: LongVector =
        LongVector(*LongArray(start.size) { maxOf(start[it], endInclusive[it]) })

    override fun contains(value: LongVector): Boolean =
        value.size == start.size
                && value.indices.all{ value[it] in start[it].. endInclusive[it] }

    infix fun overlaps(other: LongVectorRange): Boolean =
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