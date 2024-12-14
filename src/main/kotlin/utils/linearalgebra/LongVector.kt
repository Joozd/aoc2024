package nl.joozd.utils.linearalgebra

/**
 * LongVectors are immutable.
 */
open class LongVector(vararg values: Long, private val comparator: LongVectorComparator = standardComparator): Iterable<Long>, Comparable<LongVector> {
    constructor(x: List<Long>): this(*x.toLongArray())

    val vector: LongArray = values

    val x get() = vector[0]

    val y get() = vector[1]

    val size get() = vector.size

    val indices get() = vector.indices

    fun inverse(): LongVector = LongVector(vector.map { it * -1})

    fun copy() = LongVector(*vector)

    operator fun get(i: Int): Long = vector[i]

    operator fun plus(other: LongVector): LongVector {
        //require(other.size == this.size) { "Can only add Vectors of same size" }
        return LongVector(*LongArray(vector.size) { vector[it] + other[it] })
    }

    operator fun minus(other: LongVector): LongVector {
        //require(other.size == this.size) { "Can only subtract Vectors of same size" }
        return LongVector(*LongArray(vector.size) { vector[it] - other[it] })
    }

    /**
     * Dot product. For cross product use (LongVector.cross(otherLongVector)
     */
    operator fun times(other: LongVector): Long {
        //require(other.size == this.size) { "Can only dot Vectors of same size" }
        return vector.indices.sumOf { vector[it] * other[it] }
    }

    /**
     * Matrix multiplication, done in [LongVectorMatrix]
     */
    operator fun times(matrix: LongVectorMatrix) = matrix * this

    /**
     * Multiply this vector by a scalar
     */
    //operator fun times(scalar: Long): LongVector = LongVector(this.map { it * scalar})

    /**
     * Multiply this vector by a scalar (faster)
     */
    operator fun times(scalar: Long): LongVector {
        val newVectorArray = LongArray(vector.size) { vector[it] * scalar }
        return LongVector(*newVectorArray)
    }

    operator fun rangeTo(other: LongVector)= LongVectorRange(this, other)

    /**
     * Get the cross product of two (3-dimensional) vectors.
     */
    infix fun cross(other: LongVector): LongVector {
        require(this.size == 3 && other.size == 3)
        val newVector = LongArray(3)
        newVector[0] = vector[1] * other[2] - vector[2] * other[1] // x = ay*bz - az*by
        newVector[1] = vector[2] * other[0] - vector[0] * other[2] // y = az*bx - ax*bz
        newVector[2] = vector[0] * other[1] - vector[1] * other[0] // z = ax*by - ay*bx
        return LongVector(*newVector)
    }

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     * Undefined for vectors in different spans.
     */
    override fun compareTo(other: LongVector): Int =
        if(other.size != this.size) 0 // returns 0 for different length vectors
        else comparator.compare(this, other)


    override fun equals(other: Any?) =
        if (other !is LongVector) false
        else other.vector.contentEquals(vector)

    override fun hashCode(): Int {
        return vector.contentHashCode()
    }

    override fun iterator(): Iterator<Long> = vector.iterator()

    override fun toString(): String = "[${vector.joinToString()}]"

    fun interface LongVectorComparator{
        /**
         * Vectors can be assumed to be the same length
         */
        fun compare(thisVector: LongVector, other: LongVector): Int
    }

    companion object{
        /**
         * Compares first element first, then second, etc., until it finds a difference.
         * so (1,0) > (0,10) and (0,10) > (0,1)
         */
        private val standardComparator = object: LongVectorComparator{
            override fun compare(thisVector: LongVector, other: LongVector): Int {
                thisVector.vector.indices.forEach{ i->
                    if (thisVector[i] != other[i])
                        return (thisVector[i] - other[i]).toInt()
                }
                return 0 // if no different values in Vector, they are the same.
            }
        }
    }
}