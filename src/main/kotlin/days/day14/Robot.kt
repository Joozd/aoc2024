package nl.joozd.days.day14

import nl.joozd.utils.getNumbersFromLine
import nl.joozd.utils.linearalgebra.LongVector

class Robot private constructor(private val initialPos: LongVector, private val movement: LongVector) {
    /**
     * Gives the position a Robot would end up in if he moved for [moves] seconds
     */
    fun move(moves: Long, maxX: Long, maxY: Long): LongVector{
        val newX: Long = ((initialPos.x + movement.x * moves) % maxX).let { if (it < 0) it + maxX else it}
        val newY: Long = ((initialPos.y + movement.y * moves) % maxY).let { if (it < 0) it + maxY else it}
        return LongVector(newX, newY)
    }

    companion object{
        fun ofLine(line: String) = getNumbersFromLine(line).toList().let{
            Robot(LongVector(it[0], it[1]), LongVector(it[2], it[3]))
        }
    }
}