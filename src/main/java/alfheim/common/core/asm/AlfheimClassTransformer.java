package alfheim.common.core.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import net.minecraft.launchwrapper.IClassTransformer;

public class AlfheimClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			AlfheimClassVisitor ct = new AlfheimClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		}
		return basicClass;
	}
}