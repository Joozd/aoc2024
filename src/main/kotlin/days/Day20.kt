package nl.joozd.days

import nl.joozd.utils.linearalgebra.*

/**
 * Meet-in-the-middle implementation.
 * I initially did this with Dijkstra all over the place, which made this really slow (20+ seconds for part 1)
 * Now it runs in 9 ms for part 1 and 370ms or so for part 2 (when warmed up).
 */
class Day20(val isTest: Boolean = false) : Day(20, isTest) {
    private val lines = input.lines()
    private val distancesFromStart by lazy { distancesFromChar('S', lines) }
    private val distancesFromEnd by lazy { distancesFromChar('E', lines) }


    override fun first(): Any? {
        val timeToSave = if(isTest) 20 else 100
        return possibleRoutesWithCheat(2, timeToSave, distancesFromStart, distancesFromEnd)
    }

    override fun second(): Any? {
        val timeToSave = if(isTest) 50 else 100 // expect 29 for test
        val possibleRoutes = possibleRoutesWithCheat(20, timeToSave, distancesFromStart, distancesFromEnd)
        return possibleRoutes
    }


    /**
     * Find number of possible cheats that give enough time savings.
     */
    private fun possibleRoutesWithCheat(
        cheatLength: Int,
        minimumSaving: Int,
        distancesFromStart: Map<IntVector, Int>,
        distancesFromEnd: Map<IntVector, Int>
    ): Int{
        val distanceNoCheat = distancesFromStart[findPositionOf('E', lines)]!!
        val maxAllowedDistance = distanceNoCheat - minimumSaving

        var foundRoutes = 0

        for(pos in distancesFromStart.keys){
            // checking for early access if manhattan distance is already too big does not give a noticable performance increase
            for(cheatDist in 2..cheatLength){
                for (cheatCandidate in pos.withAllManhattanDistanceNeighbours(cheatDist).filter { distancesFromEnd.containsKey(it) } ) {
                    val totalDistance = distancesFromStart[pos]!! + distancesFromEnd[cheatCandidate]!! + cheatDist
                    if (totalDistance <= maxAllowedDistance)
                        foundRoutes ++
                }
            }
        }


        return foundRoutes
    }

    /**
     * makes a map with all distances to a field filled with [char]
     * Undefined if multiple fields have that char
     * @throws NullPointerException if [char] is not in [maze]
     *
     * Maze is a list of strings; all positions that are not '#' are accessible
     */
    private fun distancesFromChar(char: Char, maze: List<String>): Map<IntVector, Int> {
        fun isAccessible(neighbor: IntVector): Boolean {
            val c = maze[neighbor] ?: return false
            return c != '#'
        }
        val map = HashMap<IntVector, Int>() // location to distance
        val visitList = ArrayDeque<IntVector>()

        findPositionOf(char, maze).let {
            visitList.add(it)
            map[it] = 0
        }

        while(visitList.isNotEmpty()){
            val current = visitList.removeFirst()
            for (neighbor in current.fourPotentialNeighbours()) {
                if (!map.containsKey(neighbor) && isAccessible(neighbor)) {
                    map[neighbor] = map[current]!! +1
                    visitList += neighbor
                }
            }
        }
        return map
    }

    /**
     * Find position of [char] in [maze]
     * Undefined if multiple fields have that char
     * @throws NullPointerException if [char] is not in [maze], maze is empty
     * undefined if maze is not rectangular, might throw NullPointerException
     */
    private fun findPositionOf(char: Char, maze: List<String>): IntVector{
        for(y in maze.indices){
            for(x in maze[0].indices){
                if(maze[y][x] == char) return IntVector(x,y)
            }
        }
        throw(NullPointerException("$char not found in maze"))
    }
}

