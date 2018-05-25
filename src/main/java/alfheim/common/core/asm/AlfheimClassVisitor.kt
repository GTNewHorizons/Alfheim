package alfheim.common.core.asm

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

class AlfheimClassVisitor(val cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
    
    override fun visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array<String>) : MethodVisitor {
        if (name.equals("addBindInfo")) return AlfheimMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}