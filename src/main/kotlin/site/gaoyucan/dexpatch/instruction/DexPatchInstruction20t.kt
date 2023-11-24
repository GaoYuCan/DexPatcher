package site.gaoyucan.dexpatch.instruction;

import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t
import site.gaoyucan.ext.readUshort

class DexPatchInstruction20t(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction20t {

    override fun getCodeOffset(): Int {
        return rawInstruction.readUshort(2)
    }
}
