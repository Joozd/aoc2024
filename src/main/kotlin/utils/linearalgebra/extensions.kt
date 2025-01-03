package nl.joozd.utils.linearalgebra

import kotlin.math.absoluteValue

/**
 * For performance, use a HashSet for [otherPositionVectors] if it is a large list.
 */
fun IntVector.hasNeighboursIn(otherPositionVectors: Collection<IntVector>): Boolean =
    potentialNeighbours().any { it in otherPositionVectors }

fun IntVector.hasNeighboursIn(otherPositionVectors: IntVectorRange): Boolean =
    potentialNeighbours().any { it in otherPositionVectors }

// Function to generate a list of potential neighbours
private fun IntVector.potentialNeighbours(): Sequence<IntVector> {
    return sequenceOf(
        IntVector.NW,
        IntVector.NORTH,
        IntVector.NE,

        IntVector.WEST,
        IntVector.EAST,

        IntVector.SW,
        IntVector.SOUTH,
        IntVector.SE
    ).map { this + it }
}

fun IntVector.fourPotentialNeighbours(): Sequence<IntVector> = sequenceOf(
    IntVector.NORTH,
    IntVector.EAST,
    IntVector.SOUTH,
    IntVector.WEST
).map { this + it }


/**
 * Both vectors need to be the same length
 */
fun IntVector.manhattanDistanceTo(other: IntVector): Int =
    indices.sumOf {
        (this[it] - other[it]).absoluteValue
    }

// Only works with a 2-dimensional vector
// Null if outside.
operator fun List<String>.get(gridLocation: IntVector): Char? =
    getOrNull(gridLocation[1])?.getOrNull(gridLocation[0])

operator fun <T> List<List<T>>.get(gridLocation: IntVector): T? =
    getOrNull(gridLocation[1])?.getOrNull(gridLocation[0])


/**
 * Divide x and y by largest common divider. Has room for optimization but don't think it will matter much
 * Will return the original IntVector if unable to shorten
 */
fun IntVector.shorten(): IntVector{
    val lowest = minOf(x, y)
    (lowest downTo 2).forEach{ candidate ->
        if(x%candidate == 0 && y%candidate == 0)
            return IntVector(x/candidate, y/candidate)
    }
    return this
}

/**
 * Get the scalar needed to go from [this] to [other]
 * @return the scalar found, or null if they are not scaled by an exact integer from [this]
 */
fun LongVector.getLongScalarTo(other: LongVector): Long?{
    if(other.x % x != 0L || other.y % y != 0L) return null // cannot scale by exact int
    val scaleX = other.x / x
    val scaleY = other.y / y
    return if (scaleX == scaleY) scaleX else null
}

fun IntVector.withAllManhattanDistanceNeighbours(distance: Int) =
    allVectorsWithManhattanDistance(distance).map { this + it}

