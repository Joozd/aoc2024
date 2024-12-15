package nl.joozd.days

import nl.joozd.utils.imaging.ColorMaker
import nl.joozd.utils.imaging.GifSequenceBuilder
import nl.joozd.utils.linearalgebra.IntVector
import nl.joozd.utils.linearalgebra.IntVectorWithValue
import kotlin.time.Duration.Companion.milliseconds

class Day15(isTest: Boolean = false) : Day(15, isTest) {
    private val splitInput = input.split("\n\n")
    private val map = splitInput.first().lines()
    private val moves = splitInput.last().lines().joinToString("")

    override fun first(): Long {
        val boxes: MutableSet<IntVector> = makeBoxesSet(map)
        val walls: Set<IntVector> = makeWallsSet(map)
        var robotPos = getRobotPos(map)

        for (move in moves) {
            val direction = directionsMap[move] ?: error ("Invalid move $move")
            val nextPos = robotPos + direction
            when (nextPos) {
                in walls -> continue // do nothing
                !in boxes -> robotPos = nextPos
                else -> if (nextPos.moveBox(direction, boxes, walls)){
                    robotPos = nextPos
                    boxes.remove(nextPos)
                } // no else for this if, no move means do nothing
            }
        }
        return scoreBoxes(boxes)
    }

    /**
     * all boxes and walls  are 2 wide. This means:
     *  - moving left and right must check two ahead instead of one.
     *  - moving up and down must check NW, N and NE
     *  (NW: left side of this box pushes right side of above, N: aligned, NE: right side of this box pushes left side of above)
     * Robot must do slightly different:
     *  - left: look 2 ahead
     *  - right: look 1 ahead
     *  - up: check NW, N
     *  - down: check SW, S
     */
    override fun second(): Long {
        val gifSequenceBuilder = GifSequenceBuilder<Char>(delay = 16.milliseconds, loop = false, scale = 2, colorMaker = colorMaker)
        val boxes: MutableSet<IntVector> = makeBoxesSet(map, double = true)
        val walls: Set<IntVector> = makeWallsSet(map, double = true)
        var robotPos = getRobotPos(map, double = true)

        var movesCounter = 0
        for (move in moves) {
            if(100*movesCounter++ % moves.length == 0) println(100* movesCounter / moves.length)
            val direction = directionsMap[move] ?: error ("Invalid move $move")
            val nextPos = robotPos + direction
            when {
                nextPos in walls -> continue // do nothing
                getConflictingBox(nextPos, boxes) == null -> robotPos = nextPos
                else -> {
                    val conflictingBox = getConflictingBox(nextPos, boxes)!!
                    if(!conflictingBox.moveWideBox(direction, boxes, walls, doMove = false)) continue // cannot move, boxes blocked
                    conflictingBox.moveWideBox(direction, boxes, walls) // we can move, so do move
                    robotPos = nextPos
                    boxes.remove(conflictingBox)
                }
            }
            if(movesCounter %4 == 0)
            gifSequenceBuilder.addCoordinates( makeMapVectors(boxes, walls, robotPos) )
            // println(dumpGridToString(makeMapVectors(boxes, walls, robotPos)))
        }

        gifSequenceBuilder.writeGif("c:\\temp\\aoc_2024_15.gif")
        return scoreBoxes(boxes)
    }

    private fun getConflictingBox(nextPos: IntVector, boxes: Set<IntVector>): IntVector? = boxes.firstOrNull {
        it == nextPos || it + IntVector.EAST == nextPos
    }


