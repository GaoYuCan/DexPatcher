package site.gaoyucan.ext

import org.jf.util.ExceptionWithContext


fun ByteArray.readUshort(offset: Int): Int {
    return (this[offset].toInt() and 0xff) or ((this[offset + 1].toInt() and 0xff) shl 8)
}

fun ByteArray.readSmallUint(offset: Int): Int {
    val result = readInt(offset)
    if (result < 0) {
        throw ExceptionWithContext("Encountered small uint that is out of range at offset 0x%x", offset)
    }
    return result
}

fun ByteArray.readShort(offset: Int): Short {
    return ((this[offset].toInt() and 0xff) or (this[offset + 1].toInt() shl 8)).toShort()
}

fun ByteArray.readInt(offset: Int): Int {
    return this[offset].toInt() and 0xff or
            ((this[offset + 1].toInt() and 0xff) shl 8) or
            ((this[offset + 2].toInt() and 0xff) shl 16) or
            (this[offset + 3].toInt() shl 24)
}

fun ByteArray.readLong(offset: Int): Long {
    return  (this[offset].toLong() and 0xffL) or
            ((this[offset + 1].toLong() and 0xffL) shl 8) or
            ((this[offset + 2].toLong() and 0xffL) shl 16) or
            ((this[offset + 3].toLong() and 0xffL) shl 24) or
            ((this[offset + 4].toLong() and 0xffL) shl 32) or
            ((this[offset + 5].toLong() and 0xffL) shl 40) or
            ((this[offset + 6].toLong() and 0xffL) shl 48) or
            (this[offset + 7].toLong() shl 56)
}