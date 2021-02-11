package alexsocol.patcher.asm

import alexsocol.patcher.asm.ASJHookLoader.Companion.OBF
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

@Suppress("NAME_SHADOWING")
class ASJSyntheticMethodsInjector: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		var basicClass = basicClass
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		
		var cr: ClassReader
		var cw: ClassWriter
		var cv: ClassVisitor
		
		try {
			cr = ClassReader(basicClass)
			cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			cv = ClassVisitorPotionMethodPublicizer(cw, "$name ($transformedName)")
			cr.accept(cv, ClassReader.EXPAND_FRAMES)
			basicClass = cw.toByteArray()
		} catch (e: Throwable) {
			if (doLog) {
				System.err.println("Something went wrong while transforming class $transformedName. Ignore if everything is OK (this is NOT ASJLib error).")
				e.printStackTrace()
			}
		}
		
		if (transformedName == "alexsocol.patcher.asm.ASJSyntheticMethods") {
			cr = ClassReader(basicClass!!)
			cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			cv = `ASJSyntheticMethods$ClassVisitor`(cw)
			cr.accept(cv, ClassReader.SKIP_FRAMES)
			return cw.toByteArray()
		}
		
		return basicClass
	}
	
	internal class ClassVisitorPotionMethodPublicizer(cv: ClassVisitor, val className: String): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
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
	
	internal class `ASJSyntheticMethods$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "onFinishedPotionEffect") {
				println("Generating synthetic onFinishedPotionEffect")
				return `ASJSyntheticMethods$onFinishedPotionEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			} else if (name == "onChangedPotionEffect") {
				println("Generating synthetic onChangedPotionEffect")
				return `ASJSyntheticMethods$onChangedPotionEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ASJSyntheticMethods$onFinishedPotionEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
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
		
		internal class `ASJSyntheticMethods$onChangedPotionEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
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