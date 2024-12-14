package nl.joozd.days

import nl.joozd.days.day14.Robot
import nl.joozd.utils.dumpGridToString
import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.LongVector
import nl.joozd.utils.linearalgebra.LongVectorRange

class Day14(isTest: Boolean = false) : Day(14, isTest) {
    val xSize: Long = if (isTest) 11 else 101 // from problem description
    val ySize: Long = if (isTest) 7 else 103 // from problem description
    val inputLines = input.lines()
    val robots by lazy { inputLines.map { Robot.ofLine(it) } }

    override fun first(): Long {
        val endPositions = robots.map { it.move(100, xSize, ySize)}
        return getScore(endPositions)
    }

    /**
     * I initially made a dump of the first 500seconds; I noted a clustering at 63 and 82,
     * and from there every 103 (for 63) and 101(for 82) seconds.
     * This is not a coincidence of course, as those are the modulo's (modula? modulae?)
     * So, I decided to check the first 10K seconds (which in hindsight was just a little on the short side as 101*103
     * is slightly more than that) and manually checked the outputs.
     * This because I had no idea what the tree would look like
     * and no guarantees there would be, for instance, 6 robots in a row in the tree.
     *
     * That worked, but did need a bunch of manual checking. So second time (helped by some knowledge of the tree)
     * I did implement my backup plan checking for 6 in a row,
     * and asking user input to see if the tree was in the result.
     */
    override fun second(): Long {
            var seconds = -1L
            var answer = "n"
            while(answer != "y" ) {
                seconds++
                val positionsAfterNSeconds = robots.map { it.move(seconds, xSize, ySize) }
                    .map { IntVector(it[0].toInt(), it[1].toInt(), comparator = IntVector.coordinatesComparator) }
                    .sorted() // need IntVectors instead of long, for dumping map
                if (hasNInARow(positionsAfterNSeconds)) {
                    val map = dumpGridToString(positionsAfterNSeconds)
                    println(map)
                    println("\n Any christmas tree in this? (y/n)\n")  // anything not lowercase "y" means no
                    answer = readln()
                }
            }
            return seconds
    }

    private fun hasNInARow(positions: List<IntVector>, n: Int = 6): Boolean{
        outer@for (i in 0..positions.size - 1 - n){
            for ( offset in 1..n) {
                if (positions[i + offset].x != positions[i].x + offset)
                    continue@outer
            }
            return true // we made it through 1..6!
        }
        return false // no 6 in a row found
    }

    /**
     * Divide the rom into quadrants, middle lines don't count.
     * Count the robots in each quadrant, multiply the values.
     */
    private fun getScore(positions: List<LongVector>): Long{
        val topLeftOfMiddle = LongVector(xSize/2 - 1, ySize/2 - 1) // minus one for 0-indexed
        val topRightOfMiddle = topLeftOfMiddle + LongVector(2,0)
        val bottomLeftOfMiddle = topLeftOfMiddle + LongVector(0,2)
        val bottomRightOfMiddle = topLeftOfMiddle + LongVector(2,2)

        val topLeft = LongVectorRange(LongVector(0,0), topLeftOfMiddle)
        val topRight = LongVectorRange(topRightOfMiddle, LongVector(xSize, 0))
        val bottomLeft = LongVectorRange(bottomLeftOfMiddle, LongVector(0, ySize))
        val bottomRight = LongVectorRange(bottomRightOfMiddle, LongVector(xSize, ySize))

        return positions.count { it in topLeft } *
               positions.count { it in topRight } *
               positions.count { it in bottomLeft } *
               positions.count { it in bottomRight }
            .toLong()
    }

}