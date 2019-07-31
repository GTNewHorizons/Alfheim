package alfheim.common.item.creator

import alfheim.common.item.ItemMod
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI

class ItemRoyalStaff: ItemMod("RoyalStaff") {
	
	init {
		creativeTab = null
		setFull3D()
		maxStackSize = 1
		maxDamage = 0
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, slot: Int, equiped: Boolean) {
		if (entity.commandSenderName != "AlexSocol") {
			if (entity is EntityPlayer)
				while (entity.inventory.consumeInventoryItem(this));
			else stack.stackSize = 0
		}
	}
	
	override fun getRarity(itemstack: ItemStack) = BotaniaAPI.rarityRelic!!
	
	override fun getItemUseAction(par1ItemStack: ItemStack) = EnumAction.bow
	
	override fun getMaxItemUseDuration(itemstack: ItemStack) = 2147483647
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
}
