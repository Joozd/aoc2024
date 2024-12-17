package nl.joozd

import nl.joozd.days.*
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

fun main() {
    while(true) {
        println("Advent of Code 2024!")
        println("Pick your day to run!")
        println("Only numbers or you wil get an exception.\n")
        println("0 or negative to exit")
        println("add 100 to run with test data")


        when (readln().trim().toInt()) {
            1 -> runDay(Day1())
            101 -> runDay(Day1(isTest = true))
            2 -> runDay(Day2())
            102 -> runDay(Day2(isTest = true))
            3 -> runDay(Day3())
            103 -> runDay(Day3(isTest = true))
            4 -> runDay(Day4())
            104 -> runDay(Day4(isTest = true))
            5 -> runDay(Day5())
            105 -> runDay(Day5(isTest = true))
            6 -> runDay(Day6())
            106 -> runDay(Day6(isTest = true))
            7 -> runDay(Day7())
            107 -> runDay(Day7(isTest = true))
            8 -> runDay(Day8())
            108 -> runDay(Day8(isTest = true))
            9 -> runDay(Day9())
            109 -> runDay(Day9(isTest = true))
            10 -> runDay(Day10())
            110 -> runDay(Day10(isTest = true))

            11 -> runDay(Day11())
            111 -> runDay(Day11(isTest = true))
            12 -> runDay(Day12())
            112 -> runDay(Day12(isTest = true))
            13 -> runDay(Day13())
            113 -> runDay(Day13(isTest = true))
            14 -> runDay(Day14())
            114 -> runDay(Day14(isTest = true))
            15 -> runDay(Day15())
            115 -> runDay(Day15(isTest = true))
            16 -> runDay(Day16())
            116 -> runDay(Day16(isTest = true))
            17 -> runDay(Day17())
            117 -> runDay(Day17(isTest = true))
            else -> break

        }
    }
}


/**
 * NOTE for best values, run a few times so the JVM can properly warm up
 */
private fun runDay(day: Day){
    val first = measureTimedValue { day.first() }
    val second = measureTimedValue { day.second() }
    printResults(day, first, second)
}

private fun printResults(day: Day, first: TimedValue<Long>, second: TimedValue<Long>) {
    println("******************************************************")
    println("***************      Day $day     **********************")
    println("******************************************************")
    println("1. ${first.value.toString().padStart(15, ' ')} - ${first.duration}")
    println("2. ${second.value.toString().padStart(15, ' ')} - ${second.duration}")
    println(". ${"Total time".padStart(15, ' ')} - ${first.duration + second.duration}")
    println("******************************************************")
}