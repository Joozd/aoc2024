package nl.joozd.days

import nl.joozd.aoc2023.common.linearalgebra.IntVector

class Day4(isTest: Boolean = false): Day(4, isTest) {
    private val puzzle: Array<CharArray> = input.lines().map{it.toCharArray()}.toTypedArray()

    override fun first(): Long {
        var foundAmount = 0L
        puzzle.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'X') {
                    foundAmount += allDirections.count { checkWord(puzzle, IntVector(x,y), it, "XMAS")}
                }
            }
        }
        return foundAmount
    }

    // Probably a small speed increase to be had by not checking the outer cells,
    // but I think it will be marginal, and it does add complexity.
    override fun second(): Long {
        var foundAmount = 0L
        puzzle.forEachIndexed { y, line ->
            foundAmount += line.indices.count { x ->
                checkX(puzzle, IntVector(x,y))
            }
        }
        return foundAmount
    }

    /**
     * Checks if a [word] starts at [location]in the direction of [direction]
     * @param word The word to search for, empty will return true.
     * @return true if the word does indeed start here in that direction, false if not.
     */
    private fun checkWord(puzzle: Array<CharArray>, location: IntVector, direction: IntVector, word: String): Boolean{
        for (i in word.indices){
            if(!checkLocation(puzzle, location + direction * i, word[i])) return false
        }
        return true
    }

    /**
     * Check if [checkLocation] is the center of an X-MAS cross
     */
    private fun checkX(puzzle: Array<CharArray>, location: IntVector): Boolean{
        if(!checkLocation(puzzle, location, 'A')) return false

        //a bit hard to read, but checks if left bottom to right top is SAM or MAS, and the same for left top to right bottom
        return (checkWord(puzzle, location + IntVector.SW, IntVector.NE, "MAS") ||
                checkWord(puzzle, location + IntVector.SW, IntVector.NE, "SAM")) &&
            (checkWord(puzzle, location + IntVector.NW, IntVector.SE, "MAS") ||
                checkWord(puzzle, location + IntVector.NW, IntVector.SE, "SAM"))
    }

    /**
     * Check if [location] is a valid location in [puzzle] and contains [wantedChar]
     */
    private fun checkLocation(puzzle: Array<CharArray>, location: IntVector, wantedChar: Char): Boolean =
        location[1] in puzzle.indices && location[0] in puzzle[0].indices &&
            puzzle[location[1]][location[0]] == wantedChar

    companion object{
        private val allDirections = listOf(
            IntVector.NW,
            IntVector.NORTH,
            IntVector.NE,
            IntVector.EAST,
            IntVector.SE,
            IntVector.SOUTH,
            IntVector.SW,
            IntVector.WEST
        )
    }
}