    /**
     * Get the positions of all boxes on the map
     * @param double use double sized spacing for part 2
     * @return a Set with all positions of all boxes
     * NOTE: If using double sized spacing, only left parts of boxes will be in set.
     */
    private fun makeBoxesSet(map: List<String>, double: Boolean = false) =
        HashSet<IntVector>().apply{
            map.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c == 'O') {
                        if (double) {
                            this.add(IntVector(2 * x, y))
                        } else this.add(IntVector(x, y))
                    }
                }
            }
        }

    /**
     * Get the positions of all walls on the map
     * @param double use double sized spacing for part 2
     * @return a Set with all positions of all walls
     */
    private fun makeWallsSet(map: List<String>, double: Boolean = false) = buildSet<IntVector> {
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#')
                    if (double) {
                        this.add(IntVector(2 * x, y))
                        this.add(IntVector(2 * x + 1, y))
                    } else this.add(IntVector(x, y))
            }
        }
    }

    /**
     * Get the position of the robot from the input map
     * @param double use double sized spacing for part 2
     * @return the position of the robot
     */
    private fun getRobotPos(map: List<String>, double: Boolean = false): IntVector{
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '@') return if (double) IntVector(2* x, y) else IntVector(x, y)
            }
        }
        throw(IllegalStateException("No robot in map!"))
    }

    /**
     * Move a box in the direction of [direction]
     * If there is already a box there, attempt to move that box as well.
     * If there is a wall there, return false
     * This function is _NOT_ responsible for clearing vacated space. The robot does that.
     */
    private fun IntVector.moveBox(direction: IntVector, boxes: MutableSet<IntVector>, walls: Set<IntVector>): Boolean{
        val nextPos = this + direction
        if (nextPos in walls) return false
        if (nextPos in boxes && !nextPos.moveBox(direction, boxes, walls)) return false
        boxes.add(nextPos) // if there was a nextBox, it will be overwritten. The only space that needs to be cleared it this one, robot must take care of that.
        return true
    }

    /**
     * Move a box in the direction of [direction]
     * If there is already a box there, attempt to move that box as well.
     * If there is a wall there, return false
     * This function is _PARTIALLY_ responsible for clearing vacated space. The robot does the rest.
     *
     * This is the version that takes care of double wide boxes.
     * A double wide box is defined by its left part, we have to check if we conflict with its empty right part.
     * For simplicity, I have chopped this up in directions:
     *  - East: empty part opf this box can run into walls or other boxes
     *  - West: This box can run into walls or empty parts of other boxes
     *  - North/South: both main and empty part of this box can run into a wall or other box, main part can run into an empty part.
     * @param doMove if false, behaviour is simulated but not executed, to check if a move can be done. If true, moves are done.
     * @return true if a move can be done, false if not.
     */
    private fun IntVector.moveWideBox(direction: IntVector, boxes: MutableSet<IntVector>, walls: Set<IntVector>, doMove: Boolean = true): Boolean {
        val nextPos = this + direction
        val lookAheadPos = nextPos + direction

        return when(direction) {
            IntVector.WEST -> {
                if (nextPos in walls) return false
                if (lookAheadPos in boxes && !lookAheadPos.moveWideBox(direction, boxes, walls, doMove)) return false // this moves next box if it can be moved!
                if(doMove) {
                    boxes.add(nextPos)
                    boxes.remove(lookAheadPos) // the box that was in that pos has moved one space west and its extra part needs to be cleared
                }
                true
            }

            IntVector.EAST -> {
                if (lookAheadPos in walls) return false // extra space will hit wall
                if (lookAheadPos in boxes && !lookAheadPos.moveWideBox(direction, boxes, walls)) return false // this moves next box if it can be moved!
                if(doMove) {
                    boxes.add(nextPos)
                    boxes.remove(lookAheadPos) // the box that was in that pos has moved one east and this box's extra part needs to be cleared
                }
                true
            }
            IntVector.NORTH, IntVector.SOUTH -> {
                val ePos = nextPos + IntVector.EAST
                val wPos = nextPos + IntVector.WEST
                if (nextPos in walls || ePos in walls) return false // box or extra hits wall
                val targets = listOf(nextPos, ePos, wPos)
                if (!targets.all {
                    it !in boxes || (it.moveWideBox(direction, boxes, walls, doMove = false)) // only check, do not move
                }) return false
                // If we get here, we can move. If [doMove], move all boxes that are in the way, clear space and move this box
                if(doMove) {
                    for (target in targets) {
                        if (target in boxes) {
                            target.moveWideBox(direction, boxes, walls)
                            boxes.remove(target)
                        }
                    }
                    boxes.add(nextPos)
                }
                return true
            }
            else -> error ("Bad direction: $direction")
        }

    }

    /**
     * Get the score of all boxes
     */
    private fun scoreBoxes(boxes: Set<IntVector>): Long = boxes.sumOf { it.scoreBox() }

    /**
     * Get the score of a single box
     */
    private fun IntVector.scoreBox(): Long =
        (100*y + x).toLong()

    // map vectors to vectorsWithValue so they can be plotted for debugging
    private fun makeMapVectors(boxes: Set<IntVector>, walls: Set<IntVector>, robotPos: IntVector): List<IntVectorWithValue<Char>> =
        boxes.map { IntVectorWithValue<Char>(it, value = '[') } +
        boxes.map { IntVectorWithValue<Char>(it.x+1, it.y, value = ']') } +
            walls.map { IntVectorWithValue<Char>(it, value = '#') } +
            setOf(IntVectorWithValue(robotPos, value = '@'))

    private val colorMaker = ColorMaker<Char>{ c ->
        when(c){
            '#' -> 0xFFFFFF
            '[', ']' -> 0xFF991C
            '@' -> 0xFF0000
            else -> 0x000000
        }
    }


    companion object{
        val directionsMap = mapOf(
            '<' to IntVector.WEST,
            '^' to IntVector.NORTH,
            '>' to IntVector.EAST,
            'v' to IntVector.SOUTH
        )
    }
}