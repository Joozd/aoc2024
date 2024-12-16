package nl.joozd.days

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.Matrices
import nl.joozd.utils.linearalgebra.get
import nl.joozd.utils.removeDeadEndsFromMaze
import java.util.*

/**
 * Well... Dijkstra it is!
 * https://adventofcode.com/2024/day/16
 */
class Day16(isTest: Boolean = false) : Day(16, isTest) {
    private val maze = removeDeadEndsFromMaze(input.lines())

    override fun first(): Long {
        val startAndEnd = findStartAndEnd(maze)
        val map = maze.map { it.replace('E', '.')} // this way we only have to check for '.' and not both '.' and 'E'. Check for end using [endpos].
        val result = dijkstraHelemaalDeMoeder(map, startAndEnd.first, startAndEnd.second)

        println("Simplified maze:\n${maze.joinToString("\n")}")
        return result.toLong()
    }

    override fun second(): Long {
        val startAndEnd = findStartAndEnd(maze)
        val map = maze.map { it.replace('E', '.')} // this way we only have to check for '.' and not both '.' and 'E'. Check for end using [endpos].
        val maxDist = dijkstraHelemaalDeMoeder(map, startAndEnd.first, startAndEnd.second)

        return findPathsRecursive(map, startAndEnd.first, IntVector.EAST, startAndEnd.second, 0, maxDist).size.toLong()
    }


    /**
     * NOTE: Needs E to be replaced by '.'
     *
     * Finds the shortest distance between [startPos] and [endPos] on [map].
     */
    private fun dijkstraHelemaalDeMoeder(map: List<String>, startPos: IntVector, endPos: IntVector, startDir: IntVector = IntVector.EAST): Int{
        var currentPosDir = startPos to startDir
        var currentDistance = 0

        val visitedNodes = HashSet<Pair<IntVector, IntVector>>() // position to direction
        val visitList: PriorityQueue<Pair<Pair<IntVector, IntVector>, Int>> = PriorityQueue<Pair<Pair<IntVector, IntVector>, Int>>(distanceComparator).apply{
            @Suppress("KotlinConstantConditions") // More clear than just dropping a '0' here
            add(currentPosDir to currentDistance)
        }

        while(currentPosDir.first != endPos){
            // mark this step as visited
            visitedNodes.add(currentPosDir)

            // add next steps to [visitList]
            val currentPos = currentPosDir.first
            val currentDir = currentPosDir.second

            // add turns
            visitList.add((currentPos to currentDir * Matrices.TURN_LEFT_ASCII_MAP) to currentDistance + COST_OF_TURN)
            visitList.add((currentPos to currentDir * Matrices.TURN_RIGHT_ASCII_MAP) to currentDistance + COST_OF_TURN)

            // add step if able
            if (map[currentPos + currentDir] == '.') visitList.add((currentPos + currentDir to currentDir) to currentDistance + 1)

            // get new node. Discard already visited ones as they are a worse solution than what we have.
            do {
                val polled = visitList.poll() ?: return Int.MAX_VALUE // dead end
                currentPosDir = polled.first
                currentDistance = polled.second
            } while (currentPosDir in visitedNodes)
        }

        return currentDistance
    }

    /**
     * Exponential complexity, but checking and ignoring paths that are not OK
     * as well as "no choice" optimizations should be a big improvement.
     * Runs in under 10 seconds, which is fine to me.
     *
     * This recursively finds all points on all the possible shortest paths
     * from [currentPos] to [target], with a max length of [maxDist]
     */
    private fun findPathsRecursive(map: List<String>, currentPos: IntVector, currentDir: IntVector, target: IntVector, currentDist: Int, maxDist: Int, checkNeeded: Boolean = false): Set<IntVector>{
        //base case: At target
        if(currentPos == target) return setOf(currentPos)

        //base case: Not on shortest route: (this should include dead ends)
        // Only if previous was not a 'no choice' situation
        if (checkNeeded && dijkstraHelemaalDeMoeder(map, currentPos, target, currentDir) + currentDist != maxDist) return emptySet()

        //determine options
        val leftTurn = currentDir * Matrices.TURN_LEFT_ASCII_MAP
        val rightTurn = currentDir * Matrices.TURN_RIGHT_ASCII_MAP

        val straight = map[currentPos + currentDir] == '.'
        val nextAfterLeft = map[currentPos + leftTurn] == '.'
        val nextAfterRight = map[currentPos + rightTurn] == '.'

        //No choice, only move straight
        if(straight && !nextAfterLeft && !nextAfterRight)
            return setOf(currentPos) + findPathsRecursive(map, currentPos+currentDir, currentDir, target, currentDist + 1, maxDist, checkNeeded = false)
        if(!straight && nextAfterLeft && !nextAfterRight)
            return setOf(currentPos) + findPathsRecursive(map, currentPos, leftTurn, target, currentDist + COST_OF_TURN, maxDist, checkNeeded = false)
        if(!straight && !nextAfterLeft && nextAfterRight)
            return setOf(currentPos) + findPathsRecursive(map, currentPos, rightTurn, target, currentDist + COST_OF_TURN, maxDist, checkNeeded = false)

        // Intersection, do all options
        val straightResult: Set<IntVector> = if(!straight) emptySet()
            else setOf(currentPos) + findPathsRecursive(map, currentPos+currentDir, currentDir, target, currentDist + 1, maxDist, checkNeeded = true)

        val leftTurnResult: Set<IntVector> = if(!nextAfterLeft) emptySet()
            else setOf(currentPos) + findPathsRecursive(map, currentPos, leftTurn, target, currentDist + COST_OF_TURN, maxDist, checkNeeded = true)

        val rightTurnResult: Set<IntVector> = if(!nextAfterRight) emptySet()
            else setOf(currentPos) + findPathsRecursive(map, currentPos, rightTurn, target, currentDist + COST_OF_TURN, maxDist, checkNeeded = true)

        return straightResult + leftTurnResult + rightTurnResult
    }


    /**
     * Undefined if multiple S and E,
     * @throws NullPointerException if one of either not found
     */
    private fun findStartAndEnd(maze: List<String>): Pair<IntVector, IntVector>{
        var start: IntVector? = null
        var end: IntVector? = null
        maze.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when(c){
                    'S' -> start = IntVector(x,y)
                    'E' -> end = IntVector(x,y)
        }   }   }
        return start!! to end!!
    }



    companion object{
        private const val COST_OF_TURN = 1000

        private val distanceComparator: Comparator<Pair<Pair<IntVector, IntVector>, Int>> = object: Comparator<Pair<Pair<IntVector, IntVector>, Int>>{
            override fun compare(
                o1: Pair<Pair<IntVector, IntVector>, Int>,
                o2: Pair<Pair<IntVector, IntVector>, Int>
            ): Int =
                o1.second - o2.second
        }
    }
}