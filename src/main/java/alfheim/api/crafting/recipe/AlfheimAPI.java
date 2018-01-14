package alfheim.api.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class AlfheimAPI {
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {5, 10, 8, 5}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 9, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	
	/** List of {@link RecipeElvenTrade} outputs forbidden for re'trading from Alfheim trade portal */
	public static List<ItemStack> forbiddenRetrades = new ArrayList();
	/** List of recipies for mana infuser */
	public static List<IManaInfusionRecipe> manaInfusionRecipes = new ArrayList<IManaInfusionRecipe>();

	public static IManaInfusionRecipe addInfusionRecipe(IManaInfusionRecipe rec) {
		manaInfusionRecipes.add(rec);
		return rec;
	}
	
	public static IManaInfusionRecipe addInfusionRecipe(ItemStack result, int mana, ItemStack... ingredients) {
		IManaInfusionRecipe rec = new IManaInfusionRecipe(mana, result, ingredients);
		manaInfusionRecipes.add(rec);
		return rec;
	}
	
	public static boolean removeInfusionRecipeByResult(ItemStack result) {
		for (int i = 0; i < manaInfusionRecipes.size(); i++) if (ItemStack.areItemStacksEqual(manaInfusionRecipes.get(i).getOutput(), result)) {
			manaInfusionRecipes.remove(i);
			return true;
		}
		return false;
	}
	
	public static void addForbiddenRetrade(ItemStack output) {
		forbiddenRetrades.add(output);
	}
	
	public static boolean isRetradeForbidden(ItemStack output) {
		for (ItemStack out : forbiddenRetrades) if (ItemStack.areItemStacksEqual(output, out)) return true;
		return false;
	}
}
