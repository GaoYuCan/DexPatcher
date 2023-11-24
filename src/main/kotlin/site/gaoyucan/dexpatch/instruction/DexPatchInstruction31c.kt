package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction31c
import org.jf.dexlib2.iface.reference.Reference
import org.jf.util.ExceptionWithContext
import site.gaoyucan.ext.readSmallUint

class DexPatchInstruction31c(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction31c {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getReference(): Reference {
        return DexBackedReference.makeReference(dexFile, referenceType, rawInstruction.readSmallUint(2))
    }

    override fun getReferenceType(): Int {
        return opcode.referenceType
    }
}
