package alfheim.common.core.asm;

import static alfheim.api.ModInfo.OBF;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class AlfheimClassTransformer implements IClassTransformer {
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.entity.EntityTrackerEntry")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			EntityTrackerEntry$ClassVisitor ct = new EntityTrackerEntry$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("net.minecraft.potion.Potion")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			Potion$ClassVisitor ct = new Potion$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("net.minecraft.server.management.ItemInWorldManager")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemInWorldManager$ClassVisitor ct = new ItemInWorldManager$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("net.minecraft.world.World")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			World$ClassVisitor ct = new World$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("thaumcraft.common.items.ItemNugget")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemNugget$ClassVisitor ct = new ItemNugget$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.client.core.handler.BaubleRenderHandler")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			BaubleRenderHandler$ClassVisitor ct = new BaubleRenderHandler$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.common.entity.EntityDoppleganger")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			EntityDoppleganger$ClassVisitor ct = new EntityDoppleganger$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.common.lib.LibItemNames")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			LibItemNames$ClassVisitor ct = new LibItemNames$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.common.item.lens.ItemLens")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemLens$ClassVisitor ct = new ItemLens$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.common.item.relic.ItemRelic")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemRelic$ClassVisitor ct = new ItemRelic$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		if (transformedName.equals("vazkii.botania.common.item.rod.ItemTerraformRod")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ItemTerraformRod$ClassVisitor ct = new ItemTerraformRod$ClassVisitor(cw);
			cr.accept(ct, ClassReader.SKIP_FRAMES);
			return cw.toByteArray();
		} else
		
		return basicClass;
	}
	
	static class EntityTrackerEntry$ClassVisitor extends ClassVisitor {
		
		public EntityTrackerEntry$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("tryStartWachingThis") || (name.equals("b") && desc.equals("(Lmw;)V"))) {
				System.out.println("Visiting EntityTrackerEntry#tryStartWachingThis: " + name + desc);
				return new EntityTrackerEntry$tryStartWachingThis$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class EntityTrackerEntry$tryStartWachingThis$MethodVisitor extends MethodVisitor {
			
			public EntityTrackerEntry$tryStartWachingThis$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			boolean sended = false;
			
			@Override
			public void visitVarInsn(int opcode, int var) {
				super.visitVarInsn(opcode, var);
				if (opcode == ALOAD && var == 6 && !sended) {
					sended = true;
					visitVarInsn(ALOAD, 0);
					visitFieldInsn(GETFIELD, OBF ? "my" : "net/minecraft/entity/EntityTrackerEntry", OBF ? "a" : "myEntity", OBF ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
					visitMethodInsn(INVOKESTATIC, "alfheim/common/core/handler/CardinalSystem$PartySystem", "notifySpawn", OBF ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V", false);
				}
			}
		}
	}
	
	static class Potion$ClassVisitor extends ClassVisitor {
		
		public Potion$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("performEffect") || (name.equals("a") && desc.equals("(Lsv;I)V"))) {
				System.out.println("Visiting Potion#performEffect: " + name + desc);
				return new Potion$performEffect$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class Potion$performEffect$MethodVisitor extends MethodVisitor {
			
			public Potion$performEffect$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			boolean flag = false;
			
			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String desc) {
				if (flag && opcode == GETSTATIC && (owner.equals("net/minecraft/util/DamageSource") || owner.equals("ro")) && (name.equals("magic") || name.equals("k")) && (desc.equals("Lnet/minecraft/util/DamageSource;") || desc.equals("Lro;"))) {
					flag = false;
					super.visitFieldInsn(GETSTATIC, "alfheim/common/core/util/DamageSourceSpell", "poison", OBF ? "Lro;" : "Lnet/minecraft/util/DamageSource;");					return;
				} else if (opcode == GETSTATIC && (owner.equals("net/minecraft/potion/Potion") || owner.equals("rv")) && (name.equals("poison") || name.equals("u")) && (desc.equals("Lnet/minecraft/potion/Potion;") || desc.equals("Lrv;"))) flag = true;
				
				super.visitFieldInsn(opcode, owner, name, desc);
			}
		}
	}
	
	static class ItemInWorldManager$ClassVisitor extends ClassVisitor {
		
		public ItemInWorldManager$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("onBlockClicked") || (name.equals("a") && desc.equals("(IIII)V"))) {
				System.out.println("Visiting ItemInWorldManager#onBlockClicked: " + name + desc);
				return new ItemRelic$onBlockClicked$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemRelic$onBlockClicked$MethodVisitor extends MethodVisitor {
			
			public ItemRelic$onBlockClicked$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == ACONST_NULL) {
					visitVarInsn(ALOAD, 0);
					visitFieldInsn(GETFIELD, OBF ? "mx" : "net/minecraft/server/management/ItemInWorldManager", OBF ? "b" : "thisPlayerMP", OBF ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;");
				} else {
					super.visitInsn(opcode);
				}
			}
			
			@Override
			public void visitTypeInsn(int opcode, String type) {
				if (opcode == CHECKCAST && type.equals(OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer")) {} else {
					super.visitTypeInsn(opcode, type);
				}
			}
		}
	}
	
	static class World$ClassVisitor extends ClassVisitor {
		
		public World$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (!AlfheimHookLoader.isThermos) {
				if (name.equals("updateEntities") || (name.equals("h") && desc.equals("()V"))) {
					System.out.println("Visiting World#updateEntities: " + name + desc);
					return new World$updateEntities$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
				}
			}
			if (name.equals("updateEntityWithOptionalForce") || (name.equals("a") && desc.equals("(Lsa;Z)V"))) {
				System.out.println("Visiting World#updateEntityWithOptionalForce: " + name + desc);
				return new World$updateEntityWithOptionalForce$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class World$updateEntities$MethodVisitor extends MethodVisitor {
			
			public World$updateEntities$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			boolean insert = true;
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
				if (insert && opcode == INVOKEVIRTUAL && owner.equals(OBF ? "aor" : "net/minecraft/tileentity/TileEntity") && name.equals(OBF ? "h" : "updateEntity") && desc.equals("()V") && !itf) {
					insert = false;
					super.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/TileUpdateEvent", "instantiate", OBF ? "(Laor;)Lalfheim/api/event/TileUpdateEvent;" : "(Lnet/minecraft/tileentity/TileEntity;)Lalfheim/api/event/TileUpdateEvent;", false);
					super.visitVarInsn(ASTORE, 9);
					super.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;");
					super.visitVarInsn(ALOAD, 9);
					super.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false);
					Label l84 = new Label();
					super.visitJumpInsn(IFNE, l84);
					super.visitVarInsn(ALOAD, 8);
					super.visitMethodInsn(INVOKEVIRTUAL, OBF ? "aor" : "net/minecraft/tileentity/TileEntity", OBF ? "h" : "updateEntity", "()V", false);
					super.visitLabel(l84);
					super.visitFrame(F_APPEND, 2, new Object[] {(OBF ? "aor" : "net/minecraft/tileentity/TileEntity"), "alfheim/api/event/TileUpdateEvent"}, 0, null);
					super.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/TileUpdateEvent", "stub", "()V", false);
				} else super.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}
		
		static class World$updateEntityWithOptionalForce$MethodVisitor extends MethodVisitor {
			
			public World$updateEntityWithOptionalForce$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
				if (opcode == INVOKEVIRTUAL && owner.equals(OBF ? "sa" : "net/minecraft/entity/Entity") && (name.equals(OBF ? "ab" : "updateRidden") || name.equals(OBF ? "h" : "onUpdate")) && desc.equals("()V") && !itf) {
					mv.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/EntityUpdateEvent", "instantiate", OBF ? "(Lsa;)Lalfheim/api/event/EntityUpdateEvent;" : "(Lnet/minecraft/entity/Entity;)Lalfheim/api/event/EntityUpdateEvent;", false);
					mv.visitVarInsn(ASTORE, 8);
					mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;");
					mv.visitVarInsn(ALOAD, 8);
					mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false);
					Label label = new Label();
					mv.visitJumpInsn(IFNE, label);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "sa" : "net/minecraft/entity/Entity", name, "()V", false);
					mv.visitLabel(label);
					mv.visitFrame(F_APPEND, 1, new Object[] {"alfheim/api/event/EntityUpdateEvent"}, 0, null);
					mv.visitMethodInsn(INVOKESTATIC, "alfheim/api/event/EntityUpdateEvent", "stub", "()V", false);
				} else super.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}
	}
	
	static class ItemNugget$ClassVisitor extends ClassVisitor {
		
		public ItemNugget$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (name.equals("registerIcons")) {
				System.out.println("Visiting ItemNugget#registerIcons: " + name + desc);
				return new ItemNugget$registerIcons$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			if (name.equals("getSubItems")) {
				System.out.println("Visiting ItemNugget#getSubItems: " + name + desc);
				return new ItemNugget$getSubItems$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemNugget$registerIcons$MethodVisitor extends MethodVisitor {
			
			public ItemNugget$registerIcons$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 0);
					visitFieldInsn(GETFIELD, "thaumcraft/common/items/ItemNugget", "icon", "[Lnet/minecraft/util/IIcon;");
					visitIntInsn(BIPUSH, AlfheimASMData.elementiumClusterMeta());
					visitVarInsn(ALOAD, 1);
					visitLdcInsn("thaumcraft:clusterelementium");
					visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/texture/IIconRegister", "registerIcon", "(Ljava/lang/String;)Lnet/minecraft/util/IIcon;", true);
					visitInsn(AASTORE);
					Label l15_5 = new Label();
					visitLabel(l15_5);
					visitLineNumber(47, l15_5);
				}
				super.visitInsn(opcode);
			}
		}
		
		static class ItemNugget$getSubItems$MethodVisitor extends MethodVisitor {
			
			public ItemNugget$getSubItems$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitInsn(int opcode) {
				if (opcode == RETURN) {
					visitVarInsn(ALOAD, 3);
					visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
					visitInsn(DUP);
					visitVarInsn(ALOAD, 0);
					visitInsn(ICONST_1);
					visitIntInsn(BIPUSH, AlfheimASMData.elementiumClusterMeta());
					visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
					visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
					visitInsn(POP);
				}
				super.visitInsn(opcode);
			}
		}
	}
	
	static class BaubleRenderHandler$ClassVisitor extends ClassVisitor {
		
		public BaubleRenderHandler$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (name.equals("renderManaTablet")) {
				System.out.println("Visiting BaubleRenderHandler#renderManaTablet: " + name + desc);
				return new BaubleRenderHandler$renderManaTablet$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class BaubleRenderHandler$renderManaTablet$MethodVisitor extends MethodVisitor {
			
			public BaubleRenderHandler$renderManaTablet$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			@Override
			public void visitLdcInsn(Object cst) {
				if (cst instanceof Float && ((Float) cst).floatValue() == 0.2F) cst = new Float(0.33F);
				super.visitLdcInsn(cst);
			}
		}
	}
	
	static class EntityDoppleganger$ClassVisitor extends ClassVisitor {
		
		public EntityDoppleganger$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override	// Just because!
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("attackEntityFrom") || (name.equals("a") && desc.equals("(Lro;F)Z"))) {
				System.out.println("Visiting EntityDoppleganger#attackEntityFrom: " + name + desc);
				MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, OBF ? "a" : "attackEntityFrom", OBF ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(371, l0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "ro" : "net/minecraft/util/DamageSource", OBF ? "j" : "getEntity", OBF ? "()Lsa;" : "()Lnet/minecraft/entity/Entity;", false);
				mv.visitVarInsn(ASTORE, 3);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(372, l1);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitFieldInsn(GETFIELD, OBF ? "ro" : "net/minecraft/util/DamageSource", OBF ? "o" : "damageType", "Ljava/lang/String;");
				mv.visitLdcInsn("player");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
				Label l2 = new Label();
				mv.visitJumpInsn(IFNE, l2);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(INSTANCEOF, "alfheim/common/core/util/DamageSourceSpell");
				mv.visitJumpInsn(IFNE, l2);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitTypeInsn(INSTANCEOF, "vazkii/botania/common/entity/EntityPixie");
				Label l3 = new Label();
				mv.visitJumpInsn(IFEQ, l3);
				mv.visitLabel(l2);
				mv.visitFrame(F_APPEND,1, new Object[] {OBF ? "sa" : "net/minecraft/entity/Entity"}, 0, null);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitJumpInsn(IFNULL, l3);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKESTATIC, "vazkii/botania/common/entity/EntityDoppleganger", "isTruePlayer", OBF ? "(Lsa;)Z" : "(Lnet/minecraft/entity/Entity;)Z", false);
				mv.visitJumpInsn(IFEQ, l3);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "vazkii/botania/common/entity/EntityDoppleganger", "getInvulTime", "()I", false);
				mv.visitJumpInsn(IFNE, l3);
				Label l4 = new Label();
				mv.visitLabel(l4);
				mv.visitLineNumber(373, l4);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitTypeInsn(CHECKCAST, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer");
				mv.visitVarInsn(ASTORE, 4);
				Label l5 = new Label();
				mv.visitLabel(l5);
				mv.visitLineNumber(374, l5);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "vazkii/botania/common/entity/EntityDoppleganger", "playersWhoAttacked", "Ljava/util/List;");
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "b_" : "getCommandSenderName", "()Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "contains", "(Ljava/lang/Object;)Z", true);
				Label l6 = new Label();
				mv.visitJumpInsn(IFNE, l6);
				Label l7 = new Label();
				mv.visitLabel(l7);
				mv.visitLineNumber(375, l7);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "vazkii/botania/common/entity/EntityDoppleganger", "playersWhoAttacked", "Ljava/util/List;");
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "b_" : "getCommandSenderName", "()Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
				mv.visitInsn(POP);
				mv.visitLabel(l6);
				mv.visitLineNumber(377, l6);
				mv.visitFrame(F_APPEND,1, new Object[] {OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer"}, 0, null);
				mv.visitVarInsn(FLOAD, 2);
				mv.visitVarInsn(FSTORE, 5);
				Label l8 = new Label();
				mv.visitLabel(l8);
				mv.visitLineNumber(378, l8);
				mv.visitInsn(ICONST_0);
				mv.visitVarInsn(ISTORE, 6);
				Label l9 = new Label();
				mv.visitLabel(l9);
				mv.visitLineNumber(379, l9);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitTypeInsn(INSTANCEOF, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer");
				Label l10 = new Label();
				mv.visitJumpInsn(IFEQ, l10);
				Label l11 = new Label();
				mv.visitLabel(l11);
				mv.visitLineNumber(380, l11);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitTypeInsn(CHECKCAST, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer");
				mv.visitVarInsn(ASTORE, 7);
				Label l12 = new Label();
				mv.visitLabel(l12);
				mv.visitLineNumber(381, l12);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitFieldInsn(GETFIELD, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "R" : "fallDistance", "F");
				mv.visitInsn(FCONST_0);
				mv.visitInsn(FCMPL);
				Label l13 = new Label();
				mv.visitJumpInsn(IFLE, l13);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitFieldInsn(GETFIELD, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "D" : "onGround", "Z");
				mv.visitJumpInsn(IFNE, l13);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "h_" : "isOnLadder", "()Z", false);
				mv.visitJumpInsn(IFNE, l13);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "M" : "isInWater", "()Z", false);
				mv.visitJumpInsn(IFNE, l13);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitFieldInsn(GETSTATIC, OBF ? "rv" : "net/minecraft/potion/Potion", OBF ? "q" : "blindness", OBF ? "Lrv;" : "Lnet/minecraft/potion/Potion;");
				mv.visitMethodInsn(INVOKEVIRTUAL, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "a" : "isPotionActive", OBF ? "(Lrv;)Z" : "(Lnet/minecraft/potion/Potion;)Z", false);
				mv.visitJumpInsn(IFNE, l13);
				mv.visitVarInsn(ALOAD, 7);
				mv.visitFieldInsn(GETFIELD, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", OBF ? "m" : "ridingEntity", OBF ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
				mv.visitJumpInsn(IFNONNULL, l13);
				mv.visitInsn(ICONST_1);
				Label l14 = new Label();
				mv.visitJumpInsn(GOTO, l14);
				mv.visitLabel(l13);
				mv.visitFrame(F_APPEND,3, new Object[] {FLOAT, INTEGER, OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer"}, 0, null);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(l14);
				mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {INTEGER});
				mv.visitVarInsn(ISTORE, 6);
				mv.visitLabel(l10);
				mv.visitLineNumber(384, l10);
				mv.visitFrame(F_CHOP,1, null, 0, null);
				mv.visitVarInsn(ILOAD, 6);
				Label l15 = new Label();
				mv.visitJumpInsn(IFEQ, l15);
				mv.visitIntInsn(BIPUSH, 60);
				Label l16 = new Label();
				mv.visitJumpInsn(GOTO, l16);
				mv.visitLabel(l15);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				mv.visitIntInsn(BIPUSH, 40);
				mv.visitLabel(l16);
				mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {INTEGER});
				mv.visitVarInsn(ISTORE, 7);
				Label l17 = new Label();
				mv.visitLabel(l17);
				mv.visitLineNumber(385, l17);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 7);
				mv.visitInsn(I2F);
				mv.visitVarInsn(FLOAD, 5);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "min", "(FF)F", false);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "vazkii/botania/common/entity/EntityDoppleganger", "isHardMode", "()Z", false);
				Label l18 = new Label();
				mv.visitJumpInsn(IFEQ, l18);
				mv.visitLdcInsn(new Float("0.6"));
				Label l19 = new Label();
				mv.visitJumpInsn(GOTO, l19);
				mv.visitLabel(l18);
				mv.visitFrame(F_FULL, 8, new Object[] {"vazkii/botania/common/entity/EntityDoppleganger", OBF ? "ro" : "net/minecraft/util/DamageSource", FLOAT, OBF ? "sa" : "net/minecraft/entity/Entity", OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", FLOAT, INTEGER, INTEGER}, 3, new Object[] {"vazkii/botania/common/entity/EntityDoppleganger", OBF ? "ro" : "net/minecraft/util/DamageSource", FLOAT});
				mv.visitInsn(FCONST_1);
				mv.visitLabel(l19);
				mv.visitFrame(F_FULL, 8, new Object[] {"vazkii/botania/common/entity/EntityDoppleganger", OBF ? "ro" : "net/minecraft/util/DamageSource", FLOAT, OBF ? "sa" : "net/minecraft/entity/Entity", OBF ? "yz" : "net/minecraft/entity/player/EntityPlayer", FLOAT, INTEGER, INTEGER}, 4, new Object[] {"vazkii/botania/common/entity/EntityDoppleganger", OBF ? "ro" : "net/minecraft/util/DamageSource", FLOAT, FLOAT});
				mv.visitInsn(FMUL);
				mv.visitMethodInsn(INVOKESPECIAL, OBF ? "td" : "net/minecraft/entity/EntityCreature", OBF ? "a" : "attackEntityFrom", OBF ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z", false);
				mv.visitInsn(IRETURN);
				mv.visitLabel(l3);
				mv.visitLineNumber(387, l3);
				mv.visitFrame(F_FULL, 4, new Object[] {"vazkii/botania/common/entity/EntityDoppleganger", OBF ? "ro" : "net/minecraft/util/DamageSource", FLOAT, OBF ? "sa" : "net/minecraft/entity/Entity"}, 0, new Object[] {});
				mv.visitInsn(ICONST_0);
				mv.visitInsn(IRETURN);
				Label l20 = new Label();
				mv.visitLabel(l20);
				mv.visitLocalVariable("this", "Lvazkii/botania/common/entity/EntityDoppleganger;", null, l0, l20, 0);
				mv.visitLocalVariable("par1DamageSource", OBF ? "Lro;" : "Lnet/minecraft/util/DamageSource;", null, l0, l20, 1);
				mv.visitLocalVariable("par2", "F", null, l0, l20, 2);
				mv.visitLocalVariable("e", OBF ? "Lsa;" : "Lnet/minecraft/entity/Entity;", null, l1, l20, 3);
				mv.visitLocalVariable("player", OBF ? "yz" : "Lnet/minecraft/entity/player/EntityPlayer;", null, l5, l3, 4);
				mv.visitLocalVariable("dmg", "F", null, l8, l3, 5);
				mv.visitLocalVariable("crit", "Z", null, l9, l3, 6);
				mv.visitLocalVariable("p", OBF ? "yz" : "Lnet/minecraft/entity/player/EntityPlayer;", null, l12, l10, 7);
				mv.visitLocalVariable("cap", "I", null, l17, l3, 7);
				mv.visitMaxs(4, 8);
				mv.visitEnd();
				return mv;
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
	
	static class LibItemNames$ClassVisitor extends ClassVisitor {
		
		public LibItemNames$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (name.equals("<clinit>")) {
				System.out.println("Visiting LibItemNames#<clinit>: " + name + desc);
				return new LibItemNames$clinit$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class LibItemNames$clinit$MethodVisitor extends MethodVisitor {
			
			public LibItemNames$clinit$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			private boolean twotwo_twofour = true, add = false, twoone = true;
			
			@Override
			public void visitIntInsn(int opcode, int operand) {
				if (opcode == BIPUSH) {
					if (operand == 22) {
						if (twotwo_twofour) {
							twotwo_twofour = false;
							operand = 24;
						}
					} 
					
					if (operand == 21) {
						if (twoone) {
							twoone = false;
							add = true;
						}
					}
				}
				
				super.visitIntInsn(opcode, operand);
			}
			
			@Override
			public void visitInsn(int opcode) {
				super.visitInsn(opcode);
				
				if (opcode == AASTORE) {
					if (add) {
						add = false;
						mv.visitInsn(DUP);
						mv.visitIntInsn(BIPUSH, 22);
						mv.visitLdcInsn("lensMessenger");
						mv.visitInsn(AASTORE);
						mv.visitInsn(DUP);
						mv.visitIntInsn(BIPUSH, 23);
						mv.visitLdcInsn("lensTripwire");
						mv.visitInsn(AASTORE);
					}
				}
			}
		}
	}
	
	static class ItemLens$ClassVisitor extends ClassVisitor {
		
		public ItemLens$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			if (name.equals("SUBTYPES")) {
				value = new Integer(24);
			}
			return super.visitField(access, name, desc, signature, value);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			System.out.println("Visiting ItemLens#" + name + ": " + name + desc);
			return new ItemLens$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
		}
		
		static class ItemLens$MethodVisitor extends MethodVisitor {
			
			public ItemLens$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			int left = 2;
			
			@Override
			public void visitIntInsn(int opcode, int operand) {
				if (opcode == BIPUSH) {
					if (operand == 22) {
						operand = 24;
					} else if (operand == 21) {
						if (left--> 0) {
							operand = 23;
						}
					}
				}
				
				super.visitIntInsn(opcode, operand);
			}
		}
	}
	
	static class ItemRelic$ClassVisitor extends ClassVisitor {
		
		public ItemRelic$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("addBindInfo")) {
				System.out.println("Visiting ItemRelic#addBindInfo: " + name + desc);
				return new ItemRelic$addBindInfo$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemRelic$addBindInfo$MethodVisitor extends MethodVisitor {
			
			public ItemRelic$addBindInfo$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			private boolean perform = false;
			
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
	}
	
	static class ItemTerraformRod$ClassVisitor extends ClassVisitor {
		
		public ItemTerraformRod$ClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
			if (name.equals("<clinit>")) {
				System.out.println("Visiting ItemTerraformRod#<clinit>: " + name + desc);
				return new ItemTerraformRod$clinit$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
		static class ItemTerraformRod$clinit$MethodVisitor extends MethodVisitor {
			
			public ItemTerraformRod$clinit$MethodVisitor(MethodVisitor mv) {
				super(ASM5, mv);
			}
			
			boolean injectLength = true;
			boolean injectNew = false;
			
			@Override
			public void visitIntInsn(int opcode, int operand) {
				if (opcode == BIPUSH) {
					if (operand == 20) {
						if (injectLength) {
							injectLength = false;
							operand = 21;
						}
					} else if (operand == 19) {
						injectNew = true;
					}
				}
				super.visitIntInsn(opcode, operand);
			}
			
			@Override
			public void visitInsn(int opcode) {
				super.visitInsn(opcode);
				if (opcode == AASTORE && injectNew) {
					super.visitInsn(DUP);
					super.visitIntInsn(BIPUSH, 20);
					super.visitLdcInsn("livingrock");
					super.visitInsn(AASTORE);
				}
			}
		}
	}
}