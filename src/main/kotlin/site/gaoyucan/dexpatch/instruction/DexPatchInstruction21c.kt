package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.reference.Reference
import site.gaoyucan.ext.readUshort

class DexPatchInstruction21c(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction21c {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getReference(): Reference {
        val referenceIndex = rawInstruction.readUshort(2)
        return DexBackedReference.makeReference(dexFile, referenceType, referenceIndex)
    }

    override fun getReferenceType(): Int {
        return opcode.referenceType
    }
}