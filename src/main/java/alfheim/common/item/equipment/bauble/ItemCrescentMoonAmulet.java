package alfheim.common.item.equipment.bauble;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemCrescentMoonAmulet extends ItemPendant implements IManaUsingItem {

	public static final int MANA_PER_DAMAGE = 100;
	private static final String TAG_COOLDOWN = "cooldown";
	
	public ItemCrescentMoonAmulet() {
		super("CrescentMoonAmulet");
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, Math.max(0, ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0) - 1));
	}
	
	@SubscribeEvent
	public void onWearerHurt(LivingHurtEvent e) {
		if (!e.source.isDamageAbsolute() && e.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.entityLiving;
			IInventory bbls = BaublesApi.getBaubles(player);
			if (bbls != null && bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).getItem() instanceof ItemCrescentMoonAmulet)
				if (e.source.isMagicDamage()) {
					if (ItemNBTHelper.getInt(bbls.getStackInSlot(0), TAG_COOLDOWN, 0) <= 0) {
						ItemNBTHelper.setInt(bbls.getStackInSlot(0), TAG_COOLDOWN, 100);
						e.ammount = Math.max(0, e.ammount - 10);
					}
				} else
					e.ammount -= (ManaItemHandler.requestMana(bbls.getStackInSlot(0), player, MathHelper.ceiling_float_int(e.ammount * MANA_PER_DAMAGE), true) / (MANA_PER_DAMAGE * 10F));
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}