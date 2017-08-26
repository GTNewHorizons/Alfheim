package alfheim.common.utils;

import java.util.Vector;

import alfheim.common.registry.AlfheimItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ModItems;

public class ManaInfusionRecipies {
	
	public static Vector<IManaInfusionRecipe> recipes = new Vector<IManaInfusionRecipe>();

	public static void addRecipe(IManaInfusionRecipe rec) {
		recipes.add(rec);
	}
	
	public static void addRecipe(ItemStack result, int mana, ItemStack... ingredients) {
		recipes.add(new IManaInfusionRecipe(mana, result, ingredients));
	}
}
