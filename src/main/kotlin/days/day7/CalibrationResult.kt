package nl.joozd.days.day7

data class CalibrationResult(val value: Long, val numbers: List<Long>) {
    companion object{
        fun ofline(line: String): CalibrationResult{
            val colonIndex = line.indexOf(':')
            val total = line.substring(0 until colonIndex).toLong()
            val numbers = line.substring(colonIndex + 2).split(' ').map { it.toLong()}
            return CalibrationResult(total, numbers)
        }
    }
}