package alfheim.common.item.equipment.armor.elemental

import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent
import net.minecraftforge.event.entity.living.LivingFallEvent
import vazkii.botania.api.mana.ManaItemHandler

class ItemElementalAirBoots: ElementalArmor(3, "ElementalAirBoots") {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.09f
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc1"))
		super.addInformation(stack, player, list, b)
	}
	
	@SubscribeEvent
	fun onEntityJump(event: LivingJumpEvent) {
		if (armorType == 3 && event.entityLiving is EntityPlayer && (event.entityLiving as EntityPlayer).getCurrentArmor(0) != null && (event.entityLiving as EntityPlayer).getCurrentArmor(0).item === AlfheimItems.elementalBoots && ManaItemHandler.requestManaExact((event.entityLiving as EntityPlayer).getCurrentArmor(0), event.entityLiving as EntityPlayer, ONEBLOCKCOST * 10, true)) {
			event.entityLiving.motionY += 0.5
		}
	}
	
	@SubscribeEvent
	fun onEntityFall(event: LivingFallEvent) {
		if (armorType == 3 && event.entityLiving is EntityPlayer && (event.entityLiving as EntityPlayer).getCurrentArmor(0) != null && (event.entityLiving as EntityPlayer).getCurrentArmor(0).item === AlfheimItems.elementalBoots) {
			if (event.distance < 4.5063215) event.distance = 0f
			
			if (event.distance >= 4.5063215) {
				val decrease = ManaItemHandler.requestMana((event.entityLiving as EntityPlayer).getCurrentArmor(0), event.entityLiving as EntityPlayer, MathHelper.floor_float(event.distance) * ONEBLOCKCOST, true)
				event.distance -= (decrease / ONEBLOCKCOST).toFloat()
			}
		}
	}
	
	companion object {
		
		const val ONEBLOCKCOST = 10
	}
}
