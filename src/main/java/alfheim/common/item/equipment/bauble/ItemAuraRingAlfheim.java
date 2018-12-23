package alfheim.common.item.equipment.bauble;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemAuraRingAlfheim extends ItemBauble implements IManaGivingItem {

	public ItemAuraRingAlfheim(String name) {
		super(name);
		setCreativeTab(AlfheimCore.alfheimTab);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		if(player instanceof EntityPlayer && player.ticksExisted % getDelay() == 0)
			ManaItemHandler.dispatchManaExact(stack, (EntityPlayer) player, 10, true);
	}

	public int getDelay() {
		return 10;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
}