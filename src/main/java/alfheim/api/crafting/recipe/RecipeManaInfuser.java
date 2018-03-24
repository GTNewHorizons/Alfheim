package alfheim.api.crafting.recipe;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipePetals;

public class RecipeManaInfuser extends RecipePetals {
	
	public int mana;
	
	public RecipeManaInfuser(int mana, ItemStack out, Object... recipe) {
		super(out, recipe);
		this.mana = mana;
	}
	
	public String toString() {
		String s = "";
		for (Object ing : getInputs()) s += ((ing).toString() + " + ");
		return "Recipe: " + s + " mana*" + mana + " => " + getOutput().toString();
	}

	public int getManaUsage() {
		return mana;
	}
}
