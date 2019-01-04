package alfheim.client.render.world;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient;
import alfheim.client.gui.GUIDeathTimer;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageParticles;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Note;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelMiniIsland;
import vazkii.botania.common.Botania;

public class SpellEffectHandlerClient {
	
	public static void select(Spells s, double x, double y, double z, double x2, double y2, double z2) {
		if (AlfheimCore.enableMMO) {
			switch (s) {
				case ACID: spawnAcid(x, y, z); break; 
				case AQUABIND: spawnAquaBind(x, y, z); break;
				case AQUASTREAM: spawnAquaStream(x, y, z, x2, y2, z2); break;
				case AQUASTREAM_HIT: spawnAquaStreamHit(x, y, z); break;
				case DISPEL: spawnBurst(x, y, z, 1, 0, 0); break;
				case ECHO: spawnEcho(x, y, z); break;
				case ECHO_ENTITY: spawnEchoEntity(x, y, z); break;
				case ECHO_ITEM: spawnEchoItem(x, y, z); break;
				case ECHO_MOB: spawnEchoMob(x, y, z); break;
				case ECHO_PLAYER: spawnEchoPlayer(x, y, z); break;
				case EXPL: spawnExplosion(x, y, z); break;
				case GRAVITY: spawnGravity(x, y, z, x2, y2, z2); break;
				case HEAL: spawnBurst(x, y, z, 0, 1, 0); break;
				case ICELENS: addIceLens(); break;
				case MANA: addMana(x, y); break;
				case NOTE: spawnNote(x, y, z); break;
				case NVISION: spawnBurst(x, y, z, 0, 0, 1); break;
				case PURE: spawnBurst(x, y, z, 0, 0.75F, 1); break;
				case PURE_AREA: spawnPure(x, y, z); break;
				case QUAD: quadDamage(); break;
				case QUADH: quadHurt(); break;
				case SMOKE: spawnSmoke(x, y, z); break;
				case THROW: spawnThrow(x, y, z, x2, y2, z2); break;
				case TREMORS: spawnTremors(x, y, z); break;
				case UPHEAL: spawnBurst(x, y, z, 1, 0.75F, 0); break;
			}
		}
	}

	public static void addIceLens() {
		Minecraft.getMinecraft().thePlayer.addPotionEffect(new PotionEffect(AlfheimRegistry.icelens.id, 200, 0, true));
	}
	
	public static void addMana(double enID, double mana) {
		Entity e = Minecraft.getMinecraft().theWorld.getEntityByID((int) enID);
		if (e != null && e instanceof EntityPlayer) {
			if (mana == 0) for (double d = 0.; d < 1.; d+= .2) spawnBurst(e.posX, e.posY + d, e.posZ, 0.975F, 0.85F, 0.1F);
			else ((EntityPlayer) e).addPotionEffect(new PotionEffect(AlfheimRegistry.showMana.id, (int) mana, 100, true));
		}
	}
	
	public static void quadDamage() {
		Minecraft.getMinecraft().thePlayer.playSound(ModInfo.MODID + ":quad", 10, 1);
	}
	
	public static void quadHurt() {
		Minecraft.getMinecraft().thePlayer.playSound(ModInfo.MODID + ":quadh", 1, 1);
	}

