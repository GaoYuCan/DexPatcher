package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc
import org.jf.dexlib2.iface.reference.Reference
import site.gaoyucan.ext.readUshort

class DexPatchInstruction3rc(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction3rc {
    override fun getRegisterCount(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getStartRegister(): Int {
       return rawInstruction.readUshort(4)
    }

    override fun getReference(): Reference {
       return DexBackedReference.makeReference(dexFile, referenceType, rawInstruction.readUshort(2))
    }

    override fun getReferenceType(): Int {
        return opcode.referenceType
    }
}