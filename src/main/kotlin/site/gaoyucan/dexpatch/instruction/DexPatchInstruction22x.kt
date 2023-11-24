package site.gaoyucan.dexpatch.instruction

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction22x
import site.gaoyucan.ext.readUshort

class DexPatchInstruction22x(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction22x {
    override fun getRegisterA(): Int {
      return rawInstruction[1].toUByte().toInt()
    }

    override fun getRegisterB(): Int {
        return rawInstruction.readUshort(2)
    }
}