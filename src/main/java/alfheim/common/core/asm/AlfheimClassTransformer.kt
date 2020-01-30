package alfheim.common.core.asm

import alfheim.api.ModInfo.OBF
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

@Suppress("NAME_SHADOWING", "ClassName", "unused")
class AlfheimClassTransformer: IClassTransformer {
	
	override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
		if (basicClass == null || basicClass.isEmpty()) return basicClass
		
		return when (transformedName) {
			"alfheim.common.integration.bloodmagic.BloodMagicAlfheimConfig" -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `BloodMagicAlfheimConfig$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.client.network.NetHandlerPlayClient"             -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `NetHandlerPlayClient$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.client.particle.EffectRenderer"                  -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EffectRenderer$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.entity.EntityTrackerEntry"                       -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EntityTrackerEntry$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.potion.Potion"                                   -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `Potion$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.server.management.ItemInWorldManager"            -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemInWorldManager$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.EXPAND_FRAMES)
				cw.toByteArray()
			}
			
			"net.minecraft.world.World"                                     -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `World$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"thaumcraft.common.blocks.BlockCustomOre"                       -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `BlockCustomOre$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"thaumcraft.common.items.ItemNugget"                            -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemNugget$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.client.core.handler.BaubleRenderHandler"        -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `BaubleRenderHandler$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.client.render.tile.RenderTileFloatingFlower"    -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `RenderTileFloatingFlower$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.block.decor.IFloatingFlower\$IslandType" -> {
				println("Transforming $transformedName")
				if (OBF) {
					val cr = ClassReader(basicClass)
					val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
					val cv = object: ClassVisitor(ASM5, cw) {}
					cr.accept(cv, ASM5)
					
					val mw = cw.visitMethod(ACC_PUBLIC, "getColor", "()I", null, null)
					mw.visitLdcInsn(Integer(16777215))
					mw.visitInsn(IRETURN)
					mw.visitMaxs(0, 0)
					mw.visitEnd()
					
					cw.toByteArray()
				} else basicClass
			}
			
			"vazkii.botania.common.entity.EntityDoppleganger"               -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `EntityDoppleganger$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.item.equipment.bauble.ItemMiningRing",
			"vazkii.botania.common.item.equipment.bauble.ItemWaterRing"     -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemInfiniEffect$ClassVisitor`(transformedName.split("\\.".toRegex())[6], cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.item.lens.ItemLens"                      -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemLens$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.item.relic.ItemRelic"                    -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemRelic$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.item.rod.ItemTerraformRod"               -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ItemTerraformRod$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"vazkii.botania.common.lib.LibItemNames"                        -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `LibItemNames$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			"com.emoniph.witchery.client.ClientEvents\$GUIOverlay"          -> {
				println("Transforming $transformedName")
				val cr = ClassReader(basicClass)
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
				val ct = `ClientEvents$GUIOverlay$ClassVisitor`(cw)
				cr.accept(ct, ClassReader.SKIP_FRAMES)
				cw.toByteArray()
			}
			
			else                                                            -> basicClass
		}
	}
	
	internal class `BloodMagicAlfheimConfig$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "<init>") {
				val vis = super.visitMethod(ACC_PUBLIC + ACC_FINAL, "onTeleposing", "(LWayofTime/alchemicalWizardry/api/event/TeleposeEvent;)V", null, null)
				
				vis.visitAnnotation("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;", true).visitEnd()
				vis.visitParameterAnnotation(0, "Lorg/jetbrains/annotations/NotNull;", false).visitEnd()
				
				vis.visitCode()
				val l0 = Label()
				vis.visitLabel(l0)
				vis.visitVarInsn(ALOAD, 1)
				vis.visitLdcInsn("e")
				vis.visitMethodInsn(INVOKESTATIC, "kotlin/jvm/internal/Intrinsics", "checkParameterIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V", false)
				val l1 = Label()
				vis.visitLabel(l1)
				vis.visitLineNumber(18, l1)
				vis.visitFieldInsn(GETSTATIC, "alfheim/common/integration/bloodmagic/BloodMagicAlfheimConfig", "blacklist", if (OBF) "[Laji;" else "[Lnet/minecraft/block/Block;")
				vis.visitVarInsn(ALOAD, 1)
				vis.visitFieldInsn(GETFIELD, "WayofTime/alchemicalWizardry/api/event/TeleposeEvent", "finalBlock", if (OBF) "[Laji;" else "Lnet/minecraft/block/Block;")
				vis.visitMethodInsn(INVOKESTATIC, "kotlin/collections/ArraysKt", "contains", "([Ljava/lang/Object;Ljava/lang/Object;)Z", false)
				val l2 = Label()
				vis.visitJumpInsn(IFNE, l2)
				vis.visitFieldInsn(GETSTATIC, "alfheim/common/integration/bloodmagic/BloodMagicAlfheimConfig", "blacklist", if (OBF) "[Laji;" else "[Lnet/minecraft/block/Block;")
				vis.visitVarInsn(ALOAD, 1)
				vis.visitFieldInsn(GETFIELD, "WayofTime/alchemicalWizardry/api/event/TeleposeEvent", "initialBlock", if (OBF) "Laji;" else "Lnet/minecraft/block/Block;")
				vis.visitMethodInsn(INVOKESTATIC, "kotlin/collections/ArraysKt", "contains", "([Ljava/lang/Object;Ljava/lang/Object;)Z", false)
				val l3 = Label()
				vis.visitJumpInsn(IFEQ, l3)
				vis.visitLabel(l2)
				vis.visitFrame(F_SAME, 0, null, 0, null)
				vis.visitVarInsn(ALOAD, 1)
				vis.visitInsn(ICONST_1)
				vis.visitMethodInsn(INVOKEVIRTUAL, "WayofTime/alchemicalWizardry/api/event/TeleposeEvent", "setCanceled", "(Z)V", false)
				vis.visitLabel(l3)
				vis.visitLineNumber(19, l3)
				vis.visitFrame(F_SAME, 0, null, 0, null)
				vis.visitInsn(RETURN)
				val l4 = Label()
				vis.visitLabel(l4)
				vis.visitLocalVariable("this", "Lalfheim/common/integration/bloodmagic/BloodMagicAlfheimConfig;", null, l0, l4, 0)
				vis.visitLocalVariable("e", "LWayofTime/alchemicalWizardry/api/event/TeleposeEvent;", null, l0, l4, 1)
				vis.visitMaxs(2, 2)
				vis.visitEnd()
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
	}
	
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
				if (cst == java.lang.Double.MIN_NORMAL)
					super.visitLdcInsn(-java.lang.Double.MAX_VALUE)
				else
					super.visitLdcInsn(cst)
			}
		}
	}
	
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
					when (AlfheimConfigHandler.maxParticles) {
						in Byte.MIN_VALUE..Byte.MAX_VALUE   -> super.visitIntInsn(BIPUSH, AlfheimConfigHandler.maxParticles)
						in Short.MIN_VALUE..Short.MAX_VALUE -> super.visitIntInsn(SIPUSH, AlfheimConfigHandler.maxParticles)
						else                                -> super.visitLdcInsn(Integer(AlfheimConfigHandler.maxParticles))
					}
				} else
					super.visitIntInsn(opcode, operand)
			}
		}
	}
	
	internal class `EntityTrackerEntry$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "tryStartWachingThis" || name == "b" && desc == "(Lmw;)V") {
				println("Visiting EntityTrackerEntry#tryStartWachingThis: $name$desc")
				return `EntityTrackerEntry$tryStartWachingThis$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `EntityTrackerEntry$tryStartWachingThis$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			private var sended = false
			
			override fun visitVarInsn(opcode: Int, `var`: Int) {
				super.visitVarInsn(opcode, `var`)
				if (opcode == ALOAD && `var` == 6 && !sended) {
					sended = true
					visitVarInsn(ALOAD, 0)
					visitFieldInsn(GETFIELD, if (OBF) "my" else "net/minecraft/entity/EntityTrackerEntry", if (OBF) "a" else "myEntity", if (OBF) "Lsa;" else "Lnet/minecraft/entity/Entity;")
					visitMethodInsn(INVOKESTATIC, "alfheim/common/core/handler/CardinalSystem\$PartySystem", "notifySpawn", if (OBF) "(Lsa;)V" else "(Lnet/minecraft/entity/Entity;)V", false)
				}
			}
		}
	}
	
	internal class `Potion$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "performEffect" || name == "a" && desc == "(Lsv;I)V") {
				println("Visiting Potion#performEffect: $name$desc")
				return `Potion$performEffect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `Potion$performEffect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			var flag = false
			
			override fun visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) {
				if (flag && opcode == GETSTATIC && (owner == "net/minecraft/util/DamageSource" || owner == "ro") && (name == "magic" || name == "k") && (desc == "Lnet/minecraft/util/DamageSource;" || desc == "Lro;")) {
					flag = false
					super.visitFieldInsn(GETSTATIC, "alfheim/common/core/util/DamageSourceSpell", "Companion", "Lalfheim/common/core/util/DamageSourceSpell\$Companion;")
					super.visitMethodInsn(INVOKEVIRTUAL, "alfheim/common/core/util/DamageSourceSpell\$Companion", "getPoison", if (OBF) "()Lro;" else "()Lnet/minecraft/util/DamageSource;", false)
					return
				} else if (opcode == GETSTATIC && (owner == "net/minecraft/potion/Potion" || owner == "rv") && (name == "poison" || name == "u") && (desc == "Lnet/minecraft/potion/Potion;" || desc == "Lrv;")) flag = true
				
				super.visitFieldInsn(opcode, owner, name, desc)
			}
		}
	}
	
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
					mv.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/EntityUpdateEvent", "instantiate", if (OBF) "(Lsa;)Lalfheim/api/event/EntityUpdateEvent;" else "(Lnet/minecraft/entity/Entity;)Lalfheim/api/event/EntityUpdateEvent;", false)
					mv.visitVarInsn(ASTORE, 8)
					mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;")
					mv.visitVarInsn(ALOAD, 8)
					mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false)
					val label = Label()
					mv.visitJumpInsn(IFNE, label)
					mv.visitVarInsn(ALOAD, 1)
					mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "sa" else "net/minecraft/entity/Entity", name, "()V", false)
					mv.visitLabel(label)
					mv.visitFrame(F_APPEND, 1, arrayOf<Any>("alfheim/api/event/EntityUpdateEvent"), 0, null)
					mv.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/EntityUpdateEvent", "stub", "()V", false)
				} else
					super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
	}
	
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
	
	internal class `ItemNugget$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "registerIcons" || (name == "func_94581_a")) {
				println("Visiting ItemNugget#registerIcons: $name$desc")
				return `ItemNugget$registerIcons$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			if (name == "getSubItems" || (name == "func_150895_a")) {
				println("Visiting ItemNugget#getSubItems: $name$desc")
				return `ItemNugget$getSubItems$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemNugget$registerIcons$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0)
					visitFieldInsn(GETFIELD, "thaumcraft/common/items/ItemNugget", "icon", if (OBF) "[Lrf;" else "[Lnet/minecraft/util/IIcon;")
					visitIntInsn(BIPUSH, AlfheimConfigHandler.elementiumClusterMeta)
					visitVarInsn(ALOAD, 1)
					visitLdcInsn("thaumcraft:clusterelementium")
					visitMethodInsn(INVOKEINTERFACE, if (OBF) "rg" else "net/minecraft/client/renderer/texture/IIconRegister", if (OBF) "a" else "registerIcon", if (OBF) "(Ljava/lang/String;)Lrf;" else "(Ljava/lang/String;)Lnet/minecraft/util/IIcon;", true)
					visitInsn(AASTORE)
					val l15_5 = Label()
					visitLabel(l15_5)
					visitLineNumber(47, l15_5)
				}
				super.visitInsn(opcode)
			}
		}
		
		internal class `ItemNugget$getSubItems$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitInsn(opcode: Int) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 3)
					visitTypeInsn(NEW, if (OBF) "add" else "net/minecraft/item/ItemStack")
					visitInsn(DUP)
					visitVarInsn(ALOAD, 0)
					visitInsn(ICONST_1)
					visitIntInsn(BIPUSH, AlfheimConfigHandler.elementiumClusterMeta)
					visitMethodInsn(INVOKESPECIAL, if (OBF) "add" else "net/minecraft/item/ItemStack", "<init>", if (OBF) "(Ladb;II)V" else "(Lnet/minecraft/item/Item;II)V", false)
					visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
					visitInsn(POP)
				}
				super.visitInsn(opcode)
			}
		}
	}
	
	internal class `BaubleRenderHandler$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "renderManaTablet") {
				println("Visiting BaubleRenderHandler#renderManaTablet: $name$desc")
				return `BaubleRenderHandler$renderManaTablet$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `BaubleRenderHandler$renderManaTablet$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitLdcInsn(cst: Any) {
				var cst = cst
				if (cst is Float && cst == 0.2f) cst = 0.33f
				super.visitLdcInsn(cst)
			}
		}
	}
	
	internal class `RenderTileFloatingFlower$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "renderTileEntityAt") {
				println("Visiting RenderTileFloatingFlower#renderTileEntityAt: $name$desc")
				return `RenderTileFloatingFlower$renderTileEntityAt$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `RenderTileFloatingFlower$renderTileEntityAt$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			var before = false
			var after = true
			
			override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String?, itf: Boolean) {
				if (name == "glPushMatrix") {
					if (before) {
						mv.visitTypeInsn(NEW, "java/awt/Color")
						mv.visitInsn(DUP)
						mv.visitVarInsn(ALOAD, 9)
						mv.visitMethodInsn(INVOKEINTERFACE, "vazkii/botania/common/block/decor/IFloatingFlower", "getIslandType", "()Lvazkii/botania/common/block/decor/IFloatingFlower\$IslandType;", true)
						mv.visitMethodInsn(INVOKEVIRTUAL, "vazkii/botania/common/block/decor/IFloatingFlower\$IslandType", "getColor", "()I", false)
						mv.visitMethodInsn(INVOKESPECIAL, "java/awt/Color", "<init>", "(I)V", false)
						mv.visitVarInsn(ASTORE, 12)
						
						mv.visitVarInsn(ALOAD, 12)
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/Color", "getRed", "()I", false)
						mv.visitInsn(I2F)
						mv.visitLdcInsn(java.lang.Float("255.0"))
						mv.visitInsn(FDIV)
						mv.visitVarInsn(ALOAD, 12)
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/Color", "getGreen", "()I", false)
						mv.visitInsn(I2F)
						mv.visitLdcInsn(java.lang.Float("255.0"))
						mv.visitInsn(FDIV)
						mv.visitVarInsn(ALOAD, 12)
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/Color", "getBlue", "()I", false)
						mv.visitInsn(I2F)
						mv.visitLdcInsn(java.lang.Float("255.0"))
						mv.visitInsn(FDIV)
						mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor3f", "(FFF)V", false)
					} else before = true
				} else if (name == "glPopMatrix") {
					if (after) {
						mv.visitInsn(FCONST_1)
						mv.visitInsn(FCONST_1)
						mv.visitInsn(FCONST_1)
						mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor3f", "(FFF)V", false)
					} else after = false
				}
				
				super.visitMethodInsn(opcode, owner, name, desc, itf)
			}
		}
	}
	
	internal class `EntityDoppleganger$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
			super.visit(version, access, name, signature, superName, arrayOf("alfheim/api/boss/IBotaniaBossWithShaderAndName"))
		}
		
		override// Just because!
		fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "attackEntityFrom" || name == "a" && desc == "(Lro;F)Z") {
				println("Visiting EntityDoppleganger#attackEntityFrom: $name$desc")
				val mv = cv.visitMethod(ACC_PUBLIC, if (OBF) "a" else "attackEntityFrom", if (OBF) "(Lro;F)Z" else "(Lnet/minecraft/util/DamageSource;F)Z", null, null)
				mv.visitCode()
				val l0 = Label()
				mv.visitLabel(l0)
				mv.visitLineNumber(371, l0)
				mv.visitVarInsn(ALOAD, 1)
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "ro" else "net/minecraft/util/DamageSource", if (OBF) "j" else "getEntity", if (OBF) "()Lsa;" else "()Lnet/minecraft/entity/Entity;", false)
				mv.visitVarInsn(ASTORE, 3)
				val l1 = Label()
				mv.visitLabel(l1)
				mv.visitLineNumber(372, l1)
				mv.visitVarInsn(ALOAD, 1)
				mv.visitFieldInsn(GETFIELD, if (OBF) "ro" else "net/minecraft/util/DamageSource", if (OBF) "o" else "damageType", "Ljava/lang/String;")
				mv.visitLdcInsn("player")
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false)
				val l2 = Label()
				mv.visitJumpInsn(IFNE, l2)
				mv.visitVarInsn(ALOAD, 1)
				mv.visitTypeInsn(INSTANCEOF, "alfheim/common/core/util/DamageSourceSpell")
				mv.visitJumpInsn(IFNE, l2)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitTypeInsn(INSTANCEOF, "vazkii/botania/common/entity/EntityPixie")
				val l3 = Label()
				mv.visitJumpInsn(IFEQ, l3)
				mv.visitLabel(l2)
				mv.visitFrame(F_APPEND, 1, arrayOf<Any>(if (OBF) "sa" else "net/minecraft/entity/Entity"), 0, null)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitJumpInsn(IFNULL, l3)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitMethodInsn(INVOKESTATIC, "vazkii/botania/common/entity/EntityDoppleganger", "isTruePlayer", if (OBF) "(Lsa;)Z" else "(Lnet/minecraft/entity/Entity;)Z", false)
				mv.visitJumpInsn(IFEQ, l3)
				mv.visitVarInsn(ALOAD, 0)
				mv.visitMethodInsn(INVOKEVIRTUAL, "vazkii/botania/common/entity/EntityDoppleganger", "getInvulTime", "()I", false)
				mv.visitJumpInsn(IFNE, l3)
				val l4 = Label()
				mv.visitLabel(l4)
				mv.visitLineNumber(373, l4)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitTypeInsn(CHECKCAST, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer")
				mv.visitVarInsn(ASTORE, 4)
				val l5 = Label()
				mv.visitLabel(l5)
				mv.visitLineNumber(374, l5)
				mv.visitVarInsn(ALOAD, 0)
				mv.visitFieldInsn(GETFIELD, "vazkii/botania/common/entity/EntityDoppleganger", "playersWhoAttacked", "Ljava/util/List;")
				mv.visitVarInsn(ALOAD, 4)
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "b_" else "getCommandSenderName", "()Ljava/lang/String;", false)
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "contains", "(Ljava/lang/Object;)Z", true)
				val l6 = Label()
				mv.visitJumpInsn(IFNE, l6)
				val l7 = Label()
				mv.visitLabel(l7)
				mv.visitLineNumber(375, l7)
				mv.visitVarInsn(ALOAD, 0)
				mv.visitFieldInsn(GETFIELD, "vazkii/botania/common/entity/EntityDoppleganger", "playersWhoAttacked", "Ljava/util/List;")
				mv.visitVarInsn(ALOAD, 4)
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "b_" else "getCommandSenderName", "()Ljava/lang/String;", false)
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
				mv.visitInsn(POP)
				mv.visitLabel(l6)
				mv.visitLineNumber(377, l6)
				mv.visitFrame(F_APPEND, 1, arrayOf<Any>(if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer"), 0, null)
				mv.visitVarInsn(FLOAD, 2)
				mv.visitVarInsn(FSTORE, 5)
				val l8 = Label()
				mv.visitLabel(l8)
				mv.visitLineNumber(378, l8)
				mv.visitInsn(ICONST_0)
				mv.visitVarInsn(ISTORE, 6)
				val l9 = Label()
				mv.visitLabel(l9)
				mv.visitLineNumber(379, l9)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitTypeInsn(INSTANCEOF, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer")
				val l10 = Label()
				mv.visitJumpInsn(IFEQ, l10)
				val l11 = Label()
				mv.visitLabel(l11)
				mv.visitLineNumber(380, l11)
				mv.visitVarInsn(ALOAD, 3)
				mv.visitTypeInsn(CHECKCAST, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer")
				mv.visitVarInsn(ASTORE, 7)
				val l12 = Label()
				mv.visitLabel(l12)
				mv.visitLineNumber(381, l12)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitFieldInsn(GETFIELD, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "R" else "fallDistance", "F")
				mv.visitInsn(FCONST_0)
				mv.visitInsn(FCMPL)
				val l13 = Label()
				mv.visitJumpInsn(IFLE, l13)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitFieldInsn(GETFIELD, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "D" else "onGround", "Z")
				mv.visitJumpInsn(IFNE, l13)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "h_" else "isOnLadder", "()Z", false)
				mv.visitJumpInsn(IFNE, l13)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "M" else "isInWater", "()Z", false)
				mv.visitJumpInsn(IFNE, l13)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitFieldInsn(GETSTATIC, if (OBF) "rv" else "net/minecraft/potion/Potion", if (OBF) "q" else "blindness", if (OBF) "Lrv;" else "Lnet/minecraft/potion/Potion;")
				mv.visitMethodInsn(INVOKEVIRTUAL, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "a" else "isPotionActive", if (OBF) "(Lrv;)Z" else "(Lnet/minecraft/potion/Potion;)Z", false)
				mv.visitJumpInsn(IFNE, l13)
				mv.visitVarInsn(ALOAD, 7)
				mv.visitFieldInsn(GETFIELD, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", if (OBF) "m" else "ridingEntity", if (OBF) "Lsa;" else "Lnet/minecraft/entity/Entity;")
				mv.visitJumpInsn(IFNONNULL, l13)
				mv.visitInsn(ICONST_1)
				val l14 = Label()
				mv.visitJumpInsn(GOTO, l14)
				mv.visitLabel(l13)
				mv.visitFrame(F_APPEND, 3, arrayOf<Any>(FLOAT, INTEGER, if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer"), 0, null)
				mv.visitInsn(ICONST_0)
				mv.visitLabel(l14)
				mv.visitFrame(F_SAME1, 0, null, 1, arrayOf<Any>(INTEGER))
				mv.visitVarInsn(ISTORE, 6)
				mv.visitLabel(l10)
				mv.visitLineNumber(384, l10)
				mv.visitFrame(F_CHOP, 1, null, 0, null)
				mv.visitVarInsn(ILOAD, 6)
				val l15 = Label()
				mv.visitJumpInsn(IFEQ, l15)
				mv.visitIntInsn(BIPUSH, 60)
				val l16 = Label()
				mv.visitJumpInsn(GOTO, l16)
				mv.visitLabel(l15)
				mv.visitFrame(F_SAME, 0, null, 0, null)
				mv.visitIntInsn(BIPUSH, 40)
				mv.visitLabel(l16)
				mv.visitFrame(F_SAME1, 0, null, 1, arrayOf<Any>(INTEGER))
				mv.visitVarInsn(ISTORE, 7)
				val l17 = Label()
				mv.visitLabel(l17)
				mv.visitLineNumber(385, l17)
				mv.visitVarInsn(ALOAD, 0)
				mv.visitVarInsn(ALOAD, 1)
				mv.visitVarInsn(ILOAD, 7)
				mv.visitInsn(I2F)
				mv.visitVarInsn(FLOAD, 5)
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "min", "(FF)F", false)
				mv.visitVarInsn(ALOAD, 0)
				mv.visitMethodInsn(INVOKEVIRTUAL, "vazkii/botania/common/entity/EntityDoppleganger", "isHardMode", "()Z", false)
				val l18 = Label()
				mv.visitJumpInsn(IFEQ, l18)
				mv.visitLdcInsn(0.6f)
				val l19 = Label()
				mv.visitJumpInsn(GOTO, l19)
				mv.visitLabel(l18)
				mv.visitFrame(F_FULL, 8, arrayOf<Any>("vazkii/botania/common/entity/EntityDoppleganger", if (OBF) "ro" else "net/minecraft/util/DamageSource", FLOAT, if (OBF) "sa" else "net/minecraft/entity/Entity", if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", FLOAT, INTEGER, INTEGER), 3, arrayOf<Any>("vazkii/botania/common/entity/EntityDoppleganger", if (OBF) "ro" else "net/minecraft/util/DamageSource", FLOAT))
				mv.visitInsn(FCONST_1)
				mv.visitLabel(l19)
				mv.visitFrame(F_FULL, 8, arrayOf<Any>("vazkii/botania/common/entity/EntityDoppleganger", if (OBF) "ro" else "net/minecraft/util/DamageSource", FLOAT, if (OBF) "sa" else "net/minecraft/entity/Entity", if (OBF) "yz" else "net/minecraft/entity/player/EntityPlayer", FLOAT, INTEGER, INTEGER), 4, arrayOf<Any>("vazkii/botania/common/entity/EntityDoppleganger", if (OBF) "ro" else "net/minecraft/util/DamageSource", FLOAT, FLOAT))
				mv.visitInsn(FMUL)
				mv.visitMethodInsn(INVOKESPECIAL, if (OBF) "td" else "net/minecraft/entity/EntityCreature", if (OBF) "a" else "attackEntityFrom", if (OBF) "(Lro;F)Z" else "(Lnet/minecraft/util/DamageSource;F)Z", false)
				mv.visitInsn(IRETURN)
				mv.visitLabel(l3)
				mv.visitLineNumber(387, l3)
				mv.visitFrame(F_FULL, 4, arrayOf<Any>("vazkii/botania/common/entity/EntityDoppleganger", if (OBF) "ro" else "net/minecraft/util/DamageSource", FLOAT, if (OBF) "sa" else "net/minecraft/entity/Entity"), 0, arrayOf())
				mv.visitInsn(ICONST_0)
				mv.visitInsn(IRETURN)
				val l20 = Label()
				mv.visitLabel(l20)
				mv.visitLocalVariable("this", "Lvazkii/botania/common/entity/EntityDoppleganger;", null, l0, l20, 0)
				mv.visitLocalVariable("par1DamageSource", if (OBF) "Lro;" else "Lnet/minecraft/util/DamageSource;", null, l0, l20, 1)
				mv.visitLocalVariable("par2", "F", null, l0, l20, 2)
				mv.visitLocalVariable("e", if (OBF) "Lsa;" else "Lnet/minecraft/entity/Entity;", null, l1, l20, 3)
				mv.visitLocalVariable("player", if (OBF) "yz" else "Lnet/minecraft/entity/player/EntityPlayer;", null, l5, l3, 4)
				mv.visitLocalVariable("dmg", "F", null, l8, l3, 5)
				mv.visitLocalVariable("crit", "Z", null, l9, l3, 6)
				mv.visitLocalVariable("p", if (OBF) "yz" else "Lnet/minecraft/entity/player/EntityPlayer;", null, l12, l10, 7)
				mv.visitLocalVariable("cap", "I", null, l17, l3, 7)
				mv.visitMaxs(4, 8)
				mv.visitEnd()
				return mv
			} else if (name == "getBossBarTextureRect") {
				println("Visiting EntityDoppleganger#getBossBarTextureRect: $name$desc")
				return `EntityDoppleganger$getBossBarTextureRect$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `EntityDoppleganger$getBossBarTextureRect$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			var inject = 2
			
			override fun visitInsn(opcode: Int) {
				if (opcode == ICONST_0 && --inject == 0)
					super.visitIntInsn(BIPUSH, AlfheimConfigHandler.gaiaBarOffset * 22)
				else
					super.visitInsn(opcode)
			}
		}
	}
	
	internal class `ItemInfiniEffect$ClassVisitor`(val className: String, cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "onWornTick") {
				println("Visiting $className#onWornTick: $name$desc")
				return `ItemInfiniEffect$onWornTick$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemInfiniEffect$onWornTick$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			override fun visitLdcInsn(cst: Any?) {
				if (cst == Integer.MAX_VALUE)
					super.visitLdcInsn(20)
				else
					super.visitLdcInsn(cst)
			}
		}
	}
	
	internal class `ItemLens$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitField(access: Int, name: String, desc: String, signature: String?, value: Any?): FieldVisitor {
			var value = value
			if (name == "SUBTYPES") {
				value = 24
			}
			return super.visitField(access, name, desc, signature, value)
		}
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			val mv = super.visitMethod(access, name, desc, signature, exceptions)
			
			println("Visiting ItemLens#$name: $name$desc")
			return `ItemLens$MethodVisitor`(mv)
		}
		
		internal class `ItemLens$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			companion object {
				var left = 2
			}
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				var operand = operand
				if (opcode == BIPUSH) {
					if (operand == 22) {        // 4 injections for #SUBTYPES
						operand = 24
					} else if (operand == 21) { // 2 injections for #SUBTYPES-1
						if (left-- > 0) {       // 4 injections total
							operand = 23
						}
					}
				}
				
				super.visitIntInsn(opcode, operand)
			}
		}
	}
	
	internal class `ItemRelic$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "addBindInfo") {
				println("Visiting ItemRelic#addBindInfo: $name$desc")
				return `ItemRelic$addBindInfo$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemRelic$addBindInfo$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			private var perform = false
			
			override fun visitJumpInsn(opcode: Int, label: Label) {
				if (opcode == IF_ACMPNE) {
					if (!perform) {
						perform = true
						super.visitJumpInsn(opcode, label)
					} else {
						visitInsn(POP2)
						visitInsn(ICONST_1)
						super.visitJumpInsn(IFEQ, label)
					}
				} else {
					super.visitJumpInsn(opcode, label)
				}
			}
		}
	}
	
	internal class `ItemTerraformRod$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "<clinit>") {
				println("Visiting ItemTerraformRod#<clinit>: $name$desc")
				return `ItemTerraformRod$clinit$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ItemTerraformRod$clinit$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			private var injectLength = true
			private var injectNew = false
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				var operand = operand
				if (opcode == BIPUSH) {
					if (operand == 20) {
						if (injectLength) {
							injectLength = false
							operand = 21
						}
					} else if (operand == 19) {
						injectNew = true
					}
				}
				super.visitIntInsn(opcode, operand)
			}
			
			override fun visitInsn(opcode: Int) {
				super.visitInsn(opcode)
				if (opcode == AASTORE && injectNew) {
					super.visitInsn(DUP)
					super.visitIntInsn(BIPUSH, 20)
					super.visitLdcInsn("livingrock")
					super.visitInsn(AASTORE)
				}
			}
		}
	}
	
	internal class `LibItemNames$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "<clinit>") {
				println("Visiting LibItemNames#<clinit>: $name$desc")
				return `LibItemNames$clinit$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `LibItemNames$clinit$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			
			private var twotwo_twofour = true
			private var add = false
			private var twoone = true
			
			override fun visitIntInsn(opcode: Int, operand: Int) {
				var operand = operand
				if (opcode == BIPUSH) {
					if (operand == 22) {
						if (twotwo_twofour) {
							twotwo_twofour = false
							operand = 24
						}
					}
					
					if (operand == 21) {
						if (twoone) {
							twoone = false
							add = true
						}
					}
				}
				
				super.visitIntInsn(opcode, operand)
			}
			
			override fun visitInsn(opcode: Int) {
				super.visitInsn(opcode)
				
				if (opcode == AASTORE) {
					if (add) {
						add = false
						mv.visitInsn(DUP)
						mv.visitIntInsn(BIPUSH, 22)
						mv.visitLdcInsn("lensMessenger")
						mv.visitInsn(AASTORE)
						mv.visitInsn(DUP)
						mv.visitIntInsn(BIPUSH, 23)
						mv.visitLdcInsn("lensTripwire")
						mv.visitInsn(AASTORE)
					}
				}
			}
		}
	}
	
	internal class `ClientEvents$GUIOverlay$ClassVisitor`(cv: ClassVisitor): ClassVisitor(ASM5, cv) {
		
		override fun visitMethod(access: Int, name: String, desc: String, signature: String?, exceptions: Array<String>?): MethodVisitor {
			if (name == "renderHotbar") {
				println("Visiting witchery's ClientEvents\$GUIOverlay#renderHotbar: $name$desc")
				return `ClientEvents$GUIOverlay$renderHotbar$MethodVisitor`(super.visitMethod(access, name, desc, signature, exceptions))
			}
			return super.visitMethod(access, name, desc, signature, exceptions)
		}
		
		internal class `ClientEvents$GUIOverlay$renderHotbar$MethodVisitor`(mv: MethodVisitor): MethodVisitor(ASM5, mv) {
			var aload1 = false
			
			override fun visitVarInsn(opcode: Int, operand: Int) {
				if (opcode == ALOAD) {
					aload1 = operand == 1
				}
				
				super.visitVarInsn(opcode, operand)
			}
			
			override fun visitInsn(opcode: Int) {
				super.visitInsn(if (opcode == ICONST_1) ICONST_0 else opcode)
			}
		}
	}
}