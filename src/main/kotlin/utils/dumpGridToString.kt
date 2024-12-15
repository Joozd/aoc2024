package nl.joozd.utils

import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.IntVectorWithValue

fun dumpGridToString(positionsToDraw: Collection<IntVector>): String{
    if (positionsToDraw.firstOrNull() is IntVectorWithValue<*> && (positionsToDraw.first() as IntVectorWithValue<*>).value is Char)
        @Suppress("UNCHECKED_CAST") // actually not an unchecked cast
        return dumpGridToStringWithValues(positionsToDraw as Collection<IntVectorWithValue<Char>>)
    val minX = positionsToDraw.minOf { it.x }
    val maxX = positionsToDraw.maxOf { it.x }
    val minY = positionsToDraw.minOf { it.y }
    val maxY = positionsToDraw.maxOf { it.y }

    val xOffset = minX
    val yOfffset = minY

    val map = Array(maxY - minY + 1){
        CharArray(maxX - minX + 1){ '.' }
    }

    positionsToDraw.forEach{ v ->
        map[v.y-yOfffset][v.x-xOffset] = '#'
    }

    return map.joinToString("\n"){ it.joinToString("")}
}

private fun dumpGridToStringWithValues(positionsToDraw: Collection<IntVectorWithValue<Char>>): String{
    val minX = positionsToDraw.minOf { it.x }
    val maxX = positionsToDraw.maxOf { it.x }
    val minY = positionsToDraw.minOf { it.y }
    val maxY = positionsToDraw.maxOf { it.y }

    val xOffset = minX
    val yOfffset = minY

    val map = Array(maxY - minY + 1){
        CharArray(maxX - minX + 1){ '.' }
    }

    positionsToDraw.forEach{ v ->
        map[v.y-yOfffset][v.x-xOffset] = v.value
    }

    return map.joinToString("\n"){ it.joinToString("")}
}