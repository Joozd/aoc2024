package utils.linearalgebra

import nl.joozd.utils.linearalgebra.AugmentedLongVectorMatrix
import nl.joozd.utils.linearalgebra.LongVector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AugmentedLongVectorMatrixTest {

    @Test
    fun `findScalars returns null for no solution`() {
        // Arrange: Set up an augmented matrix with no solution
        val column1 = LongVector(0, 1)
        val column2 = LongVector(0, 1)
        val column3 = LongVector(1, 2)
        val matrix = AugmentedLongVectorMatrix(column1, column2, column3)

        // Act
        val result = matrix.findScalars()

        // Assert
        assertNull(result, "Expected no solution but got a result")
    }

    @Test
    fun `findScalars returns infinite solutions`() {
        // Arrange: Set up an augmented matrix with infinite solutions
        val column1 = LongVector(1, 0)
        val column2 = LongVector(2, 0)
        val column3 = LongVector(3, 0)
        val matrix = AugmentedLongVectorMatrix(column1, column2, column3)

        // Act
        val result = matrix.findScalars()

        // Assert
        assertEquals(AugmentedLongVectorMatrix.INFINITE_SOLUTIONS, result, "Expected infinite solutions")
    }

    @Test
    fun `findScalars returns unique solution`() {
        // Arrange: Set up an augmented matrix with a unique solution
        val column1 = LongVector(2, 1)
        val column2 = LongVector(1, -1)
        val column3 = LongVector(8, 1)
        val matrix = AugmentedLongVectorMatrix(column1, column2, column3)

        // Act
        val result = matrix.findScalars()

        // Assert
        assertNotNull(result, "Expected unique solution but got null")
        assertEquals(3.0 to 2.0, result, "Expected solution (1, -1) but got $result")
    }

    @Test
    fun `findScalars handles zero pivot`() {
        // Arrange: Set up an augmented matrix where the first pivot is zero
        val column1 = LongVector(0, 1)
        val column2 = LongVector(1, 1)
        val column3 = LongVector(2, 3)
        val matrix = AugmentedLongVectorMatrix(column1, column2, column3)

        // Act
        val result = matrix.findScalars()

        // Assert
        assertNotNull(result, "Expected unique solution but got null")
        assertEquals(1.0 to 2.0, result, "Expected solution (3, -1) but got $result")
    }
}