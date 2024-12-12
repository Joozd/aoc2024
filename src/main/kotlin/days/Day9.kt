package nl.joozd.days

import nl.joozd.days.day9.Disk

class Day9(isTest: Boolean = false): Day(9, isTest) {
    val diskSize = input.map { it.digitToInt() }.sum()

    /**
     * Let's try doing it like it says.
     * It is probably a lot faster to only calculate instead of actually compact, but we're at 400 us so meh
     */
    override fun first() =
        makeDisk()
            .compact()
            .checkSum()

    override fun second(): Long {
        return super.second()
    }

    private fun makeDisk(): Disk{
        val disk = IntArray(diskSize) { Disk.EMPTY }
        var currentIndex = 0
        var currentFileID = 0
        var lookingAtEmpty = false


        input.forEach { c ->
            val digit = c.digitToInt()
            if(lookingAtEmpty)
                currentIndex += digit
            else{
                repeat(digit){
                    disk[currentIndex] = currentFileID
                    currentIndex++
                }
                currentFileID++
            }
            lookingAtEmpty = !lookingAtEmpty
        }
        return Disk(disk)
    }


}