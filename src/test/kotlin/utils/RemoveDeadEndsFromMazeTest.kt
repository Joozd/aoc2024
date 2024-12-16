package utils

import nl.joozd.utils.removeDeadEndsFromMaze
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveDeadEndsFromMazeTest {
    @Test
    fun `test if maze gets simplified`(){
        val simplerMaze1 = removeDeadEndsFromMaze(maze1.lines())
        assertEquals(exp1.lines(), simplerMaze1, simplerMaze1.joinToString("\n"))

        val simplerMaze2 = removeDeadEndsFromMaze(maze2.lines())
        assertEquals(exp2.lines(), simplerMaze2)
    }

    companion object{
        val maze1 = """######
                      |##A###
                      |#....#
                      |##.#.#
                      |##B###
                      |######
        """.trimMargin()

        val maze2 = """#######
                      |#....##
                      |#.##.##
                      |#.##.##
                      |#.....#
                      |#######
        """.trimMargin()

        val exp1  = """######
                      |##A###
                      |##.###
                      |##.###
                      |##B###
                      |######
        """.trimMargin()

        val exp2  = """#######
                      |#....##
                      |#.##.##
                      |#.##.##
                      |#....##
                      |#######
        """.trimMargin()
    }
}

