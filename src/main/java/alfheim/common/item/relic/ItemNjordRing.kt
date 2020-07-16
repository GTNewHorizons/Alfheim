package alfheim.common.item.relic

import alexsocol.asjlib.getActivePotionEffect
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.ItemRelicBauble
import kotlin.math.*

class ItemNjordRing: ItemRelicBauble("NjordRing") {
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase?) {
		super.onWornTick(stack, player)
		
		if (player !is EntityPlayer) return
		
		if (player.isInWater) {
			player.stepHeight = 1f
			
			heal(player, stack)
			waterSpeed(player, stack)
		} else {
			player.stepHeight = 0.5f
		}
	}
	
	fun heal(player: EntityPlayer, ring: ItemStack) {
		if (player.ticksExisted % 100 == 0 && player.shouldHeal())
			player.heal(ManaItemHandler.requestMana(ring, player, 100, true) / 100f)
	}
	
	fun waterSpeed(player: EntityPlayer, ring: ItemStack) {
		val motionX = player.motionX * 1.2
		val motionY = player.motionY * 1.2
		val motionZ = player.motionZ * 1.2
		
		var changeX = min(1.3, abs(motionX)) == abs(motionX)
		var changeY = min(1.3, abs(motionY)) == abs(motionY)
		var changeZ = min(1.3, abs(motionZ)) == abs(motionZ)
		
		if (player.capabilities.isFlying) {
			changeX = false
			changeY = false
			changeZ = false
		}
		
		if (changeX) player.motionX = motionX
		if (changeY) player.motionY = motionY
		if (changeZ) player.motionZ = motionZ
		
		if (player.isInsideOfMaterial(Material.water) && player.getActivePotionEffect (Potion.nightVision.id) == null) {
			val neweffect = PotionEffect(Potion.nightVision.id, 20, -42, true)
			player.addPotionEffect(neweffect)
		}
		
		if (player.air <= 1) {
			val mana = ManaItemHandler.requestMana(ring, player, 300, true)
			if (mana > 0) // If zero gets in the player has no air but won't drown.
				player.air = mana
		}
	}
	
	companion object {
		
		fun getNjordRing(player: EntityPlayer): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player) ?: return null
			val stack1 = baubles.getStackInSlot(1)
			val stack2 = baubles.getStackInSlot(2)
			return if (isNjordRing(stack1)) stack1 else if (isNjordRing(stack2)) stack2 else null
		}
		
		private fun isNjordRing(stack: ItemStack?): Boolean {
			return stack != null && (stack.item === AlfheimItems.priestRingNjord || stack.item === ModItems.aesirRing)
		}
	}
}
