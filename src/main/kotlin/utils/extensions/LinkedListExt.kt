package nl.joozd.utils.extensions

import java.util.LinkedList

/**
 * Replace the first value that fulfills [predicate] with the value calculated from [newValue]
 * @return the item that was replaced, or null if no element was replaced
 */
fun <T> LinkedList<T>.replaceFirstWhere(newValue: (T) -> T, predicate: (T) -> Boolean): T?{
    with(listIterator()){
        while(hasNext()){
            val element = next()
            if(predicate(element)){
                set(newValue(element))
                return element
            }
        }
        return null
    }
}