package alfheim.common.core.asm;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class AlfheimClassVisitor extends ClassVisitor {

	public AlfheimClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals("addBindInfo")) return new AlfheimMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
