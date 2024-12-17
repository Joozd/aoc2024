package nl.joozd.days

import nl.joozd.days.day17.Computer

class Day17(isTest: Boolean = false) : Day(17, isTest) {
    /**
     * I could have just hardcoded the program, but where's the fun in that?
     * So, I made a [Computer]!
     */
    override fun first(): Long {
        val computer = Computer.ofInput(input)
        while(computer.tick()){
            // do nothing
        }
        println("${computer.output()}\n-=-\n\n")
        return -1
    }

    /**
     * I did hardcode the program for this, for SPEED.
     */
    override fun second(): Long {
        val prototypeComputer = Computer.ofInput(input)
        return findLowestSelfReproducing(prototypeComputer.program, 0)!!.toLong()
    }

    /**
     * Build an input from [program] that makle the computer output the program
     */
    private fun findLowestSelfReproducing(program: List<Int>, currentA: Long): Long?{
        if (program.size == 1) {
            val last3Bits = (0..7).firstOrNull { works(currentA, it, program.first()) } ?: return null // base case; last 3 bits
            println("DONE! $program $currentA")
            return currentA.shl(3) + last3Bits
        }
        val validCandidates = (0..7).filter { works(currentA, it, program.last())} // candidate for 3 bits to append to currentA
        return validCandidates.firstNotNullOfOrNull { last3Bits ->
            val newCurrentA = currentA.shl(3) + last3Bits
            findLowestSelfReproducing(program.dropLast(1), newCurrentA)
        }.also{
            println("Found solution $it for $program")
        }
    }

    /**
     * Check if [candidate] can be appended to [currentRegA] to produce [neededValue]
     */
    private fun works(currentRegA: Long, candidate: Int, neededValue: Int): Boolean{
        val a = currentRegA.shl(3) + candidate // insert candidate behind currentRegA
        var b = a.and(7).xor(2).toInt() // lowest 3 bits, bit 2 flipped
        val c = a.shr(b.toInt()).and(7).toInt() // shift a right by b bits, take last 3.
        b = b.xor(c).xor(7) // xor b with c and then invert.
        return b == neededValue
    }
}