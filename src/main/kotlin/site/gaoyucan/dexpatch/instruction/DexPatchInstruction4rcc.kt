package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc
import org.jf.dexlib2.iface.reference.Reference
import site.gaoyucan.ext.readUshort

class DexPatchInstruction4rcc(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction4rcc {
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

    override fun getReference2(): Reference {
        return DexBackedReference.makeReference(dexFile, referenceType2, rawInstruction.readUshort(6))
    }

    override fun getReferenceType2(): Int {
        return opcode.referenceType
    }
}