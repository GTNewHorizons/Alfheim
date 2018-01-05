package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.block.Block.*;
import static net.minecraftforge.oredict.OreDictionary.*;

import alexsocol.asjlib.BlockPattern;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.block.BlockAlfheimPortal;
import alfheim.common.block.BlockDreamLeaves;
import alfheim.common.block.BlockDreamLog;
import alfheim.common.block.BlockDreamSapling;
import alfheim.common.block.BlockElvenGrass;
import alfheim.common.block.BlockElvenOres;
import alfheim.common.block.BlockManaInfuser;
import alfheim.common.block.BlockPoisonIce;
import alfheim.common.block.BlockRedFlame;
import alfheim.common.item.ItemBlockElvenOres;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class AlfheimBlocks {
	
	public static Block alfheimPortal;
	public static Block dreamLeaves;
	public static Block dreamlog;
	public static Block dreamSapling;
	public static Block elvenGrass;
	public static Block elvenOres;
	public static Block elvenSand;
	public static Block elvoriumBlock;
	public static Block livingcobble;
	public static Block mauftriumBlock;
	public static Block manaInfuser;
	public static Block poisonIce;
	public static Block redFlame;
	
	public static void init() {
		construct();
		reg();
		regOreDict();
	}

	private static void construct() {
		alfheimPortal = new BlockAlfheimPortal();
		dreamLeaves = new BlockDreamLeaves();
		dreamlog = new BlockDreamLog();
		dreamSapling = new BlockDreamSapling();
		elvenGrass = new BlockElvenGrass();
		elvenOres = new BlockElvenOres();
		elvenSand = new BlockPattern(Constants.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0, 255, 1, "shovel", 0, 5, soundTypeGravel, true, false, true);
		elvoriumBlock = new BlockPattern(Constants.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false);
		livingcobble = new BlockPattern(Constants.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0, 255, 2, "pickaxe", 0, 60, soundTypeStone, true, false, false);
		mauftriumBlock = new BlockPattern(Constants.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true, false);
		manaInfuser = new BlockManaInfuser();
		poisonIce = new BlockPoisonIce();
		redFlame = new BlockRedFlame();		
	}

	private static void reg() {
		register(manaInfuser);
		register(alfheimPortal);
		register(elvoriumBlock);
		register(mauftriumBlock);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		register(livingcobble);
		register(elvenGrass);
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
