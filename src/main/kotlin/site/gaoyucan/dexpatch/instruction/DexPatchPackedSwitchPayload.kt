package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.util.FixedSizeList
import org.jf.dexlib2.iface.instruction.SwitchElement
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload
import site.gaoyucan.ext.readInt
import site.gaoyucan.ext.readUshort

class DexPatchPackedSwitchPayload(rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(Opcode.PACKED_SWITCH_PAYLOAD, rawInstruction, dexFile), PackedSwitchPayload {

    val elementCount: Int

    init {
        elementCount = rawInstruction.readUshort(ELEMENT_COUNT_OFFSET)
    }

    override fun getSwitchElements(): MutableList<out SwitchElement> {
        val firstKey = rawInstruction.readInt(FIRST_KEY_OFFSET)
        return object : FixedSizeList<SwitchElement>() {
            override val size: Int
                get() = elementCount

            override fun readItem(offset: Int): SwitchElement {
                return object : SwitchElement {
                    override fun getOffset(): Int {
                        return rawInstruction.readInt(TARGETS_OFFSET + offset * 4)
                    }

                    override fun getKey(): Int {
                        return firstKey + offset
                    }
                }
            }
        }
    }

    override fun getCodeUnits(): Int {
        return 4 + elementCount * 2
    }

    companion object {
        private const val ELEMENT_COUNT_OFFSET = 2
        private const val FIRST_KEY_OFFSET = 4
        private const val TARGETS_OFFSET = 8
    }
}