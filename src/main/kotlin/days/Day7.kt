package nl.joozd.days

import nl.joozd.days.day7.CalibrationResult

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
            CalibrationResult.ofline(it).let{ cr ->
                if(canWork(cr, allowConcat = true)) cr.value
                else 0
            }
        }
    }

    private fun canWork(calResult: CalibrationResult, allowConcat: Boolean = false): Boolean {
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

        return canWork(addCalResultNeeded, allowConcat) ||
                (canDivide && canWork(mulCalResultNeeded!!, allowConcat)) ||
                (allowConcat && concatPossible && canWork(concatCalResultNeeded, allowConcat))
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