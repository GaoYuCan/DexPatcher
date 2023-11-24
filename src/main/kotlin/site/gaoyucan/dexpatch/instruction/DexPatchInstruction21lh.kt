package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh
import site.gaoyucan.ext.readShort

class DexPatchInstruction21lh(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction21lh {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getWideLiteral(): Long {
        return hatLiteral.toLong() shl 48;
    }

    override fun getHatLiteral(): Short {
        return rawInstruction.readShort(2)
    }
}