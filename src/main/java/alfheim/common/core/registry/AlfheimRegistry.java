package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static alfheim.api.AlfheimAPI.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;

import java.lang.reflect.Method;
import java.util.UUID;

import alexsocol.asjlib.ASJReflectionHelper;
import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.client.render.world.SpellEffectHandlerClient;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.block.tile.*;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import alfheim.common.entity.*;
import alfheim.common.entity.boss.EntityFlugel;
import alfheim.common.entity.spell.*;
import alfheim.common.potion.*;
import alfheim.common.spell.darkness.*;
import alfheim.common.spell.earth.*;
import alfheim.common.spell.fire.*;
import alfheim.common.spell.illusion.*;
import alfheim.common.spell.nature.*;
import alfheim.common.spell.sound.*;
import alfheim.common.spell.tech.*;
import alfheim.common.spell.water.*;
import alfheim.common.spell.wind.*;
import alfheim.common.world.dim.alfheim.customgens.WorldGenAlfheim;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public class AlfheimRegistry {
	
	public static final IAttribute FLIGHT = new BaseAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", AlfheimConfig.flightTime) { 
		
		@Override
		public double clampValue(double d) {
			return Math.max(0, Math.min(AlfheimConfig.flightTime, d));
		}
	}.setShouldWatch(true);

	public static Potion bleeding;
	public static Potion butterShield;
	public static Potion deathMark;
	public static Potion decay;
	public static Potion goldRush;
	public static Potion icelens;
	public static Potion leftFlame;
	public static Potion nineLifes;
	public static Potion noclip;
	public static Potion possession;
	public static Potion quadDamage;
	public static Potion sacrifice;
	public static Potion sharedHP;
	public static Potion showMana;
	public static Potion soulburn;
	public static Potion stoneSkin;
	public static Potion tHrOw;
	public static Potion wellOLife;

	public static final IWorldGenerator worldGen = new WorldGenAlfheim();
	
	public static void preInit() {
		if(Potion.potionTypes.length < 256) ASJReflectionHelper.invokeStatic(ModPotions.class, null, "extendPotionArray");
		bleeding = new PotionAlfheim(AlfheimConfig.potionIDBleeding, "bleeding", true, 0xFF0000) {
			public boolean isReady(int time, int ampl) {
				return time % (20/Math.max(1, ampl)) == 0; 
			}

			public void performEffect(EntityLivingBase living, int ampl) {
				if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.bleeding, ampl+1);
			}
		};
		butterShield = new PotionAlfheim(AlfheimConfig.potionIDButterShield, "butterShield", false, 0x00FFFF);
		deathMark = new PotionAlfheim(AlfheimConfig.potionIDDeathMark, "deathMark", true, 0x553355) {
			public boolean isReady(int time, int ampl) {
				return time == 1; 
			}

			public void performEffect(EntityLivingBase living, int ampl) {
				if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.mark, Integer.MAX_VALUE);
			}
		};
		decay = new PotionAlfheim(AlfheimConfig.potionIDDecay, "decay", true, 0x553355);
		goldRush = new PotionAlfheim(AlfheimConfig.potionIDGoldRush, "goldRush", false, 0x55FF00);
		icelens = new PotionAlfheim(AlfheimConfig.potionIDIceLens, "icelens", false, 0xDDFFFF);
		leftFlame = new PotionAlfheim(AlfheimConfig.potionIDLeftFlame, "leftFlame", false, 0x0) {
			public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
				super.applyAttributesModifiersToEntity(target, attributes, ampl);
				if (AlfheimCore.enableMMO && target instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) target;
					player.capabilities.allowEdit = false;
					player.capabilities.allowFlying = true;
					player.capabilities.disableDamage = true;
					player.capabilities.isFlying = true;
					if(player instanceof EntityPlayerMP) ((EntityPlayerMP) player).theItemInWorldManager.setBlockReachDistance(-1);
					if(!ASJUtilities.isServer()) SpellEffectHandlerClient.onDeath(target);
				}
			}
			
			public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
				super.removeAttributesModifiersFromEntity(target, attributes, ampl);
				if (AlfheimCore.enableMMO && target instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) target;
					player.capabilities.allowEdit = true;
					player.capabilities.allowFlying = false;
					player.capabilities.disableDamage = false;
					player.capabilities.isFlying = false;
					if(player instanceof EntityPlayerMP) ((EntityPlayerMP) player).theItemInWorldManager.setBlockReachDistance(5);
					player.getDataWatcher().updateObject(6, Float.valueOf(ampl));
				}
			}
		};
		nineLifes = new PotionAlfheim(AlfheimConfig.potionIDNineLifes, "nineLifes", false, 0xDD2222);
		noclip = new PotionNoclip();
		possession = new PotionAlfheim(AlfheimConfig.potionIDPossession, "possession", true, 0xCC0000);
		quadDamage = new PotionAlfheim(AlfheimConfig.potionIDQuadDamage, "quadDamage", false, 0x22FFFF) {
			@Override
			public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
				super.applyAttributesModifiersToEntity(target, attributes, ampl);
				if (AlfheimCore.enableMMO) SpellEffectHandler.sendPacket(Spells.QUAD, target);
			}
		};
		sacrifice = new PotionSacrifice();
		sharedHP = new PotionAlfheim(AlfheimConfig.potionIDSharedHP, "sharedHP", false, 0xFF0000) {
			@Override
			public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
				super.applyAttributesModifiersToEntity(target, attributes, ampl);
				if (!AlfheimCore.enableMMO) return;
				AttributeModifier m = new AttributeModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"), "sharedHP", ampl - target.getMaxHealth(), 0);
				target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
				target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m);
			}
			
			@Override
			public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
				super.removeAttributesModifiersFromEntity(target, attributes, ampl);
				if (!AlfheimCore.enableMMO) return;
				AttributeModifier m = target.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"));
				if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
				target.setHealth(Math.min(target.getHealth(), target.getMaxHealth()));
			}
		};
		showMana = new PotionAlfheim(AlfheimConfig.potionIDShowMana, "showMana", false, 0x0000DD) {
			public boolean isReady(int time, int ampl) {
				return true;
			}
			
			public void performEffect(EntityLivingBase living, int ampl) {
				if (!AlfheimCore.enableMMO) return;
				PotionEffect pe = living.getActivePotionEffect(this);
				if (pe == null) return;
				
				if (ASJUtilities.isServer() || pe.amplifier <= 0) {
					pe.duration = 1;
					return;
				} else {
					if (pe.duration < Integer.MAX_VALUE) ++pe.duration;
					--pe.amplifier;
				}
				
				if (!ASJUtilities.isServer()) 
					for (int i = 0; i < Math.sqrt(Math.sqrt(Math.sqrt(pe.duration))); i++)  // FIXME proper i < VALUE
						SpellEffectHandlerClient.spawnMana(living, i);
			}
		};
		soulburn = new PotionSoulburn();
		stoneSkin = new PotionAlfheim(AlfheimConfig.potionIDStoneSkin, "stoneSkin", false, 0x593C1F);
		tHrOw = new PotionThrow();
		wellOLife = new PotionAlfheim(AlfheimConfig.potionIDWellOLife, "wellolife", false, 0x00FFFF) {
			public boolean isReady(int time, int ampl) {
				return AlfheimCore.enableMMO && time % 10 == 0;
			}

			public void performEffect(EntityLivingBase living, int ampl) {
				if (!AlfheimCore.enableMMO) return;
				if (living.isInWater()) living.heal(0.5F);
			}
		};
		registerEntities();
		registerTileEntities();
	}

	public static void init() {
		registerWorldGenerator(worldGen, 1);
		registerSpells();
	}

	public static void postInit() {
		loadAllPinkStuff();
		if (AlfheimConfig.looniumOverseed) BotaniaAPI.looniumBlacklist.remove(ModItems.overgrowthSeed);
	}
	
	private static void registerEntities() {
		registerEntity(EntityCharge.class, "Charge", AlfheimCore.instance);
		registerEntityEgg(EntityElf.class, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance);
		registerEntity(EntityFlugel.class, "Flugel", AlfheimCore.instance);
		registerEntity(EntityLightningMark.class, "LightningMark", AlfheimCore.instance);
		registerEntityEgg(EntityAlfheimPixie.class, "Pixie", 0xFF76D6, 0xFFE3FF, AlfheimCore.instance);
		
		registerEntity(EntitySpellAcidMyst.class, "SpellAcidMyst", AlfheimCore.instance);
		registerEntity(EntitySpellHarp.class, "SpellArfa", AlfheimCore.instance);
		registerEntity(EntitySpellAquaStream.class, "SpellAquaStream", AlfheimCore.instance);
		registerEntity(EntitySpellDriftingMine.class, "SpellDriftingMine", AlfheimCore.instance);
		registerEntity(EntitySpellFenrirStorm.class, "SpellFenrirStorm", AlfheimCore.instance);
		registerEntity(EntitySpellFireball.class, "SpellFireball", AlfheimCore.instance);
		registerEntity(EntitySpellFirewall.class, "SpellFirewall", AlfheimCore.instance);
		registerEntity(EntitySpellGravityTrap.class, "SpellGravityTrap", AlfheimCore.instance);
		registerEntity(EntitySpellMortar.class, "SpellMortar", AlfheimCore.instance);
		registerEntity(EntitySpellWindBlade.class, "SpellWindBlade", AlfheimCore.instance);
	}
	
	private static void registerTileEntities() {
		registerTileEntity(TileAlfheimPortal.class, "AlfheimPortal");
		registerTileEntity(TileAlfheimPylons.class, "AlfheimPylon");
		registerTileEntity(TileAnyavil.class, "Anyavil");
		registerTileEntity(TileFlugelHead.class, "FlugelHead");
		registerTileEntity(TileManaInfuser.class, "ManaInfuser");
		registerTileEntity(TileTradePortal.class, "TradePortal");
		//registerTileEntity(TileTransferer.class, "Transferer"); BACK
	}
	
	private static void registerSpells() {
		registerSpell(new SpellAcidMyst());
		registerSpell(new SpellAquaBind());
		registerSpell(new SpellAquaStream());
		registerSpell(new SpellBattleHorn());
		registerSpell(new SpellBlink());
		registerSpell(new SpellBunnyHop());
		registerSpell(new SpellButterflyShield());
		registerSpell(new SpellConfusion());
		registerSpell(new SpellDay());
		registerSpell(new SpellDeathMark());
		registerSpell(new SpellDecay());
		registerSpell(new SpellDispel());
		registerSpell(new SpellDriftingMine());
		registerSpell(new SpellDragonGrowl());
		registerSpell(new SpellEcho());
		registerSpell(new SpellFenrirStorm());
		registerSpell(new SpellFireball());
		registerSpell(new SpellFirewall());
		registerSpell(new SpellGravityTrap());
		registerSpell(new SpellGoldRush());
		registerSpell(new SpellHammerfall());
		registerSpell(new SpellHarp());
		registerSpell(new SpellHealing());
		registerSpell(new SpellHollowBody());
		registerSpell(new SpellIceLens());
		registerSpell(new SpellIgnition());
		registerSpell(new SpellMortar());
		registerSpell(new SpellNight());
		registerSpell(new SpellNightVision());
		registerSpell(new SpellNineLifes());
		registerSpell(new SpellNoclip());
		registerSpell(new SpellOutdare());
		registerSpell(new SpellPoisonRoots());
		registerSpell(new SpellPurifyingSurface());
		registerSpell(new SpellRain());
		registerSpell(new SpellResurrect());
		registerSpell(new SpellSacrifice());
		registerSpell(new SpellSharedHealthPool());
		registerSpell(new SpellSmokeScreen());
		registerSpell(new SpellStoneSkin());
		registerSpell(new SpellSun());
		registerSpell(new SpellThor());
		registerSpell(new SpellThrow());
		registerSpell(new SpellThunder());
		registerSpell(new SpellTimeStop());
		registerSpell(new SpellTitanHit());
		registerSpell(new SpellTrueSigh());
		registerSpell(new SpellUphealth());
		registerSpell(new SpellWallWarp());
		registerSpell(new SpellWaterBreathing());
		registerSpell(new SpellWellOLife());
		registerSpell(new SpellWindBlade());
	}
	
	public static void loadAllPinkStuff() {
		addPink(new ItemStack(Items.dye, 1, 9), 1);
		addPink(new ItemStack(Blocks.wool, 1, 6), 1);
		addPink(new ItemStack(Items.potionitem, 1, 8193), 2);
		addPink(new ItemStack(Items.potionitem, 1, 8225), 3);
		addPink(new ItemStack(Items.potionitem, 1, 8257), 3);
		addPink(new ItemStack(Items.potionitem, 1, 16385), 2);
		addPink(new ItemStack(Items.potionitem, 1, 16417), 3);
		addPink(new ItemStack(Items.potionitem, 1, 16449), 3);
		addPink(new ItemStack(Blocks.red_flower, 1, 7), 1);
		addPink(new ItemStack(Blocks.stained_hardened_clay, 1, 6), 1);
		addPink(new ItemStack(Blocks.stained_glass, 1, 6), 1);
		addPink(new ItemStack(Blocks.stained_glass_pane, 1, 6), 1);
		addPink(new ItemStack(Blocks.carpet, 1, 6), 1);
		addPink(new ItemStack(ModBlocks.mushroom, 1, 6), 4);
		addPink(new ItemStack(ModBlocks.flower, 1, 6), 2);
		addPink(new ItemStack(ModBlocks.floatingFlower, 1, 6), 2);
		addPink(new ItemStack(ModBlocks.shinyFlower, 1, 6), 2);
		addPink(new ItemStack(ModBlocks.doubleFlower1, 1, 6), 4);
		addPink(new ItemStack(ModBlocks.doubleFlower2, 1, 6), 4);
		addPink(new ItemStack(ModBlocks.petalBlock, 1, 6), 9);
		addPink(new ItemStack(ModItems.petal, 1, 6), 1);
		addPink(new ItemStack(ModItems.dye, 1, 6), 1);
		addPink(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), 4);
		addPink(new ItemStack(ModItems.manaResource, 1, 7), 9);
		addPink(new ItemStack(ModItems.manaResource, 1, 8), 9);
		addPink(new ItemStack(ModItems.manaResource, 1, 9), 9);
		addPink(new ItemStack(ModItems.manaResource, 1, 19), 1);
		addPink(new ItemStack(ModBlocks.storage, 1, 2), 81);
		addPink(new ItemStack(ModBlocks.storage, 1, 4), 81);
		addPink(new ItemStack(ModItems.slimeBottle), 45);
		addPink(new ItemStack(ModItems.openBucket), 27);
		addPink(new ItemStack(ModItems.elementiumAxe), 27);
		addPink(new ItemStack(ModItems.elementiumBoots), 36);
		addPink(new ItemStack(ModItems.elementiumChest), 72);
		addPink(new ItemStack(ModItems.elementiumHelm), 45);
		addPink(new ItemStack(ModItems.elementiumHelmRevealing), 45);
		addPink(new ItemStack(ModItems.elementiumLegs), 56);
		addPink(new ItemStack(ModItems.elementiumPick), 27);
		addPink(new ItemStack(ModItems.elementiumShears), 18);
		addPink(new ItemStack(ModItems.elementiumShovel), 9);
		addPink(new ItemStack(ModItems.elementiumSword), 18);
		addPink(new ItemStack(ModItems.starSword), 20);
		addPink(new ItemStack(ModItems.thorRing), 1000);
		addPink(new ItemStack(ModItems.odinRing), 1000);
		addPink(new ItemStack(ModItems.lokiRing), 1000);
		addPink(new ItemStack(ModItems.aesirRing), 3000);
		addPink(new ItemStack(ModBlocks.unstableBlock, 1, 6), 2);
		addPink(new ItemStack(ModBlocks.manaBeacon, 1, 6), 8);
		addPink(new ItemStack(ModItems.pinkinator), 100);
		addPink(new ItemStack(ModItems.rune, 1, 4), 10);
		addPink(new ItemStack(ModItems.baubleBox), 5);
		addPink(new ItemStack(ModItems.cosmetic, 1, 8), 8);
		addPink(new ItemStack(ModItems.reachRing, 1, 8), 36);
		addPink(new ItemStack(ModItems.pixieRing, 1, 8), 45);
		addPink(new ItemStack(ModItems.superTravelBelt, 1, 8), 38);
		addPink(new ItemStack(ModItems.rainbowRod, 1, 8), 45);
		for (int i = 0; i < 10; i++) addPink(new ItemStack(ModItems.flightTiara, 1, i), 88);
		addPink(new ItemStack(AlfheimItems.elementalBoots), 36);
		addPink(new ItemStack(AlfheimItems.elementalChestplate), 72);
		addPink(new ItemStack(AlfheimItems.elementalHelmet), 45);
		addPink(new ItemStack(AlfheimItems.elementalHelmetRevealing), 45);
		addPink(new ItemStack(AlfheimItems.elementalLeggings), 56);
		addPink(new ItemStack(AlfheimItems.pixieAttractor), 36);
		addPink(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore), 9);
		addPink(new ItemStack(AlfheimBlocks.manaInfuser), 90);
		addPink(new ItemStack(AlfheimBlocks.alfheimPylons), 45);
		addPink(new ItemStack(AlfheimBlocks.elvenOres), 9);
		addPink(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), 9);
		addPink(new ItemStack(AlfheimBlocks.anyavil), 297);
	}
}
