package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction22b

class DexPatchInstruction22b(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction22b {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getRegisterB(): Int {
        return rawInstruction[2].toUByte().toInt()
    }

    override fun getWideLiteral(): Long {
        return narrowLiteral.toLong()
    }

    override fun getNarrowLiteral(): Int {
        return rawInstruction[3].toInt()
    }
}