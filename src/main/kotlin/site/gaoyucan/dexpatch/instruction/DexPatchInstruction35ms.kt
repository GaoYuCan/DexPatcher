package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms
import org.jf.util.NibbleUtils
import site.gaoyucan.ext.readUshort

class DexPatchInstruction35ms(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction35ms {

    override fun getRegisterCount(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getRegisterC(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[4].toInt())
    }

    override fun getRegisterD(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[4].toInt())
    }

    override fun getRegisterE(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[5].toInt())
    }

    override fun getRegisterF(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[5].toInt())
    }

    override fun getRegisterG(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getVtableIndex(): Int {
        return rawInstruction.readUshort(2)
    }
}