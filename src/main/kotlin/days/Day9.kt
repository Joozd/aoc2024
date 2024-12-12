package nl.joozd.days

import nl.joozd.days.day9.Disk

class Day9(isTest: Boolean = false): Day(9, isTest) {
    /**
     * Let's try doing it like it says.
     * It is probably a lot faster to only calculate instead of actually compact, but we're at 400 us so meh
     */
    override fun first() =
        Disk.fromInput(input)
            .compact()
            .checkSum()

    override fun second() =
        Disk.fromInput(input)
            .compactFitting()
            .checkSum()
}