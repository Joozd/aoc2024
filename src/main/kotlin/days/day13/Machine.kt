package nl.joozd.days.day13

import nl.joozd.utils.linearalgebra.AugmentedLongVectorMatrix
import nl.joozd.utils.linearalgebra.LongVector

class Machine private constructor(private val buttonA: LongVector, private val buttonB: LongVector, private val prize: LongVector) {
    fun lowestCost(): Long? =
        costOfCombination(
        AugmentedLongVectorMatrix(buttonA, buttonB, prize).findScalars()?.let { scalars ->
            ((scalars.first + 0.1).toLong() to (scalars.second + 0.1).toLong()).takeIf { longScalars->
                longScalars.first * buttonA.x + longScalars.second * buttonB.x == prize.x &&
                        longScalars.first * buttonA.y + longScalars.second * buttonB.y == prize.y
            }
        })


    override fun toString(): String = "Button A: $buttonA\nButton B: $buttonB\nPrize: $prize\nLowestCost: ${lowestCost()}"

    private fun costOfCombination(combination: Pair<Long, Long>?) = //pair<A, B>
        combination?.let {
            combination.first * COST_A + combination.second * COST_B
        }


    companion object{
        private const val COST_A = 3
        private const val COST_B = 1
        private const val OFFSET = 10000000000000L

        // example of input:
        // Button A: X+94, Y+34
        // Button B: X+22, Y+67
        // Prize: X=8400, Y=5400
        fun ofInput(input: String, addOffset: Boolean = false): Machine {
            val offset = if (addOffset) LongVector(OFFSET, OFFSET) else LongVector(0,0)
            return input.lines().map { line ->                                         // make lines
                line.split(',')                                       // split lines on comma
                    .map { part ->
                        part.filter { it.isDigit() }.toLong()
                    }       // get only digits from parts, make Int from those digits
            }
                .map { pair -> LongVector(pair[0], pair[1]) }                     // Make Intvectors from those Ints
                .let { vectors ->
                    Machine(vectors[0], vectors[1], vectors[2] + offset)                 // Build the Machine
                }
        }

    }
}