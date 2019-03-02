package alfheim.common.item.equipment.bauble;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.common.integration.travellersgear.handler.TGHandlerBotaniaAdapter;
import baubles.api.BaubleType;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import travellersgear.api.ITravellersGear;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.equipment.bauble.ItemHolyCloak;

@Optional.Interface(modid = "TravellersGear", iface = "travellersgear.api.ITravellersGear", striprefs = true)
public class ItemInvisibilityCloak extends ItemBauble implements IManaUsingItem, ITravellersGear {
	
	public static final String TAG_EQUIPPED = "equipped"; // damn it, next time check your sync!
	
	public ItemInvisibilityCloak() {
		super("InvisibilityCloak");
		setCreativeTab(AlfheimCore.alfheimTab);
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return AlfheimCore.TravellersGearLoaded ? null : BaubleType.BELT;
	}
	
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(Potion.invisibility);
		if(effect != null && player instanceof EntityPlayer && effect.amplifier == -42)
			player.removePotionEffect(Potion.invisibility.id);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
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
	
	@Override
	public int getSlot(ItemStack stack) {
		return 0;
	}
	
	@Override
	public void onTravelGearTick(EntityPlayer player, ItemStack stack) { // because for some reason it gots called AFTER unequip :/
		if (ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false)) onWornTick(stack, player);
	}
	
	@Override
	public void onTravelGearEquip(EntityPlayer player, ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, true);
	}
	
	@Override
	public void onTravelGearUnequip(EntityPlayer player, ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, false);
		onUnequipped(stack, player);
	}
	
	@Override
	public void addHiddenTooltip(ItemStack stack, EntityPlayer player, List tooltip, boolean adv) {
		if (AlfheimCore.TravellersGearLoaded) {
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("TG.desc.gearSlot.tg.0"), tooltip);
			String key = RenderHelper.getKeyDisplayString("TG.keybind.openInv");
			if(key != null)
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("alfheimmisc.tgtooltip").replaceAll("%key%", key), tooltip);
		} else {
			BaubleType type = getBaubleType(stack);
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletype." + type.name().toLowerCase()), tooltip);
			String key = RenderHelper.getKeyDisplayString("Baubles Inventory");
			if(key != null)
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletooltip").replaceAll("%key%", key), tooltip);
		}
		
		ItemStack cosmetic = getCosmeticItem(stack);
		if(cosmetic != null)
			TGHandlerBotaniaAdapter.addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.getDisplayName()), tooltip);
		
		if(hasPhantomInk(stack))
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), tooltip);
	}
}