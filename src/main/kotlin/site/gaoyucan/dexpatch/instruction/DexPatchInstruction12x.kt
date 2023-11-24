package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction12x
import org.jf.util.NibbleUtils

class DexPatchInstruction12x(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction12x {

    override fun getRegisterA(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getRegisterB(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[1].toInt())
    }
}