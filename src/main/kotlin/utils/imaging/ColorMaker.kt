package nl.joozd.utils.imaging

/**
 * Generate a color from a CoordinateWithValue
 */
fun interface ColorMaker<T>{
    fun getColor(value: T): Int
}