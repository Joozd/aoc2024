package nl.joozd.days.day9

import nl.joozd.utils.extensions.replaceFirstWhere
import java.util.*

/**
 * @property disk the disk to use
 * @property freeSpaceIndices indices for free space with the sizes; first = size; second = starting index
 */
class Disk private constructor(
    private val disk: IntArray,
    private val freeSpaceIndices: LinkedList<FreeSpace>,
    private val fileIndices: LinkedList<File>
) {
    private var readPos = disk.indices.last
    private var writePos = 0

    fun compact(): Disk = this.apply {
        while(canMove()){
            move()
        }
    }

    fun compactFitting() = this.apply {
        while(fileIndices.isNotEmpty()){ // this way we will visit all files except the first which is already at the beginning
            val file = getFile()

            val i = indexOfFirstFit(file) ?: file.index
            writePos = i
            repeat(file.size) {
                disk[writePos++] = file.content
            }
        }
    }

    fun checkSum(): Long{
        var sum = 0L
        disk.indices.forEach{ i ->
            if(disk[i] != EMPTY) {
                sum += disk[i] * i
            }
        }
        return sum
    }

    override fun toString(): String= disk.joinToString("") {if (it == EMPTY) "." else it.toString() }




    private fun canMove() = writePos < readPos

    private fun move(){
        gotoNextReadPos()
        gotoNextWritePos()
        if (!canMove()) return
        disk[writePos] = disk[readPos]
        disk[readPos] = EMPTY
    }

    private fun gotoNextReadPos(){
        while(EMPTY == disk[readPos]){
            readPos--
        }
    }

    private fun gotoNextWritePos(){
        while(EMPTY != disk[writePos]){
            writePos++
        }
    }

    /**
     * @return a pair of size to index, pointing to the first available large enough spot.
     *  null if no free spot found.
     * HAS SIDE EFFECT: allocates the found space as used.
     */
    private fun indexOfFirstFit(file: File): Int? =
        freeSpaceIndices.replaceFirstWhere(newValue = {s -> s.copy(index = s.index + file.size, size = s.size - file.size)}){ freeSpace ->
            freeSpace.index < file.index &&
                freeSpace.size >= file.size
        }?.index


    /**
     * Removes file from disk and returns it.
     */
    private fun getFile(): File =
        fileIndices.removeLast().also{ file ->
            repeat(file.size){ i ->
                disk[file.index + i] = EMPTY
            }
    }


    private data class File(val content: Int, val size: Int, val index: Int)
    private data class FreeSpace(val index: Int, val size: Int){
        override fun toString() = "(i=$index, s=$size)"
    }

    companion object{
        const val EMPTY = -1

        fun fromInput(input: String): Disk{
            val diskSize = input.map { it.digitToInt() }.sum()
            val disk = IntArray(diskSize) { EMPTY }
            val freeSpaceIndices = LinkedList<FreeSpace>() //I think a LinkedList would be fastest, lets find out
            val fileIndices = LinkedList<File>() //I think a LinkedList would be fastest, lets find out

            var currentIndex = 0
            var currentFileID = 0
            var lookingAtEmpty = false


            input.forEach { c ->
                val digit = c.digitToInt()
                if(lookingAtEmpty) {
                    freeSpaceIndices.add(FreeSpace(currentIndex, digit))
                    currentIndex += digit
                }
                else {
                    val startIndex = currentIndex
                    repeat(digit){
                        disk[currentIndex] = currentFileID
                        currentIndex++
                    }
                    fileIndices.add(File(currentFileID, digit, startIndex))
                    currentFileID++
                }
                lookingAtEmpty = !lookingAtEmpty
            }
            return Disk(disk, freeSpaceIndices, fileIndices)
        }
    }
}