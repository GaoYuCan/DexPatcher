package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction21s
import site.gaoyucan.ext.readShort

class DexPatchInstruction21s(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction21s
{
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getWideLiteral(): Long {
        return narrowLiteral.toLong()
    }

    override fun getNarrowLiteral(): Int {
        return rawInstruction.readShort(2).toInt()
    }
}