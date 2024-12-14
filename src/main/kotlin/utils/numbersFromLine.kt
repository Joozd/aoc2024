package nl.joozd.utils

private val numberRegex = "-?\\d+".toRegex()

fun getNumbersFromLine(line: String): Sequence<Long> =
    numberRegex.findAll(line).map{
        it.value.toLong()
    }