package nl.joozd.utils.extensions

/**
 * Make a list of all 1-on-1 combinations in a Collection. Keeps the order if the collection is ordered.
 * Like a "halve competitie"
 */
fun <T> List<T>.combinations(): List<Pair<T, T>> = buildList{
    val l = this@combinations
    for (firstIndex in 0 until l.indices.last) {
        for (lastIndex in (firstIndex+1)..l.indices.last){
            add(this@combinations[firstIndex] to this@combinations[lastIndex])
        }
    }
}