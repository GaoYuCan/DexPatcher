package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction11x

class DexPatchInstruction11x(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction11x {
    override fun getRegisterA(): Int {
       return rawInstruction[1].toInt()
    }
}