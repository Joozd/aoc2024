package nl.joozd.utils

import java.util.Queue

class QueueSet<T>: Queue<T> {
    private val elements = LinkedHashSet<T>()

    override fun add(element: T): Boolean =
        elements.add(element)

    /**
     * Adds all the elements of the specified collection to this collection.
     *
     * @return `true` if any of the specified elements was added to the collection, `false` if the collection was not modified.
     */
    override fun addAll(elements: Collection<T>): Boolean =
        this.elements.addAll(elements)

    /**
     * Removes all elements from this collection.
     */
    override fun clear() =
        elements.clear()

    override fun iterator(): MutableIterator<T> = elements.iterator()

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from [poll()][.poll] only in that it throws an exception if
     * this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    override fun remove(): T = elements.first().also{
        elements.remove(it)
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present.
     *
     * @return `true` if the element has been successfully removed; `false` if it was not present in the collection.
     */
    override fun remove(element: T): Boolean = elements.remove(element)

    /**
     * Removes all of this collection's elements that are also contained in the specified collection.
     *
     * @return `true` if any of the specified elements was removed from the collection, `false` if the collection was not modified.
     */
    override fun removeAll(elements: Collection<T>): Boolean =
        this.elements.removeAll(elements.toSet())

    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     *
     * @return `true` if any element was removed from the collection, `false` if the collection was not modified.
     */
    override fun retainAll(elements: Collection<T>): Boolean =
        this.elements.retainAll(elements.toSet())

    /**
     * Checks if the specified element is contained in this collection.
     */
    override fun contains(element: T): Boolean =
        elements.contains(element)

    /**
     * Checks if all elements in the specified collection are contained in this collection.
     */
    override fun containsAll(elements: Collection<T>): Boolean =
        this.elements.containsAll(elements)

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to [.add], which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e the element to add
     * @return `true` if the element was added to this queue, else
     * `false`
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     * this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this queue
     */
    override fun offer(e: T): Boolean =
        elements.add(e) // [elements] is not capacity restricted

    /**
     * Retrieves and removes the head of this queue,
     * or returns `null` if this queue is empty.
     *
     * @return the head of this queue, or `null` if this queue is empty
     */
    override fun poll(): T? = elements.firstOrNull()?.also{
        elements.remove(it)
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from [peek][.peek] only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    override fun element(): T = elements.first()

    /**
     * Returns the size of the collection.
     */
    override val size: Int
        get() = elements.size


    override fun peek(): T? = elements.firstOrNull()

    override fun isEmpty(): Boolean = elements.isEmpty()
}