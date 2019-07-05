package alfheim.common.item.equipment.armor.elemental

import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

class ItemElementalFireLeggings: ElementalArmor(2, "ElementalFireLeggings") {
	
	companion object {
		val uuid = UUID.fromString("d162f3e4-87f5-43cc-b33e-85b8b4f4cb75")!!
	}
	
	init {
		this.creativeTab = AlfheimCore.alfheimTab
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun getPixieChance(stack: ItemStack): Float {
		return 0.15f
	}
	
	@SubscribeEvent
	fun updatePlayerStepStatus(event: LivingEvent.LivingUpdateEvent) {
		if (event.entityLiving is EntityPlayer) {
			val player = event.entityLiving as EntityPlayer
			val belt = player.getCurrentArmor(1)
			if (belt?.item is ItemElementalFireLeggings && ManaItemHandler.requestManaExact(belt, player, 1, false)) {
				if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0.0f && !player.isInsideOfMaterial(Material.water)) {
					val speed = 0.185F
					player.moveFlying(0.0f, 1.0f, if (player.capabilities.isFlying) speed else speed)
					if (player.ticksExisted % 10 == 0) {
						ManaItemHandler.requestManaExact(belt, player, 1, true)
					}
				}
			}
		}
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
		if (!world.isRemote && armorType == 2 && player.getCurrentArmor(1)?.item === AlfheimItems.elementalLeggings) {
			if (player.isBurning && ManaItemHandler.requestManaExact(stack, player, 10, true)) player.extinguish()
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, b: Boolean) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc2"))
		super.addInformation(stack, player, list, b)
	}
}
