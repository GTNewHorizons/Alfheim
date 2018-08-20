package alfheim.common.core.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import net.minecraft.launchwrapper.IClassTransformer;

public class AlfheimClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemRelic$ClassVisitor ct = new ItemRelic$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		
		if (transformedName.equals("net.minecraft.server.management.ItemInWorldManager")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemInWorldManager$ClassVisitor ct = new ItemInWorldManager$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		return basicClass;
	}

	static class ItemRelic$ClassVisitor extends ClassVisitor {
		
		public ItemRelic$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("addBindInfo")) return new ItemRelic$addBindInfo$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemRelic$addBindInfo$MethodVisitor extends MethodVisitor {
			
			public ItemRelic$addBindInfo$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			private boolean perform = false;
			
			@Override
			public void visitJumpInsn(int opcode, Label label) {
				if (opcode == IF_ACMPNE) {
					if (!perform) {
						perform = true;
						super.visitJumpInsn(opcode, label);
					} else {
						visitInsn(POP2);
						visitInsn(ICONST_1);
						super.visitJumpInsn(IFEQ, label);
					}
				} else {
					super.visitJumpInsn(opcode, label);
				}
			}
		}
	}

	static class ItemInWorldManager$ClassVisitor extends ClassVisitor {
		
		public ItemInWorldManager$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("onBlockClicked")) return new ItemRelic$onBlockClicked$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemRelic$onBlockClicked$MethodVisitor extends MethodVisitor {
			
			public ItemRelic$onBlockClicked$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == ACONST_NULL) {
					visitVarInsn(ALOAD, 0);
					visitFieldInsn(GETFIELD, "net/minecraft/server/management/ItemInWorldManager", "thisPlayerMP", "Lnet/minecraft/entity/player/EntityPlayerMP;");
				} else {
					super.visitInsn(opcode);
				}
			}
			
			@Override
			public void visitTypeInsn(int opcode, String type) {
				if (opcode == CHECKCAST && type.equals("net/minecraft/entity/player/EntityPlayer")) {}
				else {
					super.visitTypeInsn(opcode, type);
				}
			}
		}
	}
}