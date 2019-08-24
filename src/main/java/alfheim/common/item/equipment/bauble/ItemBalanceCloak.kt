// This is here since ItemHolyCloak#getRenderTexture is package-private
package vazkii.botania.common.item.equipment.bauble

import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimTab
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingHurtEvent
import vazkii.botania.common.Botania

class ItemBalanceCloak: ItemHolyCloak("BalanceCloak") {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun effectOnDamage(event: LivingHurtEvent, player: EntityPlayer, stack: ItemStack?): Boolean {
		if (!event.source.isMagicDamage) {
			event.ammount /= 2f
			
			if (event.source.entity != null)
				event.source.entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), event.ammount)
			
			if (event.ammount > player.health)
				event.ammount = player.health - 1
			
			player.worldObj.playSoundAtEntity(player, "botania:holyCloak", 1f, 1f)
			
			for (i in 0..29) {
				val x = player.posX + Math.random() * player.width.toDouble() * 2.0 - player.width
				val y = player.posY + Math.random() * player.height
				val z = player.posZ + Math.random() * player.width.toDouble() * 2.0 - player.width
				val green = Math.random() > 0.5
				Botania.proxy.sparkleFX(player.worldObj, x, y, z, 0.3f, if (green) 1f else 0.3f, if (green) 0.3f else 1f, 0.8f + Math.random().toFloat() * 0.4f, 3)
			}
			return true
		}
		
		return false
	}
	
	public override fun getRenderTexture() = LibResourceLocations.balanceCloak
}