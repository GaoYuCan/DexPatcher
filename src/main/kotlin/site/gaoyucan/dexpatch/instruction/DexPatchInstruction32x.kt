package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction32x
import site.gaoyucan.ext.readUshort

class DexPatchInstruction32x(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction32x {
    override fun getRegisterA(): Int {
        return rawInstruction.readUshort(2)
    }

    override fun getRegisterB(): Int {
        return rawInstruction.readUshort(4)
    }
}