package alfheim.common.crafting.recipe;

import alfheim.common.item.ItemLootInterceptor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class LootInterceptorClearRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean inter = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ItemLootInterceptor) {
					if (!inter) inter = true;
					
					else return false;
				}
				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return inter;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack inter = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof ItemLootInterceptor) {
				if (inter == null)
					inter = stack.copy();
				else
					return null;
			}
		}

		ItemLootInterceptor.setIDs(inter, ItemLootInterceptor.EMPTY);
		ItemLootInterceptor.setMetas(inter, ItemLootInterceptor.EMPTY);
		
		return inter;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
}