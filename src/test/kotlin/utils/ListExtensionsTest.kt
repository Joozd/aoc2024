package utils

import nl.joozd.utils.extensions.combinations
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ListExtensionsTest {
    @Test
    fun testMakeCombinations(){
        val l = listOf(1,2,3)
        val expected = listOf(1 to 2, 1 to 3, 2 to 3)
        assertEquals(expected, l.combinations())
    }
}