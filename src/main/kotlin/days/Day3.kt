package nl.joozd.days

class Day3(isTest: Boolean = false) : Day(3, isTest) {
    override fun first(): Long {
        var result = 0L

        // Matches "mul(x,y)" where a and b are digits; captures x as Group 1 and y as Group 2
        val re = """mul\((\d+),(\d+)\)""".toRegex()
        val muls = re.findAll(input)

        for (mul in muls) {
            result += mul.groupValues[1].toLong() * mul.groupValues[2].toLong()
        }
        return result
    }

    /*
     * Same as 1, but with an extra multiplier [negator].
     * It gets set to 1 whhenever we see a "do()" and to 0 whenever we see a "don't()"
     */
    override fun second(): Long {
        var result = 0L

        // Matches "mul(a,b)" capturing a (Group 1) and b (Group 2), or "do()" or "don't()"
        val re = """mul\((\d+),(\d+)\)|$RE_DO|$RE_DONT""".toRegex()
        var negator = 1L
        val instructions = re.findAll(input)

        for (inst in instructions) {
            when (inst.groupValues[0]) {
                DO -> negator = 1L
                DONT -> negator = 0L
                else -> result += inst.groupValues[1].toLong() * inst.groupValues[2].toLong() * negator
            }
        }

        return result
    }

    companion object {
        private const val DO = """do()"""
        private const val DONT = """don't()"""

        // DO but with escaped brackets
        private const val RE_DO = """do\(\)"""

        // DONT but with escaped brackets
        private const val RE_DONT = """don't\(\)"""
    }
}