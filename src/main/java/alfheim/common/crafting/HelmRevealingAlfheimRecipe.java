package alfheim.common.crafting;

import alfheim.common.registry.AlfheimItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

public class HelmRevealingAlfheimRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
		if (goggles == null)
			return false; // NO TC loaded

		boolean foundGoggles = false;
		boolean foundHelm = false;
		for (int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if (stack != null) {
				if (checkHelm(stack))
					foundHelm = true;
				else if (stack.getItem() == goggles)
					foundGoggles = true;
				else
					return false; // Found an invalid item, breaking the recipe
			}
		}
		return foundGoggles && foundHelm;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack helm = null;

		for (int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if (stack != null && checkHelm(stack))
				helm = stack;
		}

		if (helm == null)
			return null;

		ItemStack helmCopy = helm.copy();
		Item helmItem = helmCopy.getItem();

		ItemStack newHelm;

		if (helmItem == AlfheimItems.elementalHelmet)
			newHelm = new ItemStack(AlfheimItems.elementalHelmetRevealing);
		else if (helmItem == AlfheimItems.elvoriumHelmet)
			newHelm = new ItemStack(AlfheimItems.elvoriumHelmetRevealing);
		else
			return null;

		// Copy Ancient Wills
		for (int i = 0; i < 6; i++)
			if (ItemNBTHelper.getBoolean(helmCopy, "AncientWill" + i, false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill" + i, true);

		// Copy Enchantments
		NBTTagList enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true);
		if (enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList);

		// Copy Runic Hardening
		byte runicHardening = ItemNBTHelper.getByte(helmCopy, "RS.HARDEN", (byte) 0);
		ItemNBTHelper.setByte(newHelm, "RS.HARDEN", runicHardening);

		return newHelm;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.manasteelHelmRevealing);
	}

	private boolean checkHelm(ItemStack helmStack) {
		Item helmItem = helmStack.getItem();
		return helmItem == AlfheimItems.elementalHelmet || helmItem == AlfheimItems.elvoriumHelmet;
	}

}
