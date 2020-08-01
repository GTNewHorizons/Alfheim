package alfheim.common.item.equipment.bauble

import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.item.equipment.bauble.faith.IFaithHandler
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.client.core.helper.IconHelper

class ItemAesirCloak: ItemBaubleCloak("AesirCloak"), IManaUsingItem {
	
	override fun registerIcons(reg: IIconRegister?) {
		itemIcon = IconHelper.forName(reg, "cloak_aesir")
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase?) {
		if (player is EntityPlayer) for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onEquipped(stack, player, IFaithHandler.FaithBauble.CLOAK)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase?) {
		if (player is EntityPlayer) for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onWornTick(stack, player, IFaithHandler.FaithBauble.CLOAK)
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityLivingBase?) {
		if (player is EntityPlayer) for (i in 0 until ItemPriestEmblem.TYPES) IFaithHandler.getFaithHandler(i).onUnequipped(stack, player, IFaithHandler.FaithBauble.CLOAK)
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun getCloakTexture(stack: ItemStack) = LibResourceLocations.cloakAesir
	
	override fun getCloakGlowTexture(stack: ItemStack) = LibResourceLocations.cloakAesirGlow
}
