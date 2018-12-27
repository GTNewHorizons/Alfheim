package alfheim.common.core.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static alfheim.api.ModInfo.OBF;

import alfheim.common.core.handler.CardinalSystem.ManaSystem;
import alfheim.common.core.util.AlfheimBotaniaModifiers;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

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
			
		if (transformedName.equals("vazkii.botania.common.entity.EntityDoppleganger")) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			EntityDoppleganger$ClassVisitor ct = new EntityDoppleganger$ClassVisitor(cw);
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
					super.visitFieldInsn(GETSTATIC, "alfheim/api/spell/DamageSourceSpell", "poison", OBF ? "Lro;" : "Lnet/minecraft/util/DamageSource;");
					return;
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
			if (name.equals("updateEntities") || (name.equals("h") && desc.equals("()V"))) {
				System.out.println("Visiting World#updateEntities: " + name + desc);
				return new World$updateEntities$MethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
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
				mv.visitTypeInsn(INSTANCEOF, "alfheim/api/spell/DamageSourceSpell");
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
}