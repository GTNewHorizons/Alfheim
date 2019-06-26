package alfheim.api.crafting.recipe;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipePetals;

public class RecipeManaInfuser extends RecipePetals {
	
	public final int mana;
	
	public RecipeManaInfuser(int mana, ItemStack out, Object... recipe) {
		super(out, recipe);
		this.mana = mana;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Object ing : getInputs()) s.append((ing).toString()).append(" + ");
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