package nl.joozd.days

import java.nio.file.Files
import java.nio.file.Paths

abstract class Day(private val numberOfDay: Int, isTest: Boolean = false) {
    protected val input =
        if(isTest) readTextFileFromResources("test_$numberOfDay.txt")
        else readTextFileFromResources("$numberOfDay.txt")

    open fun first(): Long = numberOfDay.toLong()*100 + 1

    open fun second(): Long = numberOfDay.toLong()*100 + 2

    override fun toString(): String = numberOfDay.toString().padStart(2, '0')

    /**
     * Get contents from a file
     */
    private fun readTextFileFromResources(fileName: String): String {
        val classLoader = Thread.currentThread().contextClassLoader
        val resource = classLoader.getResource(fileName)
            ?: throw IllegalArgumentException("File not found: $fileName")
        return Files.readString(Paths.get(resource.toURI())).lines().joinToString("\n") // get rid of "\r\n" or whatever
    }
}