package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.util.FixedSizeList
import org.jf.dexlib2.iface.instruction.SwitchElement
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload
import site.gaoyucan.ext.readInt
import site.gaoyucan.ext.readUshort

class DexPatchSparseSwitchPayload(rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(Opcode.PACKED_SWITCH_PAYLOAD, rawInstruction, dexFile), SparseSwitchPayload {
    val elementCount: Int

    init {
        elementCount = rawInstruction.readUshort(ELEMENT_COUNT_OFFSET)
    }

    override fun getSwitchElements(): MutableList<out SwitchElement> {
        return object : FixedSizeList<SwitchElement>() {
            override val size: Int
                get() = elementCount

            override fun readItem(offset: Int): SwitchElement {
                return object : SwitchElement {
                    override fun getOffset(): Int {
                        return rawInstruction.readInt(KEYS_OFFSET + elementCount * 4 + offset * 4)
                    }

                    override fun getKey(): Int {
                        return rawInstruction.readInt(KEYS_OFFSET + offset * 4)
                    }
                }
            }
        }
    }

    override fun getCodeUnits(): Int {
        return 2 + elementCount * 4
    }

    companion object {

        private const val ELEMENT_COUNT_OFFSET = 2
        private const val KEYS_OFFSET = 4
    }
}