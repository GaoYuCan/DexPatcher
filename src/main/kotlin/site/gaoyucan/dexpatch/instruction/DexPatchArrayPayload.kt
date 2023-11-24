package site.gaoyucan.dexpatch.instruction

import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.util.FixedSizeList
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload
import org.jf.util.ExceptionWithContext

class DexPatchArrayPayload(rawInstruction: ByteArray, dexFile: DexBackedDexFile) :
    DexPatchInstruction(Opcode.ARRAY_PAYLOAD, rawInstruction, dexFile), ArrayPayload {
    private val elementWidth: Int
    val elementCount: Int

    init {
        val localElementWidth = rawInstruction.readUshort(ELEMENT_WIDTH_OFFSET)
        if (localElementWidth == 0) {
            elementWidth = 1
            elementCount = 0
        } else {
            elementWidth = localElementWidth
            elementCount = rawInstruction.readSmallUint(ELEMENT_COUNT_OFFSET)
            if (elementWidth.toLong() * elementCount > Int.MAX_VALUE) {
                throw ExceptionWithContext("Invalid array-payload instruction: element width*count overflows")
            }
        }
    }

    override fun getElementWidth(): Int {
        return elementWidth
    }

    override fun getArrayElements(): MutableList<Number> {

        abstract class ReturnedList : FixedSizeList<Number>() {
            override val size: Int
                get() = elementCount
        }

        if (elementCount == 0) {
            return mutableListOf()
        }
        return when (elementWidth) {
            1 -> object : ReturnedList() {
                override fun readItem(offset: Int): Number {
                    return rawInstruction[ELEMENTS_OFFSET + offset]
                }
            }
            2 -> object : ReturnedList() {
                override fun readItem(offset: Int): Number {
                    return rawInstruction.readShort(ELEMENTS_OFFSET + offset * 2)
                }
            }
            4 -> object : ReturnedList() {
                override fun readItem(offset: Int): Number {
                    return rawInstruction.readInt(ELEMENTS_OFFSET + offset * 4)
                }
            }
            8 -> object : ReturnedList() {
                override fun readItem(offset: Int): Number {
                    return rawInstruction.readLong(ELEMENTS_OFFSET + offset * 8)
                }
            }
            else -> throw ExceptionWithContext("Invalid element width: %d", elementWidth)
        }
    }

    override fun getCodeUnits(): Int {
        return 4 + (elementWidth * elementCount + 1) / 2
    }

    companion object {
        private const val ELEMENT_WIDTH_OFFSET = 2
        private const val ELEMENT_COUNT_OFFSET = 4
        private const val ELEMENTS_OFFSET = 8
    }
}