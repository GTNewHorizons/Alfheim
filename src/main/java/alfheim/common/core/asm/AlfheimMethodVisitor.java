package alfheim.common.core.asm;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;

public class AlfheimMethodVisitor extends MethodVisitor {

	private boolean perform = false;
	
	public AlfheimMethodVisitor(MethodVisitor mv) {
		super(ASM5, mv);
	}
	
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
