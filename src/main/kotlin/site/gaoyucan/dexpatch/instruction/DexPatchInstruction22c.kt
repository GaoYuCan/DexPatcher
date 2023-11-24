package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction22c
import org.jf.dexlib2.iface.reference.Reference
import org.jf.util.NibbleUtils
import site.gaoyucan.ext.readUshort

class DexPatchInstruction22c(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction22c {
    override fun getRegisterA(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getRegisterB(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getReference(): Reference {
        val referenceIndex = rawInstruction.readUshort(2)
        return DexBackedReference.makeReference(dexFile, referenceType, referenceIndex)
    }

    override fun getReferenceType(): Int {
        return opcode.referenceType
    }
}