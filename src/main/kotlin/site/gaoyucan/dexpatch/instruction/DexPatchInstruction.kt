package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Format
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.instruction.Instruction

abstract class DexPatchInstruction(
    private val opcode: Opcode,
    val rawInstruction: ByteArray,
    val dexFile: DexBackedDexFile
) :
    Instruction {
    override fun getCodeUnits() = opcode.format.size / 2
    override fun getOpcode() = opcode

    companion object {
        fun buildInstruction(opcode: Opcode, raw: ByteArray, dexFile: DexBackedDexFile): DexPatchInstruction {
            return when (opcode.format) {
                Format.Format10t -> DexPatchInstruction10t(opcode, raw, dexFile)
                Format.Format10x -> DexPatchInstruction10x(opcode, raw, dexFile)
                Format.Format11n -> DexPatchInstruction11n(opcode, raw, dexFile)
                Format.Format11x -> DexPatchInstruction11x(opcode, raw, dexFile)
                Format.Format12x -> DexPatchInstruction12x(opcode, raw, dexFile)
                Format.Format20bc -> DexPatchInstruction20bc(opcode, raw, dexFile)
                Format.Format20t -> DexPatchInstruction20t(opcode, raw, dexFile)
                Format.Format21c -> DexPatchInstruction21c(opcode, raw, dexFile)
                Format.Format21ih -> DexPatchInstruction21ih(opcode, raw, dexFile)
                Format.Format21lh -> DexPatchInstruction21lh(opcode, raw, dexFile)
                Format.Format21s -> DexPatchInstruction21s(opcode, raw, dexFile)
                Format.Format21t -> DexPatchInstruction21t(opcode, raw, dexFile)
                Format.Format22b -> DexPatchInstruction22b(opcode, raw, dexFile)
                Format.Format22c -> DexPatchInstruction22c(opcode, raw, dexFile)
                Format.Format22cs -> DexPatchInstruction22cs(opcode, raw, dexFile)
                Format.Format22s -> DexPatchInstruction22s(opcode, raw, dexFile)
                Format.Format22t -> DexPatchInstruction22t(opcode, raw, dexFile)
                Format.Format22x -> DexPatchInstruction22x(opcode, raw, dexFile)
                Format.Format23x -> DexPatchInstruction23x(opcode, raw, dexFile)
                Format.Format30t -> DexPatchInstruction30t(opcode, raw, dexFile)
                Format.Format31c -> DexPatchInstruction31c(opcode, raw, dexFile)
                Format.Format31i -> DexPatchInstruction31i(opcode, raw, dexFile)
                Format.Format31t -> DexPatchInstruction31t(opcode, raw, dexFile)
                Format.Format32x -> DexPatchInstruction32x(opcode, raw, dexFile)
                Format.Format35c -> DexPatchInstruction35c(opcode, raw, dexFile)
                Format.Format35mi -> DexPatchInstruction35mi(opcode, raw, dexFile)
                Format.Format35ms -> DexPatchInstruction35ms(opcode, raw, dexFile)
                Format.Format3rc -> DexPatchInstruction3rc(opcode, raw, dexFile)
                Format.Format3rmi -> DexPatchInstruction3rmi(opcode, raw, dexFile)
                Format.Format3rms -> DexPatchInstruction3rms(opcode, raw, dexFile)
                Format.Format45cc -> DexPatchInstruction45cc(opcode, raw, dexFile)
                Format.Format4rcc -> DexPatchInstruction4rcc(opcode, raw, dexFile)
                Format.Format51l -> DexPatchInstruction51l(opcode, raw, dexFile)
                Format.ArrayPayload -> DexPatchArrayPayload(raw, dexFile)
                Format.PackedSwitchPayload -> DexPatchPackedSwitchPayload(raw, dexFile)
                Format.SparseSwitchPayload -> DexPatchSparseSwitchPayload(raw, dexFile)
                else -> throw IllegalArgumentException("Unsupported opcode format: ${opcode.format}")
            }
        }
    }
}