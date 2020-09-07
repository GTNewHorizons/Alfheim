package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.event.entity.living.LivingHurtEvent
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.item.equipment.bauble.ItemHolyCloak

class ItemBalanceCloak: ItemHolyCloak("BalanceCloak") {
	
	init {
		creativeTab = AlfheimTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		iconHoly = IconHelper.forName(reg, "cloak_holy")
		iconUnholy = IconHelper.forName(reg, "cloak_unholy")
		itemIcon = IconHelper.forName(reg, "cloak_balance")
	}
	
	override fun effectOnDamage(event: LivingHurtEvent, player: EntityPlayer, stack: ItemStack?): Boolean {
		if (!event.source.isMagicDamage) {
			event.ammount /= 2f
			
			if (event.source.entity != null)
				event.source.entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), event.ammount)
			
			if (event.ammount > player.health)
				event.ammount = player.health - 1
			
			player.playSoundAtEntity("botania:holyCloak", 1f, 1f)
			
			for (i in 0..29) {
				val x = player.posX + Math.random() * player.width.D * 2.0 - player.width
				val y = player.posY + Math.random() * player.height
				val z = player.posZ + Math.random() * player.width.D * 2.0 - player.width
				val green = Math.random() > 0.5
				Botania.proxy.sparkleFX(player.worldObj, x, y, z, 0.3f, if (green) 1f else 0.3f, if (green) 0.3f else 1f, 0.8f + Math.random().F * 0.4f, 3)
			}
			return true
		}
		
		return false
	}
	
	@SideOnly(Side.CLIENT)
	override fun getRenderTexture() = LibResourceLocations.cloakBalance
	
	companion object {
		lateinit var iconHoly: IIcon
		lateinit var iconUnholy: IIcon
	}
}