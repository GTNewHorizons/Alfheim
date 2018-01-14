package alfheim.api.crafting.recipe;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipePetals;

public class IManaInfusionRecipe extends RecipePetals {
	
	public int mana;
	
	public IManaInfusionRecipe(int mana, ItemStack out, ItemStack... recipe) {
		super(out, (Object[]) recipe);
		this.mana = mana;
	}
	
	public String toString() {
		String s = "";
		for (Object ing : getInputs()) s += (((ItemStack)ing).toString() + " + ");
		return "Recipe: " + s + " mana*" + mana + " => " + getOutput().toString();
	}

	public int getManaUsage() {
		return mana;
	}
}
