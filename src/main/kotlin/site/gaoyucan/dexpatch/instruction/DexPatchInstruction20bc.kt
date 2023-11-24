package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.ReferenceType
import org.jf.dexlib2.ReferenceType.InvalidReferenceTypeException
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc
import org.jf.dexlib2.iface.reference.Reference
import org.jf.dexlib2.iface.reference.Reference.InvalidReferenceException
import site.gaoyucan.ext.readUshort

class DexPatchInstruction20bc(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction20bc {

    override fun getVerificationError(): Int {
        return rawInstruction[1].toUByte().toInt() and 0x3f
    }

    override fun getReference(): Reference {
        val referenceIndex = rawInstruction.readUshort(2)
        return try {
            val referenceType = getReferenceType()
            DexBackedReference.makeReference(dexFile, referenceType, referenceIndex)
        } catch (ex: InvalidReferenceTypeException) {
            Reference {
                throw InvalidReferenceException("${ex.referenceType}${referenceIndex}", ex)
            }
        }
    }

    override fun getReferenceType(): Int {
        val referenceType = (rawInstruction[1].toUByte().toInt() ushr 6) + 1
        ReferenceType.validateReferenceType(referenceType)
        return referenceType
    }
}