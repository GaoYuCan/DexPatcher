package site.gaoyucan

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.google.common.io.LittleEndianDataInputStream
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.formatter.DexFormatter
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.immutable.ImmutableAnnotation
import org.jf.dexlib2.immutable.ImmutableMethod
import org.jf.dexlib2.immutable.ImmutableMethodImplementation
import org.jf.dexlib2.immutable.ImmutableMethodParameter
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction10t
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction10x
import org.jf.dexlib2.rewriter.DexRewriter
import org.jf.dexlib2.rewriter.MethodRewriter
import org.jf.dexlib2.rewriter.Rewriter
import org.jf.dexlib2.rewriter.RewriterModule
import org.jf.dexlib2.rewriter.Rewriters
import site.gaoyucan.dexpatch.instruction.DexPatchInstruction
import site.gaoyucan.ext.readSmallUint
import site.gaoyucan.ext.readUshort
import java.io.InputStream


class Main {
    companion object {
        val methods = mapOf(
            "Lcom/example/ezandroid/S;->onCreate(Landroid/os/Bundle;)V" to "assets/0OooOO",
            "Lcom/example/ezandroid/S2;->onCreate(Landroid/os/Bundle;)V" to "assets/O0ooOO",
            "Lcom/example/ezandroid/MU;->GMS(Ljava/lang/String;)Ljava/lang/String;" to "assets/oo0Ooo",
            "Lcom/example/ezandroid/C;->cf(Ljava/lang/String;I)Z" to "assets/ooO0oo",
            "Lcom/example/ezandroid/S$1;->onClick(Landroid/view/View;)V" to "assets/OOoo0O",
            "Lcom/example/ezandroid/S2$1;->onClick(Landroid/view/View;)V" to "assets/OOooO0",
        )


        @OptIn(ExperimentalStdlibApi::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val dexFileAsStream = openResource("classes.dex")
            val dexFile = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), dexFileAsStream)
            val opcodeArr = Opcode.entries.map {
                it.apiToValueMap[dexFile.opcodes.api]?.toInt() to it
            }.toMap()
            DexRewriter(object : RewriterModule() {
                override fun getMethodRewriter(rewriters: Rewriters): Rewriter<Method> {
                    return object : MethodRewriter(rewriters) {
                        override fun rewrite(method: Method): Method {
                            val methodDescriptor = DexFormatter.INSTANCE.getMethodDescriptor(method)
                            if (methodDescriptor in methods.keys) {
                                println("Rewriting $methodDescriptor : native method: ${method.accessFlags and AccessFlags.NATIVE.value != 0}")
                                val rawMethod = LittleEndianDataInputStream(openResource(methods[methodDescriptor]!!))
                                val regCount = rawMethod.readUnsignedShort()
                                val codeSegSize = rawMethod.readUnsignedShort() * 2
                                val _argCount = rawMethod.readUnsignedShort() // drop this
                                val instructions = mutableListOf<Instruction>()
                                var i = 0
                                while (i < codeSegSize) {
                                    var op = rawMethod.readUnsignedByte()
                                    // TODO: 这里存在问题，按理说该放后面，但是这里我确定几个 Payload 和 Nop 都没动，所以先这样吧
                                    if (op == 0x00) { // maybe Payload
                                        op = rawMethod.readUnsignedByte() shl 8
                                    }
                                    if (op == 0x00) { // 提前处理 NOP
                                        instructions.add(ImmutableInstruction10x(Opcode.NOP))
                                        i += 2
                                        continue
                                    }
                                    // 获取原始（真实） opcode
                                    if (op !in opcodesMap.keys) { // 验证是否已经找到对应的修改后的opcode
                                        throw IllegalStateException("Unknown opcode: ${op.toUShort().toHexString()}")
                                    }
                                    op = opcodesMap[op]!!
                                    val opcode = opcodeArr[op]!!
                                    // 获取指令长度
                                    val insSize = opcode.format.size
                                    if (insSize == -1) {
                                        var insSizeVar: Int
                                        var rawCount: ByteArray
                                        if (opcode == Opcode.PACKED_SWITCH_PAYLOAD) {
                                            rawCount = ByteArray(2) // elementCount
                                            rawMethod.read(rawCount, 0, 2)
                                            insSizeVar = 8 + rawCount.readUshort(0) * 4

                                        } else if (opcode == Opcode.SPARSE_SWITCH_PAYLOAD) {
                                            rawCount = ByteArray(2) // elementCount
                                            rawMethod.read(rawCount, 0, 2)
                                            insSizeVar = 4 + rawCount.readUshort(0) * 8
                                        } else {
                                            rawCount = ByteArray(6) // elementWidth
                                            rawMethod.read(rawCount, 0, 6)
                                            val localElementWidth = rawCount.readUshort(0)
                                            insSizeVar = if (localElementWidth == 0) {
                                                8
                                            } else {
                                                ((rawCount.readSmallUint(2) * localElementWidth + 1) / 2) * 2 + 8
                                            }
                                        }
                                        val rawInstruction = ByteArray(insSizeVar)
                                        // offset=2 跳过 opcode，补上 rawCount
                                        rawCount.copyInto(rawInstruction, 2, 0, rawCount.size)
                                        // offset=2+rawCount.size 跳过 opcode，rawCount，补上剩余的数据
                                        rawMethod.read(
                                            rawInstruction,
                                            2 + rawCount.size,
                                            insSizeVar - 2 - rawCount.size
                                        )
                                        val instruction =
                                            DexPatchInstruction.buildInstruction(opcode, rawInstruction, dexFile)
                                        instructions.add(instruction)
                                        i += insSizeVar
                                    } else {
                                        // 创建指令
                                        val rawInstruction = ByteArray(insSize)
                                        // offset=1 跳过 opcode
                                        rawMethod.read(rawInstruction, 1, insSize - 1)
                                        val instruction =
                                            DexPatchInstruction.buildInstruction(opcode, rawInstruction, dexFile)
                                        instructions.add(instruction)
                                        i += insSize
                                    }
                                }
                                // 构造 methodImplementation, parameters, annotations
                                val methodImplementation = ImmutableMethodImplementation(
                                    regCount,
                                    instructions,
                                    emptyList(),
                                    emptyList(),
                                )
                                val parameters = method.parameters.stream().map { ImmutableMethodParameter.of(it) }
                                    .collect(ImmutableList.toImmutableList())
                                val annotations = method.annotations.stream().map { ImmutableAnnotation.of(it) }
                                    .collect(ImmutableSet.toImmutableSet())
                                // 构造新的 method
                                return ImmutableMethod(
                                    method.definingClass,
                                    method.name,
                                    parameters,
                                    method.returnType,
                                    method.accessFlags xor AccessFlags.NATIVE.value,
                                    annotations,
                                    ImmutableSet.of(),
                                    methodImplementation
                                )
                            }
                            return super.rewrite(method)
                        }
                    }
                }
            }).dexFileRewriter.rewrite(dexFile).let {
                DexFileFactory.writeDexFile("classes-rewritten.dex", it)
            }

        }

        val opcodesMap = mapOf(
            0x6f to 0x6f,
            0x1a to 0x6e,
            0x26 to 0x0c,
            0x54 to 0x71,
            0x22 to 0x54,
            0x71 to 0x1a,
            0x0e to 0x22,
            0x0c to 0x70,
            0x70 to 0x0e,
            0x12 to 0x12,
            0x5b to 0x5b,
            0x39 to 0x39,
            0x0a to 0x0a,
            0x13 to 0x13,
            0xdf to 0x33,
            0x23 to 0x23,
            0x4d to 0x4d,
            0x28 to 0x28,
            0x38 to 0x38,
            0x33 to 0x26,
            0x35 to 0x35,
            0x48 to 0xdf,
            0x6e to 0x48,
            0x32 to 0x32,
            0x0f to 0x0f,
            0xd8 to 0xd8,
            0x300 to 0x300,
            0x0d to 0x0d,
            0x11 to 0x11,
            0x14 to 0x14,
            0x1f to 0x1f
        )

        private fun openResource(name: String): InputStream {
            return Main::class.java.classLoader.getResourceAsStream(name)!!
        }
    }
}