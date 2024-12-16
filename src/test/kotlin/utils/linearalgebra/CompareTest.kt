package utils.linearalgebra

import nl.joozd.utils.linearalgebra.IntVector
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals

class CompareTest {
    @Test
    fun `test comparing Intvectors in Pairs`(){
        val v1 = IntVector (3,7)
        val d1 = IntVector.NORTH
        val d2 = IntVector.EAST

        assertNotEquals(v1 to d1, v1 to d2)
    }
}