package nl.joozd.utils.linearalgebra

class IntVectorWithValue<T>(vararg positions: Int, comparator: IntVectorComparator = standardComparator, val value: T): IntVector(*positions, comparator = comparator) {
    constructor(vector: IntVector, comparator: IntVectorComparator = standardComparator, value: T): this(*vector.vector, comparator = comparator, value = value)
}