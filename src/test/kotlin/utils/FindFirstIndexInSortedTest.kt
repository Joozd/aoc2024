package utils

import nl.joozd.utils.findFirstIndexInSorted
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FindFirstIndexInSortedTest {
    private val someArray = listOf(1,2,3,4,5,6,7,8,9,10,11).map { it.toLong() }.toLongArray()

    private val negativeArray = someArray.map{it * -1}.toLongArray().sortedArray()


    @Test
    fun `test if works for valid`(){
        (1L..9L).forEach{ t ->
            println("Looking for $t")
            assertEquals(t.toInt()-1, findFirstIndexInSorted(t, someArray))
        }
    }

    @Test
    fun `test if works for negative`(){
        negativeArray.indices.forEach { i ->
            println("Looking for ${negativeArray[i]}")
            assertEquals(i, findFirstIndexInSorted(negativeArray[i], negativeArray))
        }
    }

    @Test
    fun `test if works for invalid`(){
        assertNull(findFirstIndexInSorted(-1, someArray))
    }
}