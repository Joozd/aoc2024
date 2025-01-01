package nl.joozd.utils.linearalgebra

import kotlin.math.absoluteValue

fun allVectorsWithManhattanDistance(distance: Int): Sequence<IntVector> {
    return if (distance == 0) emptySequence() else
        sequence {
            for (y in (distance * -1)..distance) {
                val xDistance = distance - y.absoluteValue
                if (xDistance != 0)  // don't yield the top and bottom points twice
                    yield(IntVector(xDistance * -1, y))
                yield(IntVector(xDistance, y))
            }
        }
}