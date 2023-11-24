package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih
import site.gaoyucan.ext.readShort

class DexPatchInstruction21ih(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction21ih {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getHatLiteral(): Short {
        return rawInstruction.readShort(2)
    }

    override fun getWideLiteral(): Long {
        return narrowLiteral.toLong()
    }

    override fun getNarrowLiteral(): Int {
        return hatLiteral.toInt() shl 16
    }
}