package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.reference.DexBackedReference
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc
import org.jf.dexlib2.iface.reference.Reference
import org.jf.util.NibbleUtils
import site.gaoyucan.ext.readUshort

class DexPatchInstruction45cc(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction45cc {
    override fun getRegisterCount(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getRegisterC(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[4].toInt())
    }

    override fun getRegisterD(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[4].toInt())
    }

    override fun getRegisterE(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[5].toInt())
    }

    override fun getRegisterF(): Int {
        return NibbleUtils.extractHighUnsignedNibble(rawInstruction[5].toInt())
    }

    override fun getRegisterG(): Int {
        return NibbleUtils.extractLowUnsignedNibble(rawInstruction[1].toInt())
    }

    override fun getReference(): Reference {
        return  DexBackedReference.makeReference(dexFile, referenceType, rawInstruction.readUshort(2))
    }

    override fun getReferenceType(): Int {
        return opcode.referenceType
    }

    override fun getReference2(): Reference {
        return DexBackedReference.makeReference(dexFile, referenceType2, rawInstruction.readUshort(6))
    }

    override fun getReferenceType2(): Int {
        return opcode.referenceType2
    }
}