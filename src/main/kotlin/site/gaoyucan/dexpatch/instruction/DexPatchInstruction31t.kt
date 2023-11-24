package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction31t
import site.gaoyucan.ext.readInt

class DexPatchInstruction31t(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction31t {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getCodeOffset(): Int {
        return rawInstruction.readInt(2)
    }
}