package nl.joozd.aoc2023.common.linearalgebra

/**
 * IntVectors are immutable.
 */
open class IntVector(vararg values: Int): Iterable<Int>, Comparable<IntVector> {
    constructor(x: List<Int>): this(*x.toIntArray())

    protected val vector: IntArray = values

    val x get() = this[0]

    val y get() = this[1]

    val size get() = vector.size

    val indices get() = vector.indices

    fun inverse(): IntVector = IntVector(vector.map { it * -1})

    fun copy() = IntVector(*vector)

    operator fun get(i: Int): Int = vector[i]

    operator fun plus(other: IntVector): IntVector {
        require(other.size == this.size) { "Can only add Vectors of same size" }
        return IntVector(*IntArray(size) { vector[it] + other[it] })
    }

    operator fun minus(other: IntVector): IntVector {
        require(other.size == this.size) { "Can only subtract Vectors of same size" }
        return IntVector(*IntArray(size) { vector[it] - other[it] })
    }

    /**
     * Dot product. For cross product use (IntVector.cross(otherIntVector)
     */
    operator fun times(other: IntVector): Int {
        require(other.size == this.size) { "Can only dot Vectors of same size" }
        return vector.indices.sumOf { vector[it] * other[it] }
    }

    /**
     * Matrix multiplication, done in [IntVectorMatrix]
     */
    operator fun times(matrix: IntVectorMatrix) = matrix * this

    /**
     * Multiply this vector by a scalar
     */
    operator fun times(scalar: Int): IntVector = IntVector(this.map { it * scalar})

    operator fun rangeTo(other: IntVector)= IntVectorRange(this, other)

    /**
     * Get the cross product of two (3-dimensional) vectors.
     */
    infix fun cross(other: IntVector): IntVector {
        require(this.size == 3 && other.size == 3)
        val newVector = IntArray(3)
        newVector[0] = vector[1] * other[2] - vector[2] * other[1] // x = ay*bz - az*by
        newVector[1] = vector[2] * other[0] - vector[0] * other[2] // y = az*bx - ax*bz
        newVector[2] = vector[0] * other[1] - vector[1] * other[0] // z = ax*by - ay*bx
        return IntVector(*newVector)
    }

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     * Undefined for vectors in different spans.
     * Compares first element first, then second, etc, until it finds a difference.
     * so (1,0) > (0,10) and (0,10) > (0,1)
     */
    override fun compareTo(other: IntVector): Int {
        if(other.size != this.size) return 0 // returns 0 for different length vectors
        vector.indices.forEach{ i->
            if (this[i] != other[i])
                return this[i] - other[i]
        }
        return 0 // if no different values in Vector, they are the same.
    }

    override fun equals(other: Any?) =
        if (other !is IntVector) false
        else other.vector.contentEquals(vector)

    override fun hashCode(): Int {
        return vector.contentHashCode()
    }

    override fun iterator(): Iterator<Int> = vector.iterator()

    override fun toString(): String = "[${vector.joinToString()}]"

    // Helper function to get the value in a list of Strings. Used a lot in ASCII maps.
    fun itemInMap(stringsList: List<String>): Char? =
        stringsList.getOrNull(vector[1])?.getOrNull(vector[0])

    companion object{
        // helpers for 2d grids
        val NORTH = IntVector(0,-1)
        val EAST  = IntVector(1,0)
        val SOUTH = IntVector(0,1)
        val WEST  = IntVector(-1,0)

        val NW = NORTH + WEST
        val NE = NORTH + EAST
        val SE = SOUTH + EAST
        val SW = SOUTH + WEST
    }
}