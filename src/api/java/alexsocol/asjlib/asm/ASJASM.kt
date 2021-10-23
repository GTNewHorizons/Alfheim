package alexsocol.asjlib.asm

import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*
import org.objectweb.asm.tree.ClassNode

/** Include this to your #getASMTransformerClass to make @HookField's work  */
class ASJASM: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		if (!fieldsMap.containsKey(transformedName)) return basicClass
		val cr = ClassReader(basicClass)
		val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
		for (fd in fieldsMap[transformedName]!!) cw.visitField(fd.access, fd.name, fd.desc, null, null).visitEnd()
		cr.accept(cw, 0)
		return cw.toByteArray()
	}
	
	companion object {
		
		val fieldsMap = HashMap<String, ArrayList<FieldData>>()
		
		fun registerFieldHookContainer(className: String) {
			try {
				transform(ASJASM::class.java.getResourceAsStream("/${className.replace('.', '/')}.class")?.readBytes() ?: throw NullPointerException("Can't read data from ${className}.class"))
			} catch (e: Exception) {
				System.err.println("[ASJASM] Can not parse hooks container $className")
				e.printStackTrace()
			}
		}
		
		private fun transform(basicClass: ByteArray) {
			val cr = ClassReader(basicClass)
			val cn = ClassNode()
			cr.accept(cn, 0)
			for (fn in cn.fields) {
				var flag = false
				var targetClassName = ""
				if (fn.visibleAnnotations != null && fn.visibleAnnotations.isNotEmpty()) {
					for (an in fn.visibleAnnotations) {
						if (an.desc == Type.getDescriptor(HookField::class.java)) {
							flag = true
							if (an.values != null && an.values.isNotEmpty()) {
								var i = 0
								while (i < an.values.size) {
									if (an.values[i] == "targetClassName") {
										targetClassName = an.values[i + 1].toString()
										break
									}
									i += 2
								}
							}
						}
						if (flag && targetClassName.isNotEmpty()) break
					}
				}
				
				if (!flag || targetClassName.isEmpty()) continue
				
				if (!fieldsMap.containsKey(targetClassName)) fieldsMap[targetClassName] = ArrayList()
				fieldsMap[targetClassName]?.add(FieldData(fn.access, fn.name, fn.desc))
			}
		}
	}
}
