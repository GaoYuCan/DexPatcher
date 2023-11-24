package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction11n
import org.jf.util.NibbleUtils

class DexPatchInstruction11n(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction11n {

    override fun getRegisterA(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getWideLiteral(): Long {
        return NibbleUtils.extractHighSignedNibble(rawInstruction[1].toInt()).toLong()
    }

    override fun getNarrowLiteral(): Int {
        return wideLiteral.toInt()
    }
}