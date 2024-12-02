package nl.joozd.days

/**
 * https://adventofcode.com/2024/day/2
 */
class Day2(isTest: Boolean = false): Day(2, isTest) {
    private val reports by lazy { generateReports(input) }

    override fun first(): Long =
        reports.count { isSafe(it) }.toLong()


    override fun second(): Long =
        reports.count{ report ->
            report.indices.any{ i ->
                isSafe(report.take(i) + report.drop(i+1))
            }
        }.toLong()

    /**
     * Generate reports
     */
    private fun generateReports(input: String) =
        input.lines().map { l ->l.split(' ').map { splits -> splits.toLong() } }

    /**
     * @return if this is a safe report, false if not.
     */
    private fun isSafe(report: List<Long>): Boolean {
        if (!allIncrease(report) && !allDecrease(report)) return false
        return hasAllSafeCombinations(report)
    }

    /**
     * @return true if all pairs in [report] differ by 1, 2 or 3
     */
    private fun hasAllSafeCombinations(report: List<Long>): Boolean {
        for (i in 0..<report.indices.last) {
            if (isUnsafeCombination(report[i], report[i + 1])) return false
        }
        return true
    }

    private fun isUnsafeCombination(l: Long, r: Long): Boolean = when(l - r){
        1L,2L,3L, -1L, -2L, -3L -> false

        else -> true
    }

    /**
     * @return true if all values in [report] are larger than the previous
     */
    private fun allIncrease(report: List<Long>): Boolean {
        var last = Long.MIN_VALUE
        for (v in report) {
            if (v <= last) return false
            last = v
        }
        return true
    }

    /**
     * @return true if all values in [report] are more smol than the previous
     */
    private fun allDecrease(report: List<Long>): Boolean {
        var last = Long.MAX_VALUE
        for (v in report){
            if (v >= last) return false
            last = v
        }
        return true
    }
}