package utils

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.shorten
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IntVectorExtensionsTest {
    @Test
    fun `test if a vector can be shortened`() {
        val testVector = IntVector(4, 2)
        val expectedVector = IntVector(2,1)
        assertEquals(expectedVector, testVector.shorten())
    }

    @Test
    fun `test if a vector can be shortened if it has multiple dividers`() {
        val testVector = IntVector(16, 24)
        val expectedVector = IntVector(2,3)
        assertEquals(expectedVector, testVector.shorten())
    }

    @Test
    fun `test non-dividable vector`(){
        val testVector = IntVector(2,3)
        assertEquals(testVector, testVector.shorten())
    }
}