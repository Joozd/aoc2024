package nl.joozd.days

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.fourPotentialNeighbours
import nl.joozd.utils.linearalgebra.get

class Day10(isTest: Boolean = false): Day(10, isTest) {
    val map = input.lines().map { line -> line.map{ try { it.digitToInt() } catch(e: Throwable) { 10 } }}

    override fun first(): Long =
        map.indices.sumOf { y ->
            map[0].indices.sumOf { x ->
                ninesReachable(map, IntVector(x,y)).size
            }
        }.toLong()


    override fun second(): Long =
        map.indices.sumOf { y ->
            map[0].indices.sumOf { x ->
                rating(map, IntVector(x,y))
            }
        }.toLong()

    /**
     * Count how many distinct endpoints can be reached from this trailhead
     */
    private fun ninesReachable(map: List<List<Int>>, currentPos: IntVector, startValue: Int = 0): Set<IntVector>{
        // base cases
        if(map[currentPos] != startValue) return emptySet()
        if(startValue == 9 && map[currentPos] == 9) return setOf(currentPos)
        return currentPos.fourPotentialNeighbours().map {
            ninesReachable(map, it, startValue + 1)
        }.flatten().toSet()
    }

    /**
     * Count how many valid trails start or continue from this point
     * An end point(9) means 1.
     */
    private fun rating(map: List<List<Int>>, currentPos: IntVector, startValue: Int = 0): Int {
        // base cases
        if(map[currentPos] != startValue) return 0
        if(startValue == 9 && map[currentPos] == 9) return 1
        return currentPos.fourPotentialNeighbours().sumOf {
            rating(map, it, startValue + 1)
        }
    }
}