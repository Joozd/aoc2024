package utils

import nl.joozd.utils.getNumbersFromLine
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NumbersFromLineTest {
    @Test
    fun `test if a line parses`(){
        val line = "p=3,0 v=-2,2"
        val expected = listOf<Long>(3,0,-2,2)
        assertEquals(expected, getNumbersFromLine(line).toList(), "Line was $line")
    }

    @Test
    fun `test if no numbers give an empty list`(){
        val line = "Joozd is gaaf!"
        val expected = listOf<Long>()
        assertEquals(expected, getNumbersFromLine(line).toList(), "Line was $line")
    }

    @Test
    fun `test if numbers with a decimal place become two Longs`(){
        val line = "This is a number with a Decimal place: 123.456. Isn't it something?"
        val expected = listOf<Long>(123, 456)
        assertEquals(expected, getNumbersFromLine(line).toList(), "Line was $line")
    }
}