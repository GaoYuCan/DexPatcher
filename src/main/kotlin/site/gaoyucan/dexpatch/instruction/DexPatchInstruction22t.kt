package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction22t
import org.jf.util.NibbleUtils
import site.gaoyucan.ext.readShort

class DexPatchInstruction22t(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction22t {
    override fun getRegisterA(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getRegisterB(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getCodeOffset(): Int {
        return rawInstruction.readShort(2).toInt()
    }
}