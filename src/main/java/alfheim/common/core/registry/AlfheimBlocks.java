package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.getBlockName;
import static alexsocol.asjlib.ASJUtilities.register;
import static cpw.mods.fml.common.registry.GameRegistry.registerBlock;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypeStone;
import static net.minecraftforge.oredict.OreDictionary.registerOre;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.block.BlockAlfheimPortal;
import alfheim.common.block.BlockAlfheimPylons;
import alfheim.common.block.BlockAnyavil;
import alfheim.common.block.BlockDreamLeaves;
import alfheim.common.block.BlockDreamLog;
import alfheim.common.block.BlockDreamSapling;
import alfheim.common.block.BlockElvenOres;
import alfheim.common.block.BlockPatternLexicon;
import alfheim.common.block.BlockPoisonIce;
import alfheim.common.block.BlockRedFlame;
import alfheim.common.block.BlockTradePortal;
import alfheim.common.block.mana.BlockManaInfuser;
import alfheim.common.block.mana.BlockTransferer;
import alfheim.common.item.block.ItemBlockElvenOres;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;

public class AlfheimBlocks {
	
	public static Block alfheimPortal;
	public static Block alfheimPylons;
	public static Block anyavil;
	public static Block dreamLeaves;
	public static Block dreamlog;
	public static Block dreamSapling;
	public static Block elvenOres;
	public static Block elvenSand;
	public static Block elvoriumBlock;
	public static Block livingcobble;
	public static Block mauftriumBlock;
	public static Block manaInfuser;
	public static Block poisonIce;
	public static Block redFlame;
	public static Block tradePortal;
	public static Block transferer;
	
	public static void init() {
		construct();
		reg();
		regOreDict();
	}

	private static void construct() {
		alfheimPortal = new BlockAlfheimPortal();
		alfheimPylons = new BlockAlfheimPylons();
		anyavil = new BlockAnyavil();
		dreamLeaves = new BlockDreamLeaves();
		dreamlog = new BlockDreamLog();
		dreamSapling = new BlockDreamSapling();
		elvenOres = new BlockElvenOres();
		elvenSand = new BlockPatternLexicon(ModInfo.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0, 255, 1, "shovel", 0, 5, soundTypeGravel, true, false, true, AlfheimLexiconData.worldgen);
		elvoriumBlock = new BlockPatternLexicon(ModInfo.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false, AlfheimLexiconData.elvorium);
		livingcobble = new BlockPatternLexicon(ModInfo.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0, 255, 2, "pickaxe", 0, 60, soundTypeStone, true, false, false, AlfheimLexiconData.worldgen);
		mauftriumBlock = new BlockPatternLexicon(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false, AlfheimLexiconData.essences);
		manaInfuser = new BlockManaInfuser();
		poisonIce = new BlockPoisonIce();
		redFlame = new BlockRedFlame();
		tradePortal = new BlockTradePortal();
		transferer = new BlockTransferer();
	}

	private static void reg() {
		register(manaInfuser);
		register(alfheimPortal);
		register(tradePortal);
		register(transferer);
		registerBlock(alfheimPylons, ItemBlockWithMetadataAndName.class, getBlockName(alfheimPylons));
		register(anyavil);
		register(elvoriumBlock);
		register(mauftriumBlock);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		register(livingcobble);
		register(elvenSand);
		register(dreamlog);
		register(dreamLeaves);
		register(dreamSapling);
		register(poisonIce);
		register(redFlame);
	}

	private static void regOreDict() {
		registerOre("oreGold", new ItemStack(elvenOres, 1, 3));
	}
}
