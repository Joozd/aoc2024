package nl.joozd.aoc2023.common.linearalgebra

object Matrices {
    // For use in Ascii-maps where y down is increasing y
    val TURN_LEFT_ASCII_MAP = IntVectorMatrix.ofRowVectors(
        IntVector(0, 1),
        IntVector(-1,0)
    )

    // For use in Ascii-maps where y down is increasing y
    val TURN_RIGHT_ASCII_MAP = IntVectorMatrix.ofRowVectors(
        IntVector(0, -1),
        IntVector(1,0)
    )
}