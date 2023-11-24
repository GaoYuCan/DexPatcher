package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi
import site.gaoyucan.ext.readUshort

class DexPatchInstruction3rmi(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction3rmi {
    override fun getRegisterCount(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getStartRegister(): Int {
        return rawInstruction.readUshort(4)
    }

    override fun getInlineIndex(): Int {
        return rawInstruction.readUshort(2)
    }
}