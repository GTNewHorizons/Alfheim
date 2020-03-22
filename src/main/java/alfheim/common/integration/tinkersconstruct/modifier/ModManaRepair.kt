package alfheim.common.integration.tinkersconstruct.modifier

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimModule
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import tconstruct.library.ActiveToolMod
import tconstruct.library.modifier.IModifyable
import tconstruct.library.tools.*
import tconstruct.library.weaponry.IAmmo
import tconstruct.modifiers.tools.ModBoolean
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper

object ModManaRepair: ModBoolean(arrayOf(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore)), AlfheimConfigHandler.modifierIDs[0], "ManaCore", "${EnumChatFormatting.AQUA}", StatCollector.translateToLocal("modifier.tool.manacore")) {
	
	override fun canModify(stack: ItemStack, input: Array<ItemStack?>?): Boolean {
		val tool = stack.item as? IModifyable ?: return false
		if (tool.traits.contains("ammo")) return false
		val tag = ItemNBTHelper.getCompound(stack, "InfiTool", true) ?: return false
		if (tag.getInteger("Head") in TinkersConstructAlfheimModule.manaRepairMaterials) return false
		if (tag.getBoolean("Moss") || tag.getBoolean("Flux")) return false
		
		return tag.getInteger("Modifiers") > 0 && !tag.getBoolean(key)
	}
}

object AModNatural: ActiveToolMod() {
	
	override fun updateTool(tool: ToolCore, stack: ItemStack, world: World, entity: Entity?) {
		val player = entity as? EntityPlayer ?: return
		if (tool is IAmmo) return
		val tag = ItemNBTHelper.getCompound(stack, tool.baseTagName, true) ?: return
		
		repair(stack, tag, player)
		generareMana(stack, tag, player)
	}
	
	fun repair(stack: ItemStack, tag: NBTTagCompound, player: EntityPlayer) {
		val damage = tag.getInteger("Damage")
		
		if (damage <= 0) return
		
		val cost = getRepairCost(tag) * 2
		if (cost > 0 && ManaItemHandler.requestManaExactForTool(stack, player, cost, true)) {
			AbilityHelper.healTool(stack, 1, player, true)
		}
	}
	
	fun generareMana(stack: ItemStack, tag: NBTTagCompound, player: EntityPlayer) {
		val headMaterial = tag.getInteger("Head")
		
		val addDelay = if (stack === player.currentEquippedItem) 1 else 10
		
		if (headMaterial in TinkersConstructAlfheimModule.manaGenMaterials && player.ticksExisted % (TinkersConstructAlfheimModule.manaGenDelay[headMaterial] ?: 20) * addDelay == 0)
			ManaItemHandler.dispatchManaExact(stack, player, 1, true)
	}
	
	override fun damageTool(stack: ItemStack, damage: Int, entity: EntityLivingBase?): Boolean {
		if (damage <= 0) return false
		val player = entity as? EntityPlayer ?: return false
		val item = stack.item as? IModifyable ?: return false
		if (item.traits.contains("ammo")) return false
		val tag = ItemNBTHelper.getCompound(stack, item.baseTagName, true) ?: return false
		return ManaItemHandler.requestManaExactForTool(stack, player, getRepairCost(tag), true)
	}
	
	fun getRepairCost(tag: NBTTagCompound): Int {
		val headMaterial = tag.getInteger("Head")
		
		return when {
			headMaterial in TinkersConstructAlfheimModule.manaRepairMaterials -> TinkersConstructAlfheimModule.manaRepairCost[headMaterial] ?: 10
			tag.getBoolean(ModManaRepair.key)                                 -> 5
			else                                                              -> 0
		}
	}
}