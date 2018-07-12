package alfheim.common.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

public class AlfheimClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemRelic_ClassVisitor ct = new ItemRelic_ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		return basicClass;
	}

	static class ItemRelic_ClassVisitor extends ClassVisitor {
		
		public ItemRelic_ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("addBindInfo")) return new ItemRelic_addBindInfo_MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemRelic_addBindInfo_MethodVisitor extends MethodVisitor {
			
			public ItemRelic_addBindInfo_MethodVisitor(MethodVisitor mv) {
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
}