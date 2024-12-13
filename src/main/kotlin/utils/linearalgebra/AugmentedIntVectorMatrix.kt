package nl.joozd.utils.linearalgebra

/**
 * An [IntVectorMatrix] with added methods for doing Reduced Row Echelon form calculations
 * Thanks to https://en.wikipedia.org/wiki/Row_echelon_form / https://en.wikipedia.org/wiki/Hermite_normal_form
 */
class AugmentedLongVectorMatrix(vararg columnVectors: LongVector): LongVectorMatrix(*columnVectors) {
    init{
        require(matrix.size == REQUIRED_NUMBER_OF_COLUMNS) { "An augmented matrix needs to have 3 column vectors" }
    }
    // EXAMPLE
    // [ 2  1    8 ]
    // [ 1 -1    1 ]
    //
    // divide row 1 by 2
    // [ 1  0.5  4 ]
    // [ 1 -1    1 ]
    //
    // subtract row 1 from row 2
    // [ 1  0.5  4 ]
    // [ 0 -1.5 -3 ]
    //
    // divide row 2 by -1.5
    // [ 1  0.5  4 ]
    // [ 0  1    2 ]
    //
    // reduce row 1 by 0.5 * row 2
    // [ 1  0    3 ]
    // [ 0  1    2 ]


    fun findScalars(): Pair<Double, Double>?{
        // We do this with doubles, convert back to Int later if needed.
        val mm = Array(rowVectors.size) { rowIndex ->
            rowVectors[rowIndex].let { v ->
                DoubleArray(v.size) { columnIndex -> v[columnIndex].toDouble() }
            }
        }

        // make top left not zero
        if(mm[0][0] == 0.0 && mm[0][1] == 0.0) return null // Both columns have x=0 so they are colinear
        if(mm[0][0] == 0.0){
            val temp = mm[1]
            mm[1] = mm[0]
            mm[0] = temp
        }

        // make top left 1 by dividing top row
        val pivot1 = mm[0][0]
        mm[0] = DoubleArray(REQUIRED_NUMBER_OF_COLUMNS) { i -> mm[0][i] / pivot1} // make new top row

        // make bottom row start with 0 by removing top row [bottomLeft] times
        val bottomLeft = mm[1][0]
        mm[1] = DoubleArray(REQUIRED_NUMBER_OF_COLUMNS)  { i -> mm[1][i] - (mm[0][i] * bottomLeft)} // make new bottom row

        // check second pivot not 0
        val pivot2 = mm[1][1]
        if (0.0 == pivot2){
            if (mm[1][2] != 0.0) return null // no solutions
            return INFINITE_SOLUTIONS
        }

        // make bottom row [ 0 1 x ]
        mm[1] = DoubleArray(REQUIRED_NUMBER_OF_COLUMNS) { i -> mm[1][i] / pivot2 }

        // make top row [ 1 0 x ] by removing top bottom row [topMiddle] times
        val topMiddle = mm[0][1]
        mm[0] = DoubleArray(REQUIRED_NUMBER_OF_COLUMNS)  { i -> mm[0][i] - (mm[1][i] * topMiddle)} // make new bottom row

        // now we should have
        // [ 1 0 x ]
        // [ 0 1 y ]

        return mm[0][2] to mm[1][2]
    }

    companion object{
        val INFINITE_SOLUTIONS = Double.MAX_VALUE to Double.MAX_VALUE
        private const val REQUIRED_NUMBER_OF_COLUMNS = 3
    }
}
