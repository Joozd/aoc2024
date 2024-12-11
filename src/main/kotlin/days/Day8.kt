package nl.joozd.days

import nl.joozd.utils.extensions.combinations
import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.shorten

class Day8(isTest: Boolean = false): Day(8, isTest) {
    private val lines = input.lines()
    private val map by lazy { makeMap(lines) }
    private val xRange = lines.first().indices
    private val yRange = lines.indices

    /**
     * This can probably be done by calculating instead of filling,
     * but the data set is pretty manageable, so I'll go with actually filling it
     */
    override fun first(): Long {

        val possibleLocations = buildSet {
            for (antennas in map.values) {
                antennas.combinations().forEach { pair ->
                    val diff = pair.second - pair.first
                    (pair.first - diff) .takeIf{ it.x in xRange && it.y in yRange }?.let{ add(it)}
                    (pair.second + diff).takeIf{ it.x in xRange && it.y in yRange }?.let{ add(it)}
                }
            }
        }

        return possibleLocations.size.toLong()
    }

    /**
     * This can probably be done by calculating instead of filling,
     * but the data set is pretty manageable, so I'll go with actually filling it
     */
    override fun second(): Long {
        val possibleLocations = buildSet {
            for (antennas in map.values) {
                antennas.combinations().forEach { pair ->
                    val direction = (pair.second - pair.first).shorten()
                    var nextPos = pair.first
                    while(nextPos.x in xRange && nextPos.y in yRange){
                        add(nextPos)
                        nextPos -= direction
                    }
                    nextPos = pair.first + direction // once again go from first, so we get intermediates as well
                    while(nextPos.x in xRange && nextPos.y in yRange){
                        add(nextPos)
                        nextPos += direction
                    }
                }
            }
        }

        return possibleLocations.size.toLong()
    }

    private fun makeMap(lines: List<String>): Map<Char, List<IntVector>> = buildMap<Char, MutableList<IntVector>>{
        for (y in lines.indices){
            for (x in lines.first().indices){
                val c =  lines[y][x]
                if(c == '.') continue // early return

                getOrPut(c){ ArrayList() }.add(IntVector(x,y))
            }
        }
    }
}