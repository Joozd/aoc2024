package nl.joozd.utils

class Counter() {
    private var _counts: Int = 0

    fun count(){
        _counts++
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = _counts.toString()
}