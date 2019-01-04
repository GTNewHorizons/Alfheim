package alfheim.common.item.equipment.armor.elemental;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemElementalAirBoots extends ElementalArmor {

	public static final int ONEBLOCKCOST = 10;
	
	public ItemElementalAirBoots() {
		super(3, "ElementalAirBoots");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.09F;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc1"));
		super.addInformation(stack, player, list, b);
	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event) {
		if (armorType == 3 && event.entityLiving instanceof EntityPlayer && ((EntityPlayer) event.entityLiving).getCurrentArmor(0) != null && ((EntityPlayer) event.entityLiving).getCurrentArmor(0).getItem() == AlfheimItems.elementalBoots && ManaItemHandler.requestManaExact(((EntityPlayer) event.entityLiving).getCurrentArmor(0), (EntityPlayer) event.entityLiving, ONEBLOCKCOST * 10, true)) {
			event.entityLiving.motionY += 0.5;
		}
	}
	
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		if (armorType == 3 && event.entityLiving instanceof EntityPlayer && ((EntityPlayer) event.entityLiving).getCurrentArmor(0) != null && ((EntityPlayer) event.entityLiving).getCurrentArmor(0).getItem() == AlfheimItems.elementalBoots) {
			if (event.distance < 4.5063215) event.distance = 0;

			if (event.distance >= 4.5063215) {
				int decrease = ManaItemHandler.requestMana(((EntityPlayer) event.entityLiving).getCurrentArmor(0), (EntityPlayer) event.entityLiving, MathHelper.floor_float(event.distance) * ONEBLOCKCOST, true);
				event.distance -= decrease / ONEBLOCKCOST;
			}
		}
	}
}
