package alfheim.common.item.rod;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;

public abstract class ItemRodBase extends Item implements IManaUsingItem {

	public ItemRodBase(String name) {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":" + name);
		setUnlocalizedName(name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		cast(stack, world, player);
		return stack;
	}
	
	public abstract void cast(ItemStack stack, World world, EntityPlayer player);
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}