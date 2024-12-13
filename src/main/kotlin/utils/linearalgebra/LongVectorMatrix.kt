package nl.joozd.utils.linearalgebra

open class LongVectorMatrix(vararg columnVectors: LongVector) {
    init{
        require(
            columnVectors.isEmpty() ||
                    columnVectors.all{
                        it.size == columnVectors[0].size
                    }
        ) { "Vectors in matrix must be the same length"}
    }
    protected open val matrix: Array<LongVector> = Array(columnVectors.size) { columnVectors[it] } // to support children of LongVector

    protected val length get() = matrix.getOrNull(0)?.size ?: 0

    protected val rowVectors: Array<LongVector> by lazy {
        if (matrix.isEmpty()) emptyArray()
        else {
            matrix.first().indices.map { i ->
                LongVector(matrix.map { it[i] })
            }.toTypedArray()
        }
    }


    /**
     * Multiply this matrix with an LongVector
     */
    operator fun times(vector: LongVector): LongVector {
        require(vector.size == matrix.size) { "Can't multiply vector with length${vector.size} with matrix with ${matrix.size} vectors; must be equal."}
        if(length == 0) return LongVector() // multiplying an empty vector with an empty matrix, or a matrix with empty vectors, gives another empty vector
        return LongVector(rowVectors.map { it * vector })
    }

    companion object{
        // Getting the rowVectors from the rowVectors of a matrix rotates them straight back to column vectors
        fun ofRowVectors(vararg rowVectors: LongVector): LongVectorMatrix =
            LongVectorMatrix(*LongVectorMatrix(*rowVectors).rowVectors)
    }
}