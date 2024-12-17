package nl.joozd.days.day17

import nl.joozd.utils.getNumbersFromLine

/**
 * Bitwise manipulation seemed fitting!
 * Twiddle them bitties!
 */
class Computer private constructor(private var regA: Long, private var regB: Long, private var regC: Long, val program: List<Int>) {
    private val output = ArrayList<Int>(program.size)

    fun output() = output.joinToString(",")

    private val maxPointer = program.indices.last - 1
    private var pointer = 0
    private val functions = listOf<(Int) -> Unit>(
        ::adv, // 0
        ::bxl, // 1
        ::bst, // 2
        ::jnz, // 3
        ::bxc, // 4
        ::out, // 5
        ::bdv, // 6
        ::cdv  // 7
    )

    /**
     * Run the program by 1 tick.
     * @return true if program still running, false if halted
     */
    fun tick(): Boolean{
        if(pointer > maxPointer) return false
        //print("($regA, $regB, $regC), ${program[pointer]}(${program[pointer+1]}) // ")

        val opcode = program[pointer]
        val operand = program[pointer + 1]
        val instruction = functions[opcode]

        instruction(operand)
        pointer += 2
        return true
    }

    /**
     * The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
     * The denominator is found by raising 2 to the power of the instruction's combo operand.
     * (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
     * The result of the division operation is truncated to an integer and then written to the A register.
     */
    private fun adv(comboOperand: Int){
        val op = getComboOperand(comboOperand)
        regA = regA.shr(op.toInt())
    }

    /**
     * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and
     * the instruction's literal operand, then stores the result in register B.
     */
    private fun bxl(op: Int){
        regB = regB.xor(op.toLong())
    }

    /**
     * The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
     * (thereby keeping only its lowest 3 bits), then writes that value to the B register
     */
    private fun bst(comboOperand: Int){
        val op = getComboOperand(comboOperand)
        regB = op.and(7L)
    }

    /**
     * The jnz instruction (opcode 3) does nothing if the A register is 0.
     * However, if the A register is not zero, it jumps by setting the instruction
     * pointer to the value of its literal operand; if this instruction jumps,
     * the instruction pointer is not increased by 2 after this instruction.
     */
    private fun jnz(op: Int){
        if(regA != 0L)
            pointer = op - 2 // gets increased by 2 after instruction is done
    }

    /**
     * The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C,
     * then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)
     */
    private fun bxc(
        @Suppress("unused") // It needs to be here becaus e the siugnature needs to be (Int) -> Unit, see specifications.
        op: Int
    ){ // op is ignored
        if(regA != 0L)
            regB = regB.xor(regC)
    }

    /**
     * The out instruction (opcode 5) calculates the value of its combo operand modulo 8,
     * then outputs that value. (If a program outputs multiple values, they are separated by commas.)
     * NOTE: Not separating by commas. Hah!
     */
    private fun out(comboOperand: Int){
        output.add(getComboOperand(comboOperand).toInt().and(7))
    }

    /**
     * The bdv instruction (opcode 6) works exactly like the adv instruction except that
     * the result is stored in the B register. (The numerator is still read from the A register.)
     */
    private fun bdv(comboOperand: Int){
        val op = getComboOperand(comboOperand)
        regB = regA.shr(op.toInt())
    }

    /**
     * The cdv instruction (opcode 7) works exactly like the adv instruction except that
     * the result is stored in the C register. (The numerator is still read from the A register.)
     */
    private fun cdv(comboOperand: Int){
        val op = getComboOperand(comboOperand)
        regC = regA.shr(op.toInt())
    }

    private fun getComboOperand(operand: Int): Long = when(operand){
        0,1,2,3 -> operand.toLong()
        4 -> regA
        5 -> regB
        6 -> regC
        7 -> error("Combo operand 7 is reserved and will not appear in valid programs.")
        else -> throw IndexOutOfBoundsException("Combo operand $operand not in range 0..7")
    }

    companion object{
        fun ofInput(input: String): Computer{
            val numberLists = input.lines().filter{ it.isNotBlank() }.map { getNumbersFromLine(it).map { it.toInt()} }
            val regA = numberLists[0].first().toLong()
            val regB = numberLists[1].first().toLong()
            val regC = numberLists[2].first().toLong()
            val program = numberLists[3].toList()

            return Computer(regA, regB, regC, program)
        }
    }
}