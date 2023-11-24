package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.formats.Instruction23x

class DexPatchInstruction23x(opcode: Opcode, rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(opcode, rawInstruction, dexFile), Instruction23x {
    override fun getRegisterA(): Int {
        return rawInstruction[1].toUByte().toInt()
    }

    override fun getRegisterB(): Int {
        return rawInstruction[2].toUByte().toInt()
    }

    override fun getRegisterC(): Int {
        return rawInstruction[3].toUByte().toInt()
    }
}