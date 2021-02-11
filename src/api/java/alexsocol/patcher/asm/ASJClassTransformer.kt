package alexsocol.patcher.asm

import alexsocol.patcher.PatcherConfigHandler
import alexsocol.patcher.asm.ASJHookLoader.Companion.OBF
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*

class ASJClassTransformer: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		
		return when (transformedName) {
			"net.minecraft.client.network.NetHandlerPlayClient"  -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `NetHandlerPlayClient$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.client.particle.EffectRenderer"       -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EffectRenderer$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.server.management.ItemInWorldManager" -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemInWorldManager$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.tileentity.TileEntityFurnace"         -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `TileEntityFurnace$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.world.World"                          -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `World$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"thaumcraft.common.blocks.BlockCustomOre"            -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `BlockCustomOre$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			else                                                 -> basicClass
		}
	}
	
	// wrong RangedAttribute#minimumValue fix
	internal class `NetHandlerPlayClient$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "handleEntityProperties" || (name == "a" && desc == "(Lil;)V")) {
				println("Visiting NetHandlerPlayClient#handleEntityProperties: $name$desc")
				return `NetHandlerPlayClient$handleEntityProperties$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `NetHandlerPlayClient$handleEntityProperties$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitLdcInsn(cst: Any?) {
				if (cst == java.lang.Double.MIN_NORMAL)
					super.visitLdcInsn(-java.lang.Double.MAX_VALUE)
				else
					super.visitLdcInsn(cst)
			}
		}
	}
	
	// More Particles
	internal class `EffectRenderer$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "addEffect" || name == "a" && desc == "(Lbkm;)V") {
				println("Visiting EffectRenderer#addEffect: $name$desc")
				return `EffectRenderer$addEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `EffectRenderer$addEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				if (opcode == Opcodes.SIPUSH && operand == 4000) {
					when (PatcherConfigHandler.maxParticles) {
						in Byte.MIN_VALUE..Byte.MAX_VALUE   -> super.visitIntInsn(Opcodes.BIPUSH, PatcherConfigHandler.maxParticles)
						in Short.MIN_VALUE..Short.MAX_VALUE -> super.visitIntInsn(Opcodes.SIPUSH, PatcherConfigHandler.maxParticles)
						else                                -> super.visitLdcInsn(Integer(PatcherConfigHandler.maxParticles))
					}
				} else
					super.visitIntInsn(opcode, operand)
			}
		}
	}
	
	// Non-null fire extinguishing
	internal class `ItemInWorldManager$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "onBlockClicked" || name == "a" && desc == "(IIII)V") {
				println("Visiting ItemInWorldManager#onBlockClicked: $name$desc")
				return `ItemRelic$onBlockClicked$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemRelic$onBlockClicked$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == Opcodes.ACONST_NULL) {
					visitVarInsn(Opcodes.ALOAD, 0)
					visitFieldInsn(Opcodes.GETFIELD, if (OBF) "mx" else "net/minecraft/server/management/ItemInWorldManager", if (OBF) "b" else "thisPlayerMP", if (OBF) "Lmw;" else "Lnet/minecraft/entity/player/EntityPlayerMP;")
				} else {
					super.visitInsn(opcode)
				}
			}
			
			override fun visitTypeInsn(opcode: Int, type: String) {
				if (opcode != Opcodes.CHECKCAST || type != (if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer")) {
					super.visitTypeInsn(opcode, type)
				}
			}
		}
	}
	
	// saving more than Short.MAX_VALUE amount of fuel
	internal class `TileEntityFurnace$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "readFromNBT" || (name == "a" && desc == "(Ldh;)V")) {
				println("Visiting TileEntityFurnace#readFromNBT: $name$desc")
				return `TileEntityFurnace$readFromNBT$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			} else if (name == "writeToNBT" || (name == "b" && desc == "(Ldh;)V")) {
				println("Visiting TileEntityFurnace#writeToNBT: $name$desc")
				return `TileEntityFurnace$writeToNBT$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `TileEntityFurnace$readFromNBT$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
				if (name == "getShort" || (name == "e" && desc == "(Ljava/lang/String;)S")) {
					super.visitMethodInsn(opcode, owner, if (OBF) "f" else "getInteger", "(Ljava/lang/String;)I", itf)
				} else
					super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
		
		internal class `TileEntityFurnace$writeToNBT$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
				if (name == "setShort" || (name == "a" && desc == "(Ljava/lang/String;S)V")) {
					super.visitMethodInsn(opcode, owner, if (OBF) "a" else "setInteger", "(Ljava/lang/String;I)V", itf)
				} else
					super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
			
			override fun visitInsn(opcode: Int) {
				if (opcode != Opcodes.I2S) super.visitInsn(opcode)
			}
		}
	}
	
	// Firing any entity update event
	internal class `World$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "updateEntityWithOptionalForce" || name == "a" && desc == "(Lsa;Z)V") {
				println("Visiting World#updateEntityWithOptionalForce: $name$desc")
				return `World$updateEntityWithOptionalForce$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `World$updateEntityWithOptionalForce$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String, itf: Boolean) {
				if (opcode == Opcodes.INVOKEVIRTUAL && owner == (if (OBF) "sa" else "net/minecraft/entity/Entity") && (name == (if (OBF) "ab" else "updateRidden") || name == if (OBF) "h" else "onUpdate") && desc == "()V" && !itf) {
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "alexsocol/patcher/event/EntityUpdateEvent", "instantiate", if (OBF) "(Lsa;)Lalexsocol/patcher/event/EntityUpdateEvent;" else "(Lnet/minecraft/entity/Entity;)Lalexsocol/patcher/event/EntityUpdateEvent;", false)
					mv.visitVarInsn(Opcodes.ASTORE, 8)
					mv.visitFieldInsn(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;")
					mv.visitVarInsn(Opcodes.ALOAD, 8)
					mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false)
					val label = Label()
					mv.visitJumpInsn(Opcodes.IFNE, label)
					mv.visitVarInsn(Opcodes.ALOAD, 1)
					mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, if (OBF) "sa" else "net/minecraft/entity/Entity", name, "()V", false)
					mv.visitLabel(label)
					mv.visitFrame(Opcodes.F_APPEND, 1, arrayOf<Any>("alexsocol/patcher/event/EntityUpdateEvent"), 0, null)
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "alexsocol/patcher/event/EntityUpdateEvent", "stub", "()V", false)
				} else
					super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
	}
	
	// Fix for entropy ore being without black hit particles
	internal class `BlockCustomOre$ClassVisitor`(cv: ClassVisitor): ClassVisitor(Opcodes.ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "addHitEffects") {
				println("Visiting BlockCustomOre#addHitEffects: $name$desc")
				return `BlockCustomOre$addHitEffects$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `BlockCustomOre$addHitEffects$MethodVisitor`(mv: MethodVisitor): MethodVisitor(Opcodes.ASM5, mv) {
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				if (opcode == Opcodes.BIPUSH && operand == 6) super.visitIntInsn(opcode, 7)
				else super.visitIntInsn(opcode, operand)
			}
		}
	}
}