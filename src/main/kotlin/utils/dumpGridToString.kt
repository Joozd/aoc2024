package nl.joozd.utils

import nl.joozd.utils.linearalgebra.IntVector

fun dumpGridToString(positionsToDraw: Collection<IntVector>): String{
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