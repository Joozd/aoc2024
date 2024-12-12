package nl.joozd.days.day9

class Disk(private val disk: IntArray) {
    fun compact(): Disk = this.apply {
        while(canMove()){
            move()
        }
    }

    fun checkSum(): Long{
        var i = 0
        var sum = 0L
        while(disk[i] != EMPTY){
            sum += disk[i] * i++
        }
        return sum
    }

    override fun toString(): String= disk.joinToString("") {if (it == EMPTY) "." else it.toString() }


    private var readPos = disk.indices.last

    private var writePos = 0

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

    companion object{
        const val EMPTY = -1
    }
}