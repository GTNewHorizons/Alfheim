package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.getActivePotionEffect
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimTab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraftforge.client.event.RenderPlayerEvent
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.*
import vazkii.botania.client.core.helper.IconHelper

class ItemInvisibilityCloak: ItemBaubleCloak("InvisibilityCloak"), IManaUsingItem {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun registerIcons(reg: IIconRegister?) {
		itemIcon = IconHelper.forName(reg, "cloak_invisibility")
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		super.onWornTick(stack, player)
		
		if (player is EntityPlayer && !player.worldObj.isRemote) {
			val manaCost = 2
			val hasMana = ManaItemHandler.requestManaExact(stack, player, manaCost, false)
			if (!hasMana)
				onUnequipped(stack, player)
			else {
				if (player.getActivePotionEffect(Potion.invisibility.id) != null)
					player.removePotionEffect(Potion.invisibility.id)
				ManaItemHandler.requestManaExact(stack, player, manaCost, true)
				player.addPotionEffect(PotionEffect(Potion.invisibility.id, 10, -42, true))
			}
		}
	}
	
	override fun onUnequipped(stack: ItemStack?, player: EntityLivingBase) {
		val effect = player.getActivePotionEffect(Potion.invisibility.id)
		if (effect != null && player is EntityPlayer && effect.amplifier == -42)
			player.removePotionEffect(Potion.invisibility.id)
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun getCloakTexture(stack: ItemStack) = LibResourceLocations.cloakBalance
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) = Unit
}