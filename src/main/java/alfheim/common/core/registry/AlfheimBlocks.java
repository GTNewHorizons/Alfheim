package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.block.Block.*;
import static net.minecraftforge.oredict.OreDictionary.*;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibOreDict;
import alfheim.common.block.*;
import alfheim.common.block.mana.*;
import alfheim.common.item.block.*;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;

public class AlfheimBlocks {
	
	public static Block alfheimPortal;
	public static Block alfheimPylons;
	public static Block animatedTorch;
	public static Block anomaly;
	public static Block anyavil;
	public static Block dreamLeaves;
	public static Block dreamLog;
	public static Block dreamSapling;
	public static Block elvenOres;
	public static Block elvenSand;
	public static Block elvoriumBlock;
	public static Block flugelHead;
	public static Block itemHolder;
	public static Block livingcobble;
	public static Block mauftriumBlock;
	public static Block manaInfuser;
	public static Block poisonIce;
	public static Block redFlame;
	public static Block tradePortal;
	//public static Block transferer; BACK
	
	public static void init() {
		construct();
		reg();
		regOreDict();
	}

	private static void construct() {
		alfheimPortal = new BlockAlfheimPortal();
		alfheimPylons = new BlockAlfheimPylon();
		animatedTorch = new BlockAnimatedTorch();
		anomaly = new BlockAnomaly();
		anyavil = new BlockAnyavil();
		dreamLeaves = new BlockDreamLeaves();
		dreamLog = new BlockDreamLog();
		dreamSapling = new BlockDreamSapling();
		elvenOres = new BlockElvenOres();
		elvenSand = new BlockPatternLexicon(ModInfo.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0, 255, 1, "shovel", 0, 5, soundTypeGravel, true, false, true, AlfheimLexiconData.worldgen);
		elvoriumBlock = new BlockPatternLexicon(ModInfo.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false, AlfheimLexiconData.elvorium);
		flugelHead = new BlockFlugelHead();
		itemHolder = new BlockItemHolder();
		livingcobble = new BlockPatternLexicon(ModInfo.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0, 255, 2, "pickaxe", 0, 60, soundTypeStone, true, false, false, AlfheimLexiconData.worldgen);
		mauftriumBlock = new BlockPatternLexicon(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false, AlfheimLexiconData.essences);
		manaInfuser = new BlockManaInfuser();
		poisonIce = new BlockPoisonIce();
		redFlame = new BlockRedFlame();
		tradePortal = new BlockTradePortal();
		//transferer = new BlockTransferer(); BACK
	}

	private static void reg() {
		register(manaInfuser);
		register(alfheimPortal);
		register(tradePortal);
		//register(transferer); BACK
		register(itemHolder);
		registerBlock(alfheimPylons, ItemBlockWithMetadataAndName.class, getBlockName(alfheimPylons));
		register(anyavil);
		register(elvoriumBlock);
		register(mauftriumBlock);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		register(livingcobble);
		register(elvenSand);
		register(dreamLog);
		register(dreamLeaves);
		register(dreamSapling);
		register(animatedTorch);
		register(flugelHead);
		register(anomaly);
		register(poisonIce);
		register(redFlame);
	}

	private static void regOreDict() {
		registerOre(LibOreDict.DRAGON_ORE, new ItemStack(elvenOres, 1, 0));
		registerOre(LibOreDict.ELEMENTIUM_ORE, new ItemStack(elvenOres, 1, 1));
		registerOre(LibOreDict.ELVEN_QUARTZ_ORE, new ItemStack(elvenOres, 1, 2));
		registerOre(LibOreDict.GOLD_ORE, new ItemStack(elvenOres, 1, 3));
		registerOre(LibOreDict.IFFESAL_ORE, new ItemStack(elvenOres, 1, 4));
	}
}
