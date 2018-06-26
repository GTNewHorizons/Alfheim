package alfheim.common.core.asm

import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

class AlfheimClassTransformer : IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray): ByteArray {
		if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
			var cr = ClassReader(basicClass)
			var cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			var ct = ItemRelic_ClassVisitor(cw)
			cr.accept(ct, ClassReader.SKIP_FRAMES)
			return cw.toByteArray()
		}
		return basicClass
	}
	
	class ItemRelic_ClassVisitor(var cv: ClassVisitor) : ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?) : MethodVisitor {
			if (name.equals("addBindInfo")) return ItemRelic_addBindInfo_MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions))
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		class ItemRelic_addBindInfo_MethodVisitor(var mv: MethodVisitor) : MethodVisitor(ASM5, mv) {
			private var perform = false
			
			override fun visitJumpInsn(opcode: Int, label: Label) {
				if (opcode == IF_ACMPNE) {
					if (!perform) {
						perform = true
						super.visitJumpInsn(opcode, label)
					} else {
						visitInsn(POP2)
						visitInsn(ICONST_1)
						super.visitJumpInsn(IFEQ, label)
					}
				} else {
					super.visitJumpInsn(opcode, label)
				}
			}
		}
	}
}