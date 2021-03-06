package alexsocol.patcher.asm

import alexsocol.patcher.PatcherConfigHandler
import alexsocol.patcher.asm.ASJHookLoader.Companion.OBF
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

class ASJClassTransformer: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		
		var returnClass = basicClass
		
		try {
			val cr = ClassReader(returnClass)
			val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
			val cv = ClassVisitorPotionMethodPublicizer(cw, "$name ($transformedName)")
			cr.accept(cv, ClassReader.EXPAND_FRAMES)
			returnClass = cw.toByteArray()
		} catch (e: Throwable) {
			System.err.println("Something went wrong while transforming class $transformedName. Ignore if everything is OK (this is NOT ASJLib error).")
			e.printStackTrace()
		}
		
		return when (transformedName) {
			"codechicken.nei.api.ItemInfo"                             -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemInfo$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"io.netty.channel.DefaultChannelPipeline"                  -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `DefaultChannelPipeline$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.client.network.NetHandlerPlayClient"        -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `NetHandlerPlayClient$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.client.particle.EffectRenderer"             -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EffectRenderer$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.network.play.client.C17PacketCustomPayload" -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `C17PacketCustomPayload$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.server.management.ItemInWorldManager"       -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemInWorldManager$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.tileentity.TileEntityFurnace"               -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `TileEntityFurnace$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.world.World"                                -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `World$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"thaumcraft.common.blocks.BlockCustomOre"                  -> {
				println("Transforming $transformedName")
				val cr = ClassReader(returnClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `BlockCustomOre$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			else                                                       -> returnClass
		}
	}
	
	internal class ClassVisitorPotionMethodPublicizer(cv: ClassVisitor, val className: String): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(acc: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			var access = acc
			if (name == (if (OBF) "b" else "onFinishedPotionEffect") && desc == (if (OBF) "(Lrw;)V" else "(Lnet/minecraft/potion/PotionEffect;)V")) {
				println("Publicizing onFinishedPotionEffect: $name$desc for $className")
				access = ACC_PUBLIC
			}
			if (name == (if (OBF) "a" else "onChangedPotionEffect") && desc == (if (OBF) "(Lrw;Z)V" else "(Lnet/minecraft/potion/PotionEffect;Z)V")) {
				println("Publicizing onChangedPotionEffect: $name$desc for $className")
				access = ACC_PUBLIC
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
	}
	
	// fixes crash when adding eggs
	internal class `ItemInfo$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "load") {
				println("Visiting ItemInfo#load: $name$desc")
				return `ItemInfo$load$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemInfo$load$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String?, itf: Boolean) {
				if (opcode != INVOKESTATIC || owner != "codechicken/nei/api/ItemInfo" || name != "addSpawnEggs")
					super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
	}
	
	internal class `DefaultChannelPipeline$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			val mv = super.visitMethod(access, name, desc, signature, exceptions)
			
			if (name == "checkDuplicateName") {
				mv.visitCode()
				val l0 = Label()
				mv.visitLabel(l0)
				mv.visitInsn(RETURN)
				val l1 = Label()
				mv.visitLabel(l1)
				mv.visitLocalVariable("this", "Lio/netty/channel/DefaultChannelPipeline;", null, l0, l1, 0)
				mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l1, 1)
				mv.visitMaxs(0, 2)
				mv.visitEnd()
			}
			
			return mv
		}
	}
	
	// wrong RangedAttribute#minimumValue fix
	internal class `NetHandlerPlayClient$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "handleEntityProperties" || (name == "a" && desc == "(Lil;)V")) {
				println("Visiting NetHandlerPlayClient#handleEntityProperties: $name$desc")
				return `NetHandlerPlayClient$handleEntityProperties$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `NetHandlerPlayClient$handleEntityProperties$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitLdcInsn(cst: Any?) {
				if (cst == java.lang.Double.MIN_NORMAL) super.visitLdcInsn(-java.lang.Double.MAX_VALUE)
				else super.visitLdcInsn(cst)
			}
		}
	}
	
	// More Particles
	internal class `EffectRenderer$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "addEffect" || name == "a" && desc == "(Lbkm;)V") {
				println("Visiting EffectRenderer#addEffect: $name$desc")
				return `EffectRenderer$addEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `EffectRenderer$addEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				if (opcode == SIPUSH && operand == 4000) {
					when (PatcherConfigHandler.maxParticles) {
						in Byte.MIN_VALUE..Byte.MAX_VALUE   -> super.visitIntInsn(BIPUSH, PatcherConfigHandler.maxParticles)
						in Short.MIN_VALUE..Short.MAX_VALUE -> super.visitIntInsn(SIPUSH, PatcherConfigHandler.maxParticles)
						else                                -> super.visitLdcInsn(Integer(PatcherConfigHandler.maxParticles))
					}
				} else super.visitIntInsn(opcode, operand)
			}
		}
	}
	
	// Payload fix
	internal class `C17PacketCustomPayload$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if ((name == "<init>" && desc == "(Ljava/lang/String;[B)V") || (name == "readPacketData" || name == "a" && desc == "(Let;)V") || (name == "writePacketData" || name == "b" && desc == "(Let;)V")) {
				println("Visiting C17PacketCustomPayload methods: $name$desc")
				return `C17PacketCustomPayload$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `C17PacketCustomPayload$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				if (opcode == SIPUSH && operand == 32767) {
					super.visitLdcInsn(Int.MAX_VALUE)
				} else if (opcode == LDC) {
					super.visitLdcInsn("Sorry hook not worked :(")
				} else super.visitIntInsn(opcode, operand)
			}
			
			override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
				var newName = name
				var newDesc = desc
				
				if (name == "readShort") {
					newName = "readInt"
					newDesc = "()I"
				} else if (name == "writeShort") {
					newName = "writeInt"
				}
				
				super.visitMethodInsn(opcode, owner, newName, newDesc, itf)
			}
			
			override fun visitInsn(opcode: Int) {
				if (opcode != I2S) super.visitInsn(opcode)
			}
		}
	}
	
	// Non-null fire extinguishing
	internal class `ItemInWorldManager$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "onBlockClicked" || name == "a" && desc == "(IIII)V") {
				println("Visiting ItemInWorldManager#onBlockClicked: $name$desc")
				return `ItemRelic$onBlockClicked$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemRelic$onBlockClicked$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == ACONST_NULL) {
					visitVarInsn(ALOAD, 0)
					visitFieldInsn(GETFIELD, if (OBF) "mx" else "net/minecraft/server/management/ItemInWorldManager", if (OBF) "b" else "thisPlayerMP", if (OBF) "Lmw;" else "Lnet/minecraft/entity/player/EntityPlayerMP;")
				} else {
					super.visitInsn(opcode)
				}
			}
			
			override fun visitTypeInsn(opcode: Int, type: String) {
				if (opcode != CHECKCAST || type != (if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer")) {
					super.visitTypeInsn(opcode, type)
				}
			}
		}
	}
	
	// saving more than Short.MAX_VALUE amount of fuel
	internal class `TileEntityFurnace$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
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
		
		internal class `TileEntityFurnace$readFromNBT$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
				if (name == "getShort" || (name == "e" && desc == "(Ljava/lang/String;)S")) {
					super.visitMethodInsn(opcode, owner, if (OBF) "f" else "getInteger", "(Ljava/lang/String;)I", itf)
				} else super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
		
		internal class `TileEntityFurnace$writeToNBT$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
				if (name == "setShort" || (name == "a" && desc == "(Ljava/lang/String;S)V")) {
					super.visitMethodInsn(opcode, owner, if (OBF) "a" else "setInteger", "(Ljava/lang/String;I)V", itf)
				} else super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
			
			override fun visitInsn(opcode: Int) {
				if (opcode != I2S) super.visitInsn(opcode)
			}
		}
	}
	
	// Firing any entity update event
	internal class `World$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "updateEntityWithOptionalForce" || name == "a" && desc == "(Lsa;Z)V") {
				println("Visiting World#updateEntityWithOptionalForce: $name$desc")
				return `World$updateEntityWithOptionalForce$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `World$updateEntityWithOptionalForce$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String, itf: Boolean) {
				if (opcode == INVOKEVIRTUAL && owner == (if (OBF) "sa" else "net/minecraft/entity/Entity") && (name == (if (OBF) "ab" else "updateRidden") || name == if (OBF) "h" else "onUpdate") && desc == "()V" && !itf) {
					mv.visitMethodInsn(INVOKESTATIC, "alexsocol/patcher/event/EntityUpdateEvent", "instantiate", if (OBF) "(Lsa;)Lalexsocol/patcher/event/EntityUpdateEvent;" else "(Lnet/minecraft/entity/Entity;)Lalexsocol/patcher/event/EntityUpdateEvent;", false)
					mv.visitVarInsn(ASTORE, 8)
					mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;")
					mv.visitVarInsn(ALOAD, 8)
					mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false)
					val label = Label()
					mv.visitJumpInsn(IFNE, label)
					mv.visitVarInsn(ALOAD, 1)
					mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "sa" else "net/minecraft/entity/Entity", name, "()V", false)
					mv.visitLabel(label)
					mv.visitFrame(F_APPEND, 1, arrayOf<Any>("alexsocol/patcher/event/EntityUpdateEvent"), 0, null)
					mv.visitMethodInsn(INVOKESTATIC, "alexsocol/patcher/event/EntityUpdateEvent", "stub", "()V", false)
				} else super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
	}
	
	// Fix for entropy ore being without black hit particles
	internal class `BlockCustomOre$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "addHitEffects") {
				println("Visiting BlockCustomOre#addHitEffects: $name$desc")
				return `BlockCustomOre$addHitEffects$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `BlockCustomOre$addHitEffects$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				if (opcode == BIPUSH && operand == 6) super.visitIntInsn(opcode, 7)
				else super.visitIntInsn(opcode, operand)
			}
		}
	}
}