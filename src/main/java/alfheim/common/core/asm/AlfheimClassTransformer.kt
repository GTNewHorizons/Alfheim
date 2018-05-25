package alfheim.common.core.asm

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import net.minecraft.launchwrapper.IClassTransformer;

class AlfheimClassTransformer : IClassTransformer {
    
    override fun transform(name: String, transformedName: String, basicClass: ByteArray) : ByteArray {
        if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
            val cr = ClassReader(basicClass);
            val cw = ClassWriter(ClassWriter.COMPUTE_MAXS);
            val ct = AlfheimClassVisitor(cw);
            cr.accept(ct, ClassReader.SKIP_FRAMES);
            return cw.toByteArray();
        }
        return basicClass;
    }
}