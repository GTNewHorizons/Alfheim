package alfmod.common.core.asm

import alfheim.api.ModInfo.OBF
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

@Suppress("NAME_SHADOWING", "ClassName", "unused")
class AlfheimModularClassTransformer: IClassTransformer {
	
	override fun transform(name: String?, transformedName: String?, basicClass: ByteArray?): ByteArray? {
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		
		return when (transformedName) {
			"net.minecraft.entity.EntityLivingBase" -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EntityLivingBase$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			else                                    -> basicClass
		}
	}
	
	private class `EntityLivingBase$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == (if (OBF) "e" else "moveEntityWithHeading") && desc == "(FF)V") {
				println("Visiting EntityLivingBase#moveEntityWithHeading: $name$desc")
				return `EntityLivingBase$moveEntityWithHeading$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		private class `EntityLivingBase$moveEntityWithHeading$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, desc: String?) {
				if (opcode == GETFIELD && owner == (if (OBF) "aji" else "net/minecraft/block/Block") && name == (if (OBF) "K" else "slipperiness") && desc == "F") {
					mv.visitVarInsn(ALOAD, 0)
					mv.visitTypeInsn(CHECKCAST, if (OBF) "sa" else "net/minecraft/entity/Entity")
					mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "aji" else "net/minecraft/block/Block", "getRelativeSlipperiness", if (OBF) "(Lsa;)F" else "(Lnet/minecraft/entity/Entity;)F", false)
				} else
					super.visitFieldInsn(opcode, owner, name, desc)
			}
		}
	}
}