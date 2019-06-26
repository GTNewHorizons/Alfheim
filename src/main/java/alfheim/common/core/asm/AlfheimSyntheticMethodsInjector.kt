package alfheim.common.core.asm

import alfheim.api.ModInfo.OBF
import org.objectweb.asm.Opcodes.*

import org.objectweb.asm.*

import net.minecraft.launchwrapper.IClassTransformer

class AlfheimSyntheticMethodsInjector: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		var basicClass = basicClass
		if (basicClass == null || basicClass.size == 0) return basicClass
		
		var cr: ClassReader
		var cw: ClassWriter
		var cv: ClassVisitor
		
		try {
			cr = ClassReader(basicClass)
			cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			cv = ClassVisitorPotionMethodPublicizer(cw, String.format("%s (%s)", name, transformedName))
			cr.accept(cv, ClassReader.EXPAND_FRAMES)
			basicClass = cw.toByteArray()
		} catch (e: Throwable) {
			if (doLog) {
				System.err.println("Something went wrong while transforming class $transformedName. Ignore if everything is OK (this is NOT Alfheim error).")
				e.printStackTrace()
			}
		}
		
		if (transformedName == "alfheim.common.core.asm.AlfheimSyntheticMethods") {
			cr = ClassReader(basicClass!!)
			cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			cv = `AlfheimSyntheticMethods$ClassVisitor`(cw)
			cr.accept(cv, ClassReader.SKIP_FRAMES)
			return cw.toByteArray()
		}
		
		return basicClass
	}
	
	internal class ClassVisitorPotionMethodPublicizer(cv: ClassVisitor, val className: String): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array<String>): MethodVisitor {
			var access = access
			if (name == (if (OBF) "b" else "onFinishedPotionEffect") && desc == (if (OBF) "(Lrw;)V" else "(Lnet/minecraft/potion/PotionEffect;)V")) {
				println("Publicizing onFinishedPotionEffect: $name$desc for $className")
				access = ACC_PUBLIC
			}
			if (name == (if (OBF) "a" else "onChangedPotionEffect") && desc == (if (OBF) "(Lrw;Z)V" else "(Lnet/minecraft/potion/PotionEffect;Z)V")) {
				println("Publicizing onChangedPotionEffect: $name$desc for $className")
				access = ACC_PUBLIC
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
	}
	
	internal class `AlfheimSyntheticMethods$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array<String>): MethodVisitor {
			if (name == "onFinishedPotionEffect") {
				println("Generating synthetic onFinishedPotionEffect")
				return `AlfheimSyntheticMethods$onFinishedPotionEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			} else if (name == "onChangedPotionEffect") {
				println("Generating synthetic onChangedPotionEffect")
				return `AlfheimSyntheticMethods$onChangedPotionEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `AlfheimSyntheticMethods$onFinishedPotionEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0)
					visitVarInsn(ALOAD, 1)
					visitMethodInsn(INVOKEVIRTUAL, if (OBF) "sv" else "net/minecraft/entity/EntityLivingBase", if (OBF) "b" else "onFinishedPotionEffect", if (OBF) "(Lrw;)V" else "(Lnet/minecraft/potion/PotionEffect;)V", false)
				}
				super.visitInsn(opcode)
			}
			
			override fun visitMaxs(maxStack: Int, maxLocals: Int) {
				super.visitMaxs(2, 2)
			}
		}
		
		internal class `AlfheimSyntheticMethods$onChangedPotionEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0)
					visitVarInsn(ALOAD, 1)
					visitVarInsn(ILOAD, 2)
					visitMethodInsn(INVOKEVIRTUAL, if (OBF) "sv" else "net/minecraft/entity/EntityLivingBase", if (OBF) "a" else "onChangedPotionEffect", if (OBF) "(Lrw;Z)V" else "(Lnet/minecraft/potion/PotionEffect;Z)V", false)
				}
				super.visitInsn(opcode)
			}
			
			override fun visitMaxs(maxStack: Int, maxLocals: Int) {
				super.visitMaxs(3, 3)
			}
		}
	}
	
	companion object {
		
		val doLog = System.getProperty("asjlib.asm.errorlog", "off") == "on"
	}
}