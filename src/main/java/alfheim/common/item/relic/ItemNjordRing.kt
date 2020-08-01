package alfheim.common.item.relic

import alexsocol.asjlib.*
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraftforge.event.entity.living.LivingEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.ItemRelicBauble
import kotlin.math.*

class ItemNjordRing: ItemRelicBauble("NjordRing") {
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		val ring = getNjordRing(player) ?: return
		
		if (player.isInWater) {
			player.stepHeight = 1f
			
			heal(player, ring)
			waterSpeed(player, ring)
		} else if (ItemNBTHelper.getBoolean(ring, TAG_WAS_IN_WATER, false)) {
			player.stepHeight = 0.5f
		}
		
		ItemNBTHelper.setBoolean(ring, TAG_WAS_IN_WATER, player.isInWater)
	}
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
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
		
		if (player.isInsideOfMaterial(Material.water)) {
			player.addPotionEffect(PotionEffect(Potion.nightVision.id, 20, 0, true))
		}
		
		if (player.air <= 1) {
			val mana = ManaItemHandler.requestMana(ring, player, 300, true)
			if (mana > 0) // If zero gets in the player has no air but won't drown.
				player.air = mana
		}
	}
	
	companion object {
		
		const val TAG_WAS_IN_WATER = "wasInWater"
		
		fun getNjordRing(player: EntityPlayer): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player) ?: return null
			val stack1 = baubles[1]
			val stack2 = baubles[2]
			return if (isNjordRing(stack1)) stack1 else if (isNjordRing(stack2)) stack2 else null
		}
		
		private fun isNjordRing(stack: ItemStack?): Boolean {
			return stack != null && (stack.item === AlfheimItems.priestRingNjord || stack.item === ModItems.aesirRing)
		}
	}
}
