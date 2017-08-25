package baubles.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract interface IBauble {
	public abstract BaubleType getBaubleType(ItemStack stack);

	public abstract void onWornTick(ItemStack stack, EntityLivingBase entity);

	public abstract void onEquipped(ItemStack stack, EntityLivingBase entity);

	public abstract void onUnequipped(ItemStack stack, EntityLivingBase entity);

	public abstract boolean canEquip(ItemStack stack, EntityLivingBase entity);

	public abstract boolean canUnequip(ItemStack stack, EntityLivingBase entity);
}