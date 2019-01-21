package alfheim.common.item.equipment.bauble;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.lib.LibItemNames;

public class ItemInvisibilityCloak extends ItemBauble implements IManaUsingItem {

	public ItemInvisibilityCloak() {
		super("InvisibilityCloak");
		setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BELT;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(Potion.invisibility);
		if(effect != null && player instanceof EntityPlayer && effect.amplifier == -42)
			player.removePotionEffect(Potion.invisibility.id);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(player instanceof EntityPlayer && !player.worldObj.isRemote) {
			int manaCost = 2;
			boolean hasMana = ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, manaCost, false);
			if(!hasMana)
				onUnequipped(stack, player);
			else {
				if(player.getActivePotionEffect(Potion.invisibility) != null)
					player.removePotionEffect(Potion.invisibility.id);

				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, Integer.MAX_VALUE, -42, true));
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}