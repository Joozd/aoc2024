package nl.joozd.days

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.Matrices

class Day6(isTest: Boolean = false): Day(6, isTest) {
    private val lines = input.lines()
    private val visitedPositions by lazy { getVisitedPositions(lines) }
    private val startPos by lazy { getStartPos(lines) }

    override fun first(): Long {


        return visitedPositions.size.toLong()
    }

    override fun second(): Long {
        return visitedPositions.count{ pos ->
            if(pos == startPos) false
            else{
                val maze = lines.map { it.toCharArray() }
                maze[pos.y][pos.x] = '#'
                detectLoop(maze)
            }
        }.toLong()
    }

    /**
     * Get starting position from [lines]
     */
    private fun getStartPos(lines: List<String>) = lines.indices.firstNotNullOf { y ->
        lines[0].indices.firstNotNullOfOrNull { x ->
            if (lines[y][x] == '^')
                IntVector(x, y)
            else null
        }
    }

    private fun getVisitedPositions(lines: List<String>): Set<IntVector>{
        val visitedPositions = HashSet<IntVector>()
        var currentPos = startPos
        var currentDir = IntVector.NORTH
        var nextPos = currentPos + currentDir

        while(nextPos.y in lines.indices && nextPos.x in lines.first().indices){
            if(lines[nextPos.y][nextPos.x] != '#') {// check if no obstacle
                visitedPositions.add(currentPos)
                currentPos = nextPos
            }
            else{
                currentDir *= Matrices.TURN_RIGHT_ASCII_MAP // matrix multiplication for turning right
            }
            nextPos = currentPos + currentDir
        }
        visitedPositions.add(currentPos) // add the last position before exiting
        return visitedPositions
    }

    /**
     * Detects if a run will end in a loop (true) or out of bounds(false)
     */
    private fun detectLoop(maze: List<CharArray>): Boolean {
        val visitedPositions = HashSet<Pair<IntVector, IntVector>>() // position to direction
        var currentPos = getStartPos(lines)
        var currentDir = IntVector.NORTH
        var nextPos = currentPos + currentDir

        while(nextPos.y in maze.indices && nextPos.x in maze.first().indices){
            if(maze[nextPos.y][nextPos.x] != '#') {// check if no obstacle
                currentPos = nextPos
            }
            else{
                currentDir *= Matrices.TURN_RIGHT_ASCII_MAP // matrix multiplication for turning right
                if(!visitedPositions.add(currentPos to currentDir)) return true // only check turns, saves a lot of logic I think
            }
            nextPos = currentPos + currentDir
        }
        return false // out of bounds, no loop
    }

}