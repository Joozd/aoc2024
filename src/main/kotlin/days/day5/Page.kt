package nl.joozd.days.day5

class Page(
    val value: Int,
    private val lowerThan: Map<Int, Set<Int>>, // all items in a set are lower than the key
    private val higherThan: Map<Int, Set<Int>> // all items in a set are higher than the key
): Comparable<Page> {
    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: Page) = when{
        other.value in (lowerThan[this.value] ?: emptySet()) -> 1
        other.value in (higherThan[this.value] ?: emptySet()) -> -1
        else -> 0
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "Page($value)"
}