	public static void spawnAcid(double x, double y, double z) {
		Vector3 v = new Vector3();
		for (int i = 0; i < 256; i++) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * 9, Math.random() * 9, Math.random() * 9);
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + v.x, y + v.y, z + v.z, (float) (Math.random() * 0.2), 1, 0, 2, 0, 2);
		}
	}
	
	public static void spawnAquaBind(double x, double y, double z) {
		for (int i = 0; i < 360; i += 5) {
			double X = Math.cos(i) * 3.5;
			double Z = Math.sin(i) * 3.5;
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + X, y, z + Z, 0, 0.5F, 1, 0.5F);
		}
	}

	public static void spawnAquaStream(double x, double y, double z, double x2, double y2, double z2) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0.5F, 0.5F, 1, 1.0F, (float) x2, (float) y2, (float) z2, 0.5F);
	}

	public static void spawnAquaStreamHit(double x, double y, double z) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 0, 0.5F, 1, 0.5F);
	}

	public static void spawnBurst(double x, double y, double z, float r, float g, float b) {
		for (int i = 0; i < 8; i++) Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, r, g, b, 1, (float) (Math.random() * -0.075));
	}

	public static void spawnEcho(double x, double y, double z) {
		Vector3 m = new Vector3();
		for (int i = 0; i < 64; i++) {
			m.set(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(0.5);
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0.5F, 0.5F, 0.5F, 1, (float) m.x, 0, (float) m.z);
		}
	}

	public static void spawnEchoEntity(double x, double y, double z) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 1, 1, 0, 1, 0, 3);
	}

	public static void spawnEchoItem(double x, double y, double z) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0, 1, 0, 1, 0, 3);
	}

	public static void spawnEchoMob(double x, double y, double z) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 1, 0, 0, 1, 0, 3);
	}

	public static void spawnEchoPlayer(double x, double y, double z) {
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0, 0, 1, 1, 0, 3);
	}

	public static void spawnExplosion(double x, double y, double z) {
		Minecraft.getMinecraft().theWorld.spawnParticle("largeexplode", x, y, z, 1, 0, 0);
        
        Vector3 v = new Vector3();
        for (int i = 0; i < 32; i++) {
        	v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.15);
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y, z, 1, (float) Math.random() * 0.25F, (float) Math.random() * 0.075F, 0.25F + (float) Math.random() * 0.45F, (float) v.x, (float) v.y, (float) v.z, 0.5F);
        }
	}
	
	public static void spawnGravity(double x, double y, double z, double x2, double y2, double z2) {
		Minecraft.getMinecraft().theWorld.spawnParticle("smoke", x, y, z, x2, y2, z2);
	}
	
	public static void spawnMana(EntityLivingBase living, double mana) {
		if (mana == 0) {
			
		}
		double d = Math.random() * (mana *= 0.5);
		Vector3 v = new Vector3(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(Math.random()).mul((1.0 * (mana * 0.25)) - (d / mana * (mana * 2.0 / 7.0))).add(0, d, 0);
		Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, living.posX + v.x, living.posY + v.y, living.posZ + v.z,
					0.025F, 0.15F, 0.9F, (float) (Math.random() * (mana * 0.5) + 0.5),
					0, (float) (Math.random() * 2.0 + 1.0));
	}
	
	public static void spawnNote(double x, double y, double z) {
		Minecraft.getMinecraft().theWorld.spawnParticle("note", x, ++y, z, Minecraft.getMinecraft().theWorld.rand.nextInt(25) / 24.0, 0, 0);
	}
	
	// TODO change to OSM particles
	public static void spawnSmoke(double x, double y, double z) {
		Vector3 pos = new Vector3();
		for (int i = 0; i < 256; i++) {
			pos.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Minecraft.getMinecraft().theWorld.rand.nextInt(15) + Math.random());
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + pos.x, y + pos.y, z + pos.z, 0.1F, 0.1F, 0.1F, (float) (Math.random() * 4 + 4), (float) (Math.random() * -0.075));
		}
	}
	
	public static void spawnPure(double x, double y, double z) {
		Vector3 m = new Vector3();
		for (int i = 0; i < 64; i++) {
			m.set(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(0.2);
			Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x, y + 0.2, z, 0, 0.75F, 1, 1, (float) m.x, 0, (float) m.z);
		}
	}

	public static void spawnThrow(double x, double y, double z, double x2, double y2, double z2) {
		for (int i = 0; i < 8; i++) Botania.proxy.wispFX(Minecraft.getMinecraft().theWorld, x + Math.random() - 0.5, y + Math.random() * 0.25, z + Math.random() - 0.5, 0, 1, 0.25F, 1, (float) x2, (float) y2, (float) z2);
	}
	
	public static void spawnTremors(double x, double y, double z) {
		Vector3 m = new Vector3();
		Block block = Minecraft.getMinecraft().theWorld.getBlock(MathHelper.floor_double(x), MathHelper.floor_double(y) - 1, MathHelper.floor_double(z));
		int meta = Minecraft.getMinecraft().theWorld.getBlockMetadata(MathHelper.floor_double(x), MathHelper.floor_double(y) - 1, MathHelper.floor_double(z));
		for (int i = 0; i < 512; i++) {
			m.set(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(Math.random() * 1.5 + 0.5).set(m.x, Math.random() * 0.25, m.z);
			Minecraft.getMinecraft().theWorld.spawnParticle("blockdust_" + Block.getIdFromBlock(block) + "_" + meta, x, y + 0.25, z, m.x, m.y, m.z);
		}
	}
	
	public static enum Spells {
		ACID, AQUABIND, AQUASTREAM, AQUASTREAM_HIT, DISPEL, ECHO, ECHO_ENTITY, ECHO_ITEM, ECHO_MOB, ECHO_PLAYER, EXPL, GRAVITY, HEAL, ICELENS, MANA, NOTE, NVISION, PURE, PURE_AREA, QUAD, QUADH, SMOKE, THROW, TREMORS, UPHEAL
	}

	public static void onDeath(EntityLivingBase target) {
		if(AlfheimCore.enableMMO && Minecraft.getMinecraft().thePlayer == target) {
			Minecraft.getMinecraft().displayGuiScreen(new GUIDeathTimer());
			Minecraft.getMinecraft().thePlayer.hurtTime = 0;
		}
	}

	public static void onDeathTick(EntityLivingBase target) {
		if (AlfheimCore.enableMMO) {
			int c = 0xFFFFFF;
			if (target instanceof EntityPlayer && EnumRace.getRace((EntityPlayer) target) != EnumRace.HUMAN) c = EnumRace.getRace((EntityPlayer) target).getRGBColor();
			Botania.proxy.wispFX(target.worldObj, target.posX, target.posY - (Minecraft.getMinecraft().thePlayer == target ? 1.5 : 0), target.posZ, ((c >> 16) & 0xFF) / 255F, ((c >> 8) & 0xFF) / 255F, (c & 0xFF) / 255F, (float)(Math.random() * 0.5), (float)(Math.random() * 0.015 - 0.0075), (float)(Math.random() * 0.025), (float)(Math.random() * 0.015 - 0.0075), 2);
		}
	}

	public static void onRespawned() {
		Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
		Minecraft.getMinecraft().setIngameFocus();
	}
}