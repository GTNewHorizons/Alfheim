package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static alfheim.api.AlfheimAPI.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;

import java.util.LinkedHashSet;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.block.tile.*;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.world.dim.alfheim.gen.WorldGenAlfheim;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public class AlfheimRegistry {
	
	public static final IWorldGenerator worldGen = new WorldGenAlfheim();
	
	public static void preInit() {
		registerEntities();
		registerTileEntities();
	}

	public static void init() {
		registerWorldGenerator(worldGen, 1);
	}
	
	public static void postInit() {
		loadAllPinkStuff();
		if (AlfheimConfig.looniumOverseed) BotaniaAPI.looniumBlacklist.remove(ModItems.overgrowthSeed);
	}
	
	private static void registerEntities() {
		registerEntityEgg(EntityElf.class, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance);
		registerEntityEgg(EntityAlfheimPixie.class, "Pixie", 0xFF76D6, 0xFFE3FF, AlfheimCore.instance);
	}
	
	private static void registerTileEntities() {
		registerTileEntity(TileAlfheimPortal.class, "AlfheimPortal");
		registerTileEntity(TileAlfheimPylons.class, "AlfheimPylon");
		registerTileEntity(TileAnyavil.class, "Anyavil");
		registerTileEntity(TileManaInfuser.class, "ManaInfuser");
		registerTileEntity(TileTradePortal.class, "TradePortal");
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
		addPink(new ItemStack(ModItems.slimeBottle, 1, 0), 45);
		addPink(new ItemStack(ModItems.openBucket, 1, 0), 27);
		addPink(new ItemStack(ModItems.elementiumAxe, 1, 0), 27);
		addPink(new ItemStack(ModItems.elementiumBoots, 1, 0), 36);
		addPink(new ItemStack(ModItems.elementiumChest, 1, 0), 72);
		addPink(new ItemStack(ModItems.elementiumHelm, 1, 0), 45);
		addPink(new ItemStack(ModItems.elementiumHelmRevealing, 1, 0), 45);
		addPink(new ItemStack(ModItems.elementiumLegs, 1, 0), 56);
		addPink(new ItemStack(ModItems.elementiumPick, 1, 0), 27);
		addPink(new ItemStack(ModItems.elementiumShears, 1, 0), 18);
		addPink(new ItemStack(ModItems.elementiumShovel, 1, 0), 9);
		addPink(new ItemStack(ModItems.elementiumSword, 1, 0), 18);
		addPink(new ItemStack(ModItems.starSword, 1, 0), 20);
		addPink(new ItemStack(ModItems.thorRing, 1, 0), 1000);
		addPink(new ItemStack(ModItems.odinRing, 1, 0), 1000);
		addPink(new ItemStack(ModItems.lokiRing, 1, 0), 1000);
		addPink(new ItemStack(ModItems.aesirRing, 1, 0), 3000);
		addPink(new ItemStack(ModBlocks.unstableBlock, 1, 6), 2);
		addPink(new ItemStack(ModBlocks.manaBeacon, 1, 6), 8);
		addPink(new ItemStack(ModItems.pinkinator, 1, 0), 100);
		addPink(new ItemStack(ModItems.rune, 1, 4), 10);
		addPink(new ItemStack(ModItems.baubleBox, 1, 0), 5);
		addPink(new ItemStack(ModItems.cosmetic, 1, 8), 8);
		addPink(new ItemStack(ModItems.reachRing, 1, 8), 36);
		addPink(new ItemStack(ModItems.pixieRing, 1, 8), 45);
		addPink(new ItemStack(ModItems.superTravelBelt, 1, 8), 38);
		addPink(new ItemStack(ModItems.rainbowRod, 1, 8), 45);
		for (int i = 0; i < 10; i++) addPink(new ItemStack(ModItems.flightTiara, 1, i), 88);
		addPink(new ItemStack(AlfheimItems.elementalBoots, 1, 0), 36);
		addPink(new ItemStack(AlfheimItems.elementalChestplate, 1, 0), 72);
		addPink(new ItemStack(AlfheimItems.elementalHelmet, 1, 0), 45);
		addPink(new ItemStack(AlfheimItems.elementalHelmetRevealing, 1, 0), 45);
		addPink(new ItemStack(AlfheimItems.elementalLeggings, 1, 0), 56);
		addPink(new ItemStack(AlfheimItems.pixieAttractor, 1, 0), 36);
		addPink(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore), 9);
		addPink(new ItemStack(AlfheimBlocks.manaInfuser, 1, ElvenResourcesMetas.ManaInfusionCore), 90);
		addPink(new ItemStack(AlfheimBlocks.alfheimPylons, 1, 0), 45);
		addPink(new ItemStack(AlfheimBlocks.elvenOres, 1, 0), 9);
		addPink(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), 9);
	}
}
