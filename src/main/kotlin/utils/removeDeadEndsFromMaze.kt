package nl.joozd.utils

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.fourPotentialNeighbours

/**
 * Removes all dead ends from a maze.
 * Assumes anything is open space that is not '#'
 * Assumes maze has all equal length rows
 */
fun removeDeadEndsFromMaze(maze: List<String>): List<String> {
    val mutableMaze = maze.map { it.toCharArray() }
    var snapshot = emptyList<String>()
    do {
        snapshot = mutableMaze.map { it.joinToString("") }
        for (y in mutableMaze.indices) {
            for (x in mutableMaze.first().indices) {
                var pos: IntVector? = IntVector(x, y)
                if (mutableMaze[pos!!.y][pos.x] != '.') continue // only remove dead ends with a '.'
                var nextPos = findNeighbourOfDeadEnd(pos, mutableMaze)
                while (nextPos != null) {
                    mutableMaze[pos!!.y][pos.x] = '#'
                    pos = nextPos
                    nextPos = findNeighbourOfDeadEnd(pos, mutableMaze)
                }
            }
        }
        //println(snapshot.joinToString("\n"))
    } while (snapshot != mutableMaze.map { it.joinToString("") })

    return snapshot
}





/**
 * Returns the one neighbour of a dead end, or null if not a dead end or isolated cell
 */
private fun findNeighbourOfDeadEnd(pos: IntVector, maze: List<CharArray>): IntVector? {
    val openNeighbours = fourNeighbours.mapNotNull { (pos + it).takeIf { n -> (maze.getOrNull(n.y)?.getOrNull(n.x) ?: '#') != '#' } }.toList()
    return if(openNeighbours.size != 1) null
    else openNeighbours.first().takeIf { maze[it.y][it.x] == '.' } // only remove '.'
}

private val fourNeighbours = IntVector(0,0).fourPotentialNeighbours()