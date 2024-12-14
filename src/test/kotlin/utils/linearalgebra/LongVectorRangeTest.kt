package utils.linearalgebra

import nl.joozd.utils.linearalgebra.LongVector
import nl.joozd.utils.linearalgebra.LongVectorRange
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class LongVectorRangeTest {
    @Test
    fun `test if a LongVector isd in a range`(){
        val range = LongVectorRange(LongVector(0,0), LongVector(10,10))
        assertTrue(LongVector(2,2) in range)
    }

    @Test
    fun `test if a LongVector can be constructed backwards`(){
        val range = LongVectorRange(LongVector(10,10), LongVector(0,0))
        assertTrue(LongVector(2,2) in range)
    }
}