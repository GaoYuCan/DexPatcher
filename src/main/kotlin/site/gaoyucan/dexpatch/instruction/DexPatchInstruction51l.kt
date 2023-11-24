package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction51l
import site.gaoyucan.ext.readLong

class DexPatchInstruction51l(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction51l {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getWideLiteral(): Long {
        return rawInstruction.readLong(2)
    }
}