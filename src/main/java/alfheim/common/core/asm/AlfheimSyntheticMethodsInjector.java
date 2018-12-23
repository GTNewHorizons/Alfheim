package alfheim.common.core.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.*;

import static alfheim.api.ModInfo.OBF;
import gloomyfolken.hooklib.minecraft.HookLibPlugin;
import net.minecraft.launchwrapper.IClassTransformer;

public class AlfheimSyntheticMethodsInjector implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassVisitor cv = new ClassVisitorPotionMethodPublicizer(cw, String.format("%s (%s)", name, transformedName));
		cr.accept(cv, ClassReader.SKIP_FRAMES);
		basicClass = cw.toByteArray();
		
		if (transformedName.equals("alfheim.common.core.asm.AlfheimSyntheticMethods")) {
			cr = new ClassReader(basicClass);
			cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cv = new AlfheimSyntheticMethods$ClassVisitor(cw);
			cr.accept(cv, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		
		return basicClass;
	}
	
	static class ClassVisitorPotionMethodPublicizer extends ClassVisitor {
		
		public final String className;
		
		public ClassVisitorPotionMethodPublicizer(ClassVisitor cv, String cn) {
			super(ASM5, cv);
			className = cn;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (name.equals(OBF ? "b" : "onFinishedPotionEffect") && desc.equals(OBF ? "(Lrw;)V" : "(Lnet/minecraft/potion/PotionEffect;)V")) {
				System.out.println("Publicizing onFinishedPotionEffect: " + name + desc + " for " + className);
				access = ACC_PUBLIC;
			}
			if (name.equals(OBF ? "a" : "onChangedPotionEffect") && desc.equals(OBF ? "(Lrw;Z)V" : "(Lnet/minecraft/potion/PotionEffect;Z)V")) {
				System.out.println("Publicizing onChangedPotionEffect: " + name + desc + " for " + className);
				access = ACC_PUBLIC;
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
	
	static class AlfheimSyntheticMethods$ClassVisitor extends ClassVisitor {
		
		public AlfheimSyntheticMethods$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("onFinishedPotionEffect")) {
				System.out.println("Generating synthetic onFinishedPotionEffect: " + name + desc);
				return new AlfheimSyntheticMethods$onFinishedPotionEffect$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			if (name.equals("onChangedPotionEffect")) {
				System.out.println("Generating synthetic onChangedPotionEffect: " + name + desc);
				return new AlfheimSyntheticMethods$onChangedPotionEffect$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class AlfheimSyntheticMethods$onFinishedPotionEffect$MethodVisitor extends MethodVisitor {
			
			public AlfheimSyntheticMethods$onFinishedPotionEffect$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0);
					visitVarInsn(ALOAD, 1);
					visitMethodInsn(INVOKEVIRTUAL, OBF ? "sv" : "net/minecraft/entity/EntityLivingBase", OBF ? "b" : "onFinishedPotionEffect", OBF ? "(Lrw;)V" : "(Lnet/minecraft/potion/PotionEffect;)V", false);
				}
				super.visitInsn(opcode);
			}
			
			@Override
			public void visitMaxs(int maxStack, int maxLocals) {
				super.visitMaxs(2, 2);
			}
		}
		
		static class AlfheimSyntheticMethods$onChangedPotionEffect$MethodVisitor extends MethodVisitor {
			
			public AlfheimSyntheticMethods$onChangedPotionEffect$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0);
					visitVarInsn(ALOAD, 1);
					visitVarInsn(ILOAD, 2);
					visitMethodInsn(INVOKEVIRTUAL, OBF ? "sv" : "net/minecraft/entity/EntityLivingBase", OBF ? "a" : "onChangedPotionEffect", OBF ? "(Lrw;Z)V" : "(Lnet/minecraft/potion/PotionEffect;Z)V", false);
				}
				super.visitInsn(opcode);
			}
			
			@Override
			public void visitMaxs(int maxStack, int maxLocals) {
				super.visitMaxs(3, 3);
			}
		}
	}
}