package nl.joozd.days

import nl.joozd.days.day5.Page

class Day5(isTest: Boolean = false): Day(5, isTest) {
    private val rulesAndSeries by lazy { input.split("\n\n") }
    private val rules by lazy { rulesAndSeries.first().lines().map { line -> line.split("|").map { it.toInt() }} }
    private val series by lazy { rulesAndSeries.last().lines().map { line -> line.split(",").map { it.toInt() } } }

    override fun first(): Long {
        var result = 0L
        val higherThan = makeHigherThan(rules)
        val lowerThan = makeLowerThan(rules)

        series.forEach{ s->
            val pages = s.map { Page(it, lowerThan, higherThan) }
            val sortedPages = pages.toList().sorted()
            if (pages == sortedPages) {
                result += sortedPages[pages.size/2].value
            }
        }
        return result
    }

    override fun second(): Long {
        var result = 0L
        val higherThan = makeHigherThan(rules)
        val lowerThan = makeLowerThan(rules)

        series.forEach{ s->
            val pages = s.map { Page(it, lowerThan, higherThan) }
            val sortedPages = pages.toList().sorted()
            if (pages != sortedPages) {
                result += sortedPages[pages.size/2].value
            }
        }
        return result
    }

    // all items in a set are higher than the key
    private fun makeHigherThan(rules: List<List<Int>>): Map<Int, Set<Int>> = buildMap{
        rules.forEach{ rule ->
            (this.getOrPut(rule.first()) { HashSet() } as HashSet).add(rule.last())
        }
    }

    // all items in a set are lower than the key
    private fun makeLowerThan(rules: List<List<Int>>): Map<Int, Set<Int>> = buildMap{
        rules.forEach{ rule ->
            (this.getOrPut(rule.last()) { HashSet() } as HashSet).add(rule.first())
        }
    }
}