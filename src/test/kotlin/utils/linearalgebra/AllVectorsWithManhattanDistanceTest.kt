package utils.linearalgebra

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.allVectorsWithManhattanDistance
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AllVectorsWithManhattanDistanceTest {
    @Test
    fun `zero distance must be empty`(){
        assert(allVectorsWithManhattanDistance(0).toList().isEmpty()) { "zero distance must be empty" }
    }

    @Test
    fun `1 distance gives expected result`(){
        val expected = setOf(IntVector.NORTH, IntVector.EAST, IntVector.WEST, IntVector.SOUTH)
        assertEquals(expected, allVectorsWithManhattanDistance(1).toSet())
    }

    @Test
    fun `2 distance gives expected result`(){
        val expected = listOf(IntVector.NORTH, IntVector.EAST, IntVector.WEST, IntVector.SOUTH).map { it * 2}.toSet() +
                setOf(IntVector.NW, IntVector.NE, IntVector.SW, IntVector.SE)
        assertEquals(expected, allVectorsWithManhattanDistance(2).toSet())
    }
}