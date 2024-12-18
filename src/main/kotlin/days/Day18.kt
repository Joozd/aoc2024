package nl.joozd.days

import nl.joozd.utils.BasicDijkstra
import nl.joozd.utils.getNumbersFromLine
import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.IntVectorRange

/**
 * Not super fast/efficient, but runs in O(n) for part 2 so good enough for me.
 * Could go O(log n) when doing binary search
 */
class Day18(isTest: Boolean = false) : Day(18, isTest) {
    // lots of text, this parses (12,34) to IntVector (12,34). Produces a List.
    val badBytes = input.lines().map { getNumbersFromLine(it).map{it.toInt()}.iterator().let { numbers -> IntVector(numbers.next(), numbers.next()) } }
    val size = if (isTest) 6 else 70
    val bytesToTake = if (isTest) 12 else 1024
    val mazeRange = makeRange(size)

    override fun first(): Long {
        val first1K = badBytes.take(bytesToTake).toSet()
        val dijkstra = BasicDijkstra(mazeRange){ pos, _ -> pos !in first1K }
        return dijkstra.getshortestDistance(IntVector(0,0), IntVector(size,size))?.toLong()!!
    }

    override fun second(): String {
        val indexOfFirstBlocking = getNumberOfBadBytesRequiredToBLock() - 1
        return badBytes[indexOfFirstBlocking].vector.joinToString(",")

    }

    /**
     * Binary search for index of first blocking.
     * First blocking is the first one blocking where the one below is not blocking
     */
    private tailrec fun getNumberOfBadBytesRequiredToBLock(left: Int = bytesToTake, right: Int = badBytes.size): Int{ // we can start at bytesToTake since that worked for 1
        val middle = (left + right) / 2
        val blocked = badBytes.take(middle).toSet()
        val dijkstra = BasicDijkstra(mazeRange){ pos, _ -> pos !in blocked }
        val canDoMaze = dijkstra.getshortestDistance(IntVector(0,0), IntVector(size,size)) != null


        // check if we found it:
        val oneLessBlocked = badBytes.take(middle - 1).toSet()
        val d2 = BasicDijkstra(mazeRange){ pos, _ -> pos !in oneLessBlocked }

        // base case
        if(!canDoMaze && d2.getshortestDistance(IntVector(0,0), IntVector(size,size)) != null)
            return middle

        // too far right
        return if (!canDoMaze)
            getNumberOfBadBytesRequiredToBLock(left, middle - 1)
        else getNumberOfBadBytesRequiredToBLock(middle + 1, right)


    }

    private fun makeRange(size: Int): IntVectorRange =
        IntVector(0,0)..IntVector(size, size)
}