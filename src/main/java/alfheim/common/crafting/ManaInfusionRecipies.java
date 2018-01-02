package alfheim.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ManaInfusionRecipies {
	
	public static List<IManaInfusionRecipe> recipes = new ArrayList<IManaInfusionRecipe>();

	public static IManaInfusionRecipe addRecipe(IManaInfusionRecipe rec) {
		recipes.add(rec);
		return rec;
	}
	
	public static IManaInfusionRecipe addRecipe(ItemStack result, int mana, ItemStack... ingredients) {
		IManaInfusionRecipe rec = new IManaInfusionRecipe(mana, result, ingredients);
		recipes.add(rec);
		return rec;
	}
}
