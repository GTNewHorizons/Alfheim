package alfheim.common.utils;

import java.util.Vector;

import net.minecraft.item.ItemStack;

public class IManaInfusionRecipe {
	
	public int mana;
	public ItemStack output;
	public Vector<ItemStack> ingredients;
	
	public IManaInfusionRecipe(int mane, ItemStack out, ItemStack... recipe) {
		mana = mane;
		output = out;
		ingredients = new Vector<ItemStack>();
		for (ItemStack part : recipe) ingredients.add(part);
	}
	
	public String toString() {
		String s = "";
		for (ItemStack ing : ingredients) s += (ing.toString() + " + ");
		return "Recipe: " + s + " mana*" + mana + " => " + output.toString();
	}
}
