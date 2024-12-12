package nl.joozd.days

import nl.joozd.utils.QueueSet
import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.fourPotentialNeighbours
import nl.joozd.utils.linearalgebra.get
import java.util.*

class Day12(isTest: Boolean = false): Day(12, isTest) {
    private val lines = input.lines()

    override fun first(): Long {
        val visited = HashSet<IntVector>()
        return lines.indices.sumOf { y ->
            lines[0].indices.sumOf { x ->
                getFencePriceForRegion(IntVector(x,y), visited)
            }
        }
    }

    override fun second(): Long {
        println("********************************")
        val visited = HashSet<IntVector>()
        return lines.indices.sumOf { y ->
            lines[0].indices.sumOf { x ->
                getFencePriceForRegion(IntVector(x,y), visited, discount = true)
            }
        }
    }

    /**
     * Side effect: adds all positions in current area to [visitedList]
     * Only counts positions that have not been visited.
     */
    private fun getFencePriceForRegion(pointInRegion: IntVector, visitedList: MutableSet<IntVector>, discount: Boolean = false): Long{
        if(pointInRegion in visitedList) return 0L

        val visitedInRegion = HashSet<IntVector>()
        val visitList = QueueSet<IntVector>()
        visitList.add(pointInRegion)
        var fenceNeeded = 0

        while (visitList.isNotEmpty()){
            val currentPos = visitList.remove()
            val neighbours = currentPos.fourPotentialNeighbours().filter{
                lines[it] == lines[currentPos]
            }.toList()
            val fence = if(discount) getSides(lines, currentPos) else (4 - neighbours.size)
            fenceNeeded += fence

            // add unvisited neighbours to the visit stack
            neighbours.filter { it !in visitedInRegion }.forEach { visitList.add(it) }

            //add current plot visited list
            visitedInRegion += currentPos
        }
        visitedList.addAll(visitedInRegion)
        val price = visitedInRegion.size * fenceNeeded.toLong()

        // println("A region of ${lines[pointInRegion]} plants with price ${visitedInRegion.size} * $fenceNeeded = $price")
        return price
    }

    /**
     * We count the left-most edge of the line, seen from the inside, as a line.
     */
    private fun getSides(map: List<String>, location: IntVector): Int{
        val crop = map[location]
        val north = when {
            map[location + IntVector.NORTH] == crop -> 0
            map[location + IntVector.WEST] != crop -> 1
            map[location + IntVector.NW] == crop -> 1 // west was crop, so the line turns here
            else -> 0
        }
        val east = when {
            map[location + IntVector.EAST] == crop -> 0
            map[location + IntVector.NORTH] != crop -> 1
            map[location + IntVector.NE] == crop -> 1 // north was crop, so the line turns here
            else -> 0
        }
        val south = when {
            map[location + IntVector.SOUTH] == crop -> 0
            map[location + IntVector.EAST] != crop -> 1
            map[location + IntVector.SE] == crop -> 1 // east was crop, so the line turns here
            else -> 0
        }
        val west = when {
            map[location + IntVector.WEST] == crop -> 0
            map[location + IntVector.SOUTH] != crop -> 1
            map[location + IntVector.SW] == crop -> 1 // south was crop, so the line turns here
            else -> 0
        }
        val total = north + east + south + west
        // println("$location has $total sides ($north/$east/$south/$west")
        return total
    }

}