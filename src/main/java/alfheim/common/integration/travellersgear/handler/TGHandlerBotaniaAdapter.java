package alfheim.common.integration.travellersgear.handler;

import java.util.List;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import travellersgear.api.TravellersGearAPI;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.equipment.bauble.ItemHolyCloak;

public class TGHandlerBotaniaAdapter {
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static BaubleType getBaubleType(ItemHolyCloak item, ItemStack stack) {
		return AlfheimCore.TravellersGearLoaded ? null : BaubleType.BELT;
	}
	
	@Hook(createMethod = true)
	public static void onTravelGearTick(ItemHolyCloak item, EntityPlayer player, ItemStack stack) {
		if (AlfheimCore.TravellersGearLoaded) item.onWornTick(stack, player);
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	public static boolean onPlayerDamage(ItemHolyCloak item, LivingHurtEvent event) {
		if (!AlfheimCore.TravellersGearLoaded) return false;
			
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			
			ItemStack[] tg = TravellersGearAPI.getExtendedInventory(player);
			
			if(tg[0] != null && tg[0].getItem() instanceof ItemHolyCloak && !item.isInEffect(tg[0])) {
				ItemHolyCloak cloak = (ItemHolyCloak) tg[0].getItem();
				int cooldown = item.getCooldown(tg[0]);
				
				// Used to prevent StackOverflows with mobs that deal damage when damaged
				item.setInEffect(tg[0], true);
				if(cooldown == 0 && cloak.effectOnDamage(event, player, tg[0]))
					item.setCooldown(tg[0], cloak.getCooldownTime(tg[0]));
				item.setInEffect(tg[0], false);
			}
			
			TravellersGearAPI.setExtendedInventory(player, tg);
		}
		return true;
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true, isMandatory = true)
	public static void addHiddenTooltip(ItemHolyCloak cloak, ItemStack stack, EntityPlayer player, List tooltip, boolean adv) {
		try {
			if (AlfheimCore.TravellersGearLoaded) {
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("TG.desc.gearSlot.tg.0"), tooltip);
				String key = RenderHelper.getKeyDisplayString("TG.keybind.openInv");
				if(key != null)
					TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("alfheimmisc.tgtooltip").replaceAll("%key%", key), tooltip);
			} else {
				BaubleType type = cloak.getBaubleType(stack);
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletype." + type.name().toLowerCase()), tooltip);
				String key = RenderHelper.getKeyDisplayString("Baubles Inventory");
				if(key != null)
					TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletooltip").replaceAll("%key%", key), tooltip);
			}
		} catch (Throwable e) {}
		
		ItemStack cosmetic = cloak.getCosmeticItem(stack);
		if(cosmetic != null)
			addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.getDisplayName()), tooltip);
		
		if(cloak.hasPhantomInk(stack))
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), tooltip);
	}
	
	public static void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}
}