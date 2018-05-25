package alfheim.common.core.asm

import jdk.internal.org.objectweb.asm.Opcodes.*;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;

public class AlfheimMethodVisitor(val mv: MethodVisitor): MethodVisitor(ASM5, mv) {

    private var perform = false;
    
    override fun visitJumpInsn(opcode: Int, label: Label) {
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