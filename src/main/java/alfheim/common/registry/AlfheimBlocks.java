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
import alfheim.common.blocks.ElvenOres;
import alfheim.common.blocks.ManaInfuser;
import alfheim.common.blocks.PoisonIce;
import alfheim.common.blocks.RedFlame;
import alfheim.common.items.ItemBlockElvenOres;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class AlfheimBlocks {

	public static Block alfheimPortal = new AlfheimPortal();
	public static Block dreamLeaves = new DreamLeaves();
	public static Block dreamLog = new DreamLog();
	public static Block elvenOres = new ElvenOres();
	public static Block elvoriumBlock = new BlockPattern(Constants.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 1, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block mauftriumBlock = new BlockPattern(Constants.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 1, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block manaInfuser = new ManaInfuser();
	public static Block poisonIce = new PoisonIce();
	public static Block redFlame = new RedFlame();
	
	public static void init() {
		register();
		registerOreDict();
	}

	private static void register() {
		registerBlock(manaInfuser);
		registerBlock(alfheimPortal);
		registerBlock(elvoriumBlock);
		registerBlock(mauftriumBlock);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		registerBlock(dreamLog);
		registerBlock(dreamLeaves);
		registerBlock(poisonIce);
		registerBlock(redFlame);
	}

	private static void registerOreDict() {
		registerOre("oreGold", new ItemStack(elvenOres, 1, 3));
	}
}
