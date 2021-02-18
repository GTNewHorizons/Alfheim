package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.ASJTile
import net.minecraft.entity.player.*
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.MinecraftServer

class TileEnderActuator: ASJTile(), IInventory {
	
	var name = "Notch"
	
	override fun getStackInSlot(slot: Int) = getEnderChest()?.get(slot)
	
	override fun decrStackSize(slot: Int, size: Int) = getEnderChest()?.decrStackSize(slot, size)
	
	override fun getSizeInventory() = getEnderChest()?.sizeInventory ?: 0
	
	override fun getStackInSlotOnClosing(slot: Int) = getEnderChest()?.getStackInSlotOnClosing(slot)
	
	override fun hasCustomInventoryName() = getEnderChest()?.hasCustomInventoryName() ?: false
	
	override fun isItemValidForSlot(slot: Int, stack: ItemStack?) = getEnderChest()?.isItemValidForSlot(slot, stack) ?: false
	
	override fun getInventoryName() = getEnderChest()?.inventoryName
	
	override fun getInventoryStackLimit() = getEnderChest()?.inventoryStackLimit ?: 0
	
	override fun isUseableByPlayer(player: EntityPlayer?) = getEnderChest()?.isUseableByPlayer(player) ?: false
	
	override fun openInventory() = getEnderChest()?.openInventory() ?: Unit
	
	override fun closeInventory() = getEnderChest()?.closeInventory() ?: Unit
	
	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) = getEnderChest()?.set(slot, stack) ?: Unit
	
	fun getEnderChest() = if (ASJUtilities.isServer) (MinecraftServer.getServer().configurationManager.playerEntityList.firstOrNull { it is EntityPlayerMP && it.commandSenderName == name } as? EntityPlayerMP)?.inventoryEnderChest else null
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		super.writeToNBT(nbt)
		nbt.setString(TAG_PLAYER_NAME, name)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		super.readFromNBT(nbt)
		name = nbt.getString(TAG_PLAYER_NAME)
	}
	
	companion object {
		
		const val TAG_PLAYER_NAME = "name"
	}
}
