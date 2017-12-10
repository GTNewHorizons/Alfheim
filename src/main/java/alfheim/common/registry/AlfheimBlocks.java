package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.block.Block.*;
import static net.minecraftforge.oredict.OreDictionary.*;

import alexsocol.asjlib.BlockPattern;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.blocks.AlfheimPortal;
import alfheim.common.blocks.DreamLeaves;
import alfheim.common.blocks.DreamLog;
import alfheim.common.blocks.DreamSapling;
import alfheim.common.blocks.ElvenGrass;
import alfheim.common.blocks.ElvenOres;
import alfheim.common.blocks.ManaInfuser;
import alfheim.common.blocks.PoisonIce;
import alfheim.common.blocks.RedFlame;
import alfheim.common.blocks.TestBlock;
import alfheim.common.items.ItemBlockElvenOres;
import clashsoft.cslib.minecraft.block.CSBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class AlfheimBlocks {
	
	public static final Block blockOfTesting = new TestBlock();
	
	public static Block alfheimPortal = new AlfheimPortal();
	public static Block dreamLeaves = new DreamLeaves();
	public static Block dreamLog = new DreamLog();
	public static Block dreamSapling = new DreamSapling();
	public static Block elvenGlass = new BlockPattern(Constants.MODID, Material.glass, "ElvenGlass", AlfheimCore.alfheimTab, 0, 255, 0.1F, null, 0, 30, soundTypeGlass, false, false);
	public static Block elvenGrass = new ElvenGrass();
	public static Block elvenOres = new ElvenOres();
	public static Block elvenSand = new BlockPattern(Constants.MODID, Material.sand, "ElvenSand", AlfheimCore.alfheimTab, 0, 255, 1, "shovel", 0, 5, soundTypeGravel, true, false);
	public static Block elvoriumBlock = new BlockPattern(Constants.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block livingcobble = new BlockPattern(Constants.MODID, Material.rock, "LivingCobble", AlfheimCore.alfheimTab, 0, 255, 2, "pickaxe", 0, 60, soundTypeStone, true, false);
	public static Block mauftriumBlock = new BlockPattern(Constants.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 255, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block manaInfuser = new ManaInfuser();
	public static Block poisonIce = new PoisonIce();
	public static Block redFlame = new RedFlame();
	
	public static void init() {
		reg();
		regOreDict();
	}

	private static void reg() {
		register(manaInfuser);
		register(alfheimPortal);
		register(elvoriumBlock);
		register(mauftriumBlock);
		register(elvenGlass);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		register(livingcobble);
		register(elvenGrass);
		register(elvenSand);
		register(dreamLog);
		register(dreamLeaves);
		register(dreamSapling);
		register(poisonIce);
		register(redFlame);
	}

	private static void regOreDict() {
		registerOre("oreGold", new ItemStack(elvenOres, 1, 3));
	}
}
