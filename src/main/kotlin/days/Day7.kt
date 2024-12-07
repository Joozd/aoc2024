package nl.joozd.days

import nl.joozd.days.day7.CalibrationResult
import nl.joozd.utils.Counter

class Day7(isTest: Boolean = false): Day(7, isTest) {
    override fun first(): Long {
        return input.lines().sumOf {
            CalibrationResult.ofline(it).let{ cr ->
                if(canWork(cr)) cr.value else 0
            }
        }
    }

    override fun second(): Long {
        return input.lines().sumOf {
            val counter = Counter()
            CalibrationResult.ofline(it).let{ cr ->
                if(canWork(cr, allowConcat = true, counter).also{
                    println("$cr took $counter iterations")
                }) cr.value
                else 0
            }
        }
    }

    /**
     * Recursively checks if the calibration works.
     * It takes the last number, and calculates what value must be present to allow that number with a given operator.
     * It then checks the remainder of the calculation.
     * optimization: If a value cannot be divided by the last number, multiplication is not checked.
     * optimization: Check if a concat can work before trying it
     * This runs in O(n^3); max of about 12 digits makes for about half a million calculations in worst case, which is fine.
     * In practice: it is rarely more than 100, with a maximum of under 300
     */
    private fun canWork(calResult: CalibrationResult, allowConcat: Boolean = false, counter: Counter? = null): Boolean {
        counter?.count()
        if (calResult.numbers.size == 1) return calResult.numbers.first() == calResult.value
        val lastNumber = calResult.numbers.last()
        val remainingNumbers = calResult.numbers.dropLast(1)

        val addCalResultNeeded = CalibrationResult(calResult.value - lastNumber, remainingNumbers)
        val canDivide = calResult.value % lastNumber == 0L
        val mulCalResultNeeded = if (!canDivide) null else CalibrationResult(calResult.value / lastNumber, remainingNumbers)

        val concatFinishes = calResult.numbers.size == 2
        if (concatFinishes && allowConcat && calResult.numbers.joinToString("") {it.toString() }.toLong() == calResult.value) return true // concat on last remaining digits works
        val concatPossible = calResult.numbers.size > 2 && calResult.value.toString().endsWith(calResult.numbers.last().toString()) // if it was 2 and possible, previouis step returns true
        val concatCalResultNeeded = makeConcatCalResult(calResult)

        return canWork(addCalResultNeeded, allowConcat, counter) ||
                (canDivide && canWork(mulCalResultNeeded!!, allowConcat, counter)) ||
                (allowConcat && concatPossible && canWork(concatCalResultNeeded, allowConcat, counter))
    }

    private fun makeConcatCalResult(calResult: CalibrationResult): CalibrationResult{
        val lastNumberString = calResult.numbers.last().toString()
        var newValue = calResult.value
        repeat(lastNumberString.length) { _ ->
            newValue /= 10
        }
        return CalibrationResult(newValue, calResult.numbers.dropLast(1))
    }
}