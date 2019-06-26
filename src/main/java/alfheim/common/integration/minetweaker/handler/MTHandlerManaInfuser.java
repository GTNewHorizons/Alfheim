package alfheim.common.integration.minetweaker.handler;

import static alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.*;

import java.util.ArrayList;

import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + ModInfo.MODID + ".ManaInfuser")
public class MTHandlerManaInfuser {

	@ZenMethod
    public static void addRecipe(IItemStack output, int mana, IIngredient... inputs) {
		MineTweakerAPI.apply(new Add(new RecipeManaInfuser(mana, getStack(output), getObjects(inputs))));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(getStack(output)));
	}
	
	private static class Add implements IUndoableAction {

		private final RecipeManaInfuser recipe;
		
		public Add(RecipeManaInfuser recipeManaInfuser) {
			recipe = recipeManaInfuser;
		}

		@Override
		public void apply() {
			AlfheimAPI.addInfuserRecipe(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			AlfheimAPI.removeInfusionRecipe(recipe);
		}

		@Override
		public String describe() {
			return String.format("Adding Mana Infuser recipe %s", recipe);
		}

		@Override
		public String describeUndo() {
			return String.format("Removing Mana Infuser recipe %s", recipe);
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
	
	private static class Remove implements IUndoableAction {

		private final ItemStack output;
		final ArrayList<RecipeManaInfuser> removed = new ArrayList<RecipeManaInfuser>();
		
		public Remove(ItemStack stack) {
			output = stack;
		}

		@Override
		public void apply() {
			RecipeManaInfuser rec;
			do {
				removed.add(rec = AlfheimAPI.removeInfusionRecipe(output));
			} while (rec != null);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			for (RecipeManaInfuser rec : removed) AlfheimAPI.addInfuserRecipe(rec);
		}

		@Override
		public String describe() {
			return String.format("Removing all Mana Infuser recipes for %s", output.getUnlocalizedName());
		}

		@Override
		public String describeUndo() {
			return String.format("Re-adding previously removed Mana Infuser recipes for %s", output.getUnlocalizedName());
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}