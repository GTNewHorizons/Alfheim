package alfheim.api.crafting.recipe;

import net.minecraft.item.Item;
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
		return "Recipe: " + s + " mana*" + mana + " => " + getOutput();
	}

	public int getManaUsage() {
		return mana;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RecipeManaInfuser) {
			RecipeManaInfuser r = (RecipeManaInfuser) obj;
			return r.mana == mana && ItemStack.areItemStacksEqual(getOutput(), r.getOutput()) && getInputs().containsAll(r.getInputs()) && r.getInputs().containsAll(getInputs());
		}
		return false;
	}
}