package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction31i
import site.gaoyucan.ext.readInt

class DexPatchInstruction31i(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction31i {
    override fun getRegisterA(): Int {
       return rawInstruction[1].toUByte().toInt()
    }

    override fun getWideLiteral(): Long {
       return narrowLiteral.toLong()
    }

    override fun getNarrowLiteral(): Int {
        return rawInstruction.readInt(2)
    }
}