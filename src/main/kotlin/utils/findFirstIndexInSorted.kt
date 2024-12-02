package nl.joozd.utils

tailrec fun findFirstIndexInSorted(
    wantedValue: Long,
    array: LongArray,
    left: Int = 0,
    right: Int = array.size-1
): Int? {
    val current = (left+right)/2
    if(array[current] == wantedValue && (current == 0 || array[current-1] != wantedValue)) return current

    if (left >= right) return null // not found, nowhere else to go

    if(array[current] >= wantedValue) { // next is lower. we are bound by distance to left
        return findFirstIndexInSorted(wantedValue, array, left, current-1)
    }

    return findFirstIndexInSorted(wantedValue, array, current+1, right)

}