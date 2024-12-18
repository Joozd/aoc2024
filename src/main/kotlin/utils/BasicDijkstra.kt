package nl.joozd.utils

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.IntVectorRange
import nl.joozd.utils.linearalgebra.fourPotentialNeighbours
import java.util.*

/**
 * Dijkstra implementation for finding the shortest distance through a normal AOC maze
 */
class BasicDijkstra(
    private val mazeConstraints: IntVectorRange,
    private val moveIsPossible: MoveIsPossible
) {
    fun getshortestDistance(startPosition: IntVector, endPosition: IntVector): Int?{
//        println("Getting shortest distance from $startPosition to $endPosition")
        var currentNode = NodeWithDistance(startPosition, 0)
        val visitedNodes = HashSet<IntVector>()
        val visitList: PriorityQueue<NodeWithDistance> = PriorityQueue<NodeWithDistance>()
        while(currentNode.position != endPosition){
//            println("Looking at $currentNode - visitList has $visitList")
            val nextDistance = currentNode.distance + 1
            // Get neighbours from current node and add them to [visitList] if they are reachable
            for(n in currentNode.position.fourPotentialNeighbours()){
//                println("Neighbour: $n - ${n in mazeConstraints} / ${moveIsPossible(n, nextDistance)}")
                if (n in mazeConstraints && moveIsPossible(n, nextDistance))
                    visitList.add(NodeWithDistance(n, nextDistance))
            }
            visitedNodes.add(currentNode.position)
            // Get the next unvisited node from visitList
            do{
                currentNode = visitList.poll() ?: return null // no solution found
            } while(currentNode.position in visitedNodes)
        }
        // Now, currentNode.position == endPosition
        return currentNode.distance
    }


    fun interface MoveIsPossible{
        operator fun invoke(moveToPosition: IntVector, distanceOfMove: Int): Boolean
    }

    private data class NodeWithDistance(val position: IntVector, val distance: Int): Comparable<NodeWithDistance>{
        override fun compareTo(other: NodeWithDistance)=
            distance.compareTo(other.distance)
    }
}