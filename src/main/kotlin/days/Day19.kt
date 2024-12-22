package nl.joozd.days

import java.util.HashMap

/**
 * Not great, but works and is fast enough.
 */
class Day19(isTest: Boolean = false) : Day(19, isTest) {
    private val inputParts = input.split("\n\n")
    private val patterns by lazy { inputParts.first().split(", ").toSet() }
    private val towels = inputParts.last().lines()

    override fun first(): Any {
        val shortestPattern = patterns.minOf { it.length }
        val longestPattern = patterns.maxOf { it.length }

        return towels.count {
            it.canBeMadeOf(patterns, shortestPattern, longestPattern)
        }
    }

    override fun second(): Any {
        val shortestPattern = patterns.minOf { it.length }
        val longestPattern = patterns.maxOf { it.length }

        return towels.sumOf {
            it.canBeMadeHowManyWays(patterns, shortestPattern, longestPattern).also{ println(it)}
        }
    }

    private fun String.canBeMadeOf(patterns: Set<String>, shortesPattern: Int, longestPattern: Int, cache: HashMap<String, Boolean> = HashMap<String, Boolean>()): Boolean =
        cache.getOrPut(this) {
            // println("Looking at $this")
            this in patterns ||
                    (shortesPattern..longestPattern).any { patternLength ->
                        (this.take(patternLength) in patterns) && this.substring(patternLength)
                            .canBeMadeOf(patterns, shortesPattern, longestPattern, cache)
                    }
        }

//    /**
//     * This is not pretty.
//     * Sorry, I am tired.
//     */
//    private fun String.canBeMadeHowManyWays(
//        patterns: Set<String>, shortesPattern: Int, longestPattern: Int, cache: HashMap<String, Long> = HashMap<String, Long>()
//    ): Long =
//        cache.getOrPut(this) {
//            when {
//                this.length < shortesPattern -> 0L
//                else -> (shortesPattern..minOf(this.length, longestPattern)).sumOf { patternLength ->
//                    when {
//                        this.take(patternLength) !in patterns -> 0L
//                        this.length == patternLength -> 1L
//                        else -> this.substring(patternLength)
//                            .canBeMadeHowManyWays(patterns, shortesPattern, longestPattern, cache)
//                    }
//                }
//            }
//        }

    /**
     * Check in how many ways a towel can be made
     */
    private fun String.canBeMadeHowManyWays(
        patterns: Set<String>, shortesPattern: Int, longestPattern: Int, cache: HashMap<String, Long> = HashMap<String, Long>()
    ): Long {
        cache[this]?.let { return it }

        // base case: Current string shorter than shortest pattern -> no possible towels can be made
        if(this.length < shortesPattern) return 0

        var possiblePatterns = 0L

        for(patternLength in shortesPattern..minOf(this.length, longestPattern)){
            // early return: not possible
            if(take(patternLength) !in patterns) {
                continue
            }
            // early return: pattern is entire towel
            if(this.length == patternLength){
                possiblePatterns++
                continue
            }
            possiblePatterns += substring(patternLength).canBeMadeHowManyWays(patterns, shortesPattern, longestPattern, cache)
        }

        return possiblePatterns.also{
            cache[this] =it
        }
    }
}