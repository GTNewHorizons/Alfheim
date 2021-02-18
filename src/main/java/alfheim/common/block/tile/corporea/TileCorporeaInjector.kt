package alfheim.common.block.tile.corporea

import alexsocol.asjlib.extendables.block.ASJTile
import alfheim.common.core.helper.CorporeaAdvancedHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import vazkii.botania.api.corporea.*

class TileCorporeaInjector: ASJTile(), IInventory {
	
	val spark: ICorporeaSpark? get() = CorporeaHelper.getSparkForBlock(worldObj, xCoord, yCoord, zCoord)
	
	override fun getSizeInventory() = if (spark == null) 0 else 1
	override fun getInventoryStackLimit() = Int.MAX_VALUE
	override fun isItemValidForSlot(slot: Int, stack: ItemStack?) = true
	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) = CorporeaAdvancedHelper.putOrDrop(this, spark, stack, -1)
	
	// UNUSED
	
	override fun getStackInSlot(slot: Int) = null
	override fun decrStackSize(slot: Int, size: Int) = null
	override fun getStackInSlotOnClosing(slot: Int) = null
	override fun hasCustomInventoryName() = false
	override fun getInventoryName() = null
	override fun isUseableByPlayer(player: EntityPlayer?) = false
	override fun openInventory() = Unit
	override fun closeInventory() = Unit
}
