package alfheim.common.core.helper

import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.corporea.*
import vazkii.botania.common.core.helper.InventoryHelper

object CorporeaInsertHelper {
	
	/**
	 * @param spark Spark above current inventory to get connections from
	 * @param stack Stack to put to connected network
	 */
	fun putToNetwork(spark: ICorporeaSpark, stack: ItemStack): ItemStack? {
		val inventories = CorporeaHelper.getInventoriesOnNetwork(spark)
		
		if (inventories.isEmpty()) return stack
		
		for (inv in inventories) {
			if (stack.stackSize <= 0) return null
			insertTo(spark, stack, inv) ?: return null
		}
		
		if (stack.stackSize <= 0) return null
		
		for (inv in inventories) {
			if (stack.stackSize <= 0) return null
			insertToHard(spark, stack, inv) ?: return null
		}
		
		// nothing to do if not the whole stack has been inserted
		return stack
	}
	
	fun insertToHard(spark: ICorporeaSpark?, stack: ItemStack, inv: IInventory): ItemStack? {
		val canPut = InventoryHelper.testInventoryInsertion(inv, stack, ForgeDirection.UP)
		if (canPut <= 0) return stack
		InventoryHelper.insertItemIntoInventory(inv, stack.splitStack(canPut), ForgeDirection.UP, -1)
		spark?.onItemExtracted(stack)
		return if (stack.stackSize <= 0) null else stack
	}
	
	fun insertTo(spark: ICorporeaSpark?, stack: ItemStack, inv: IInventory): ItemStack? {
		var did = false
		
		for (i in 0 until inv.sizeInventory) {
			if (stack.stackSize <= 0) return null
			
			if (!isValidSlot(inv, i)) continue
			
			val stackAt = inv.getStackInSlot(i)
			
			if (CorporeaHelper.stacksMatch(stack, stackAt, true)) {
				val canPut = stackAt.maxStackSize - stackAt.stackSize
				if (canPut <= 0) continue
				
				val place = if (stack.stackSize >= canPut) stack.splitStack(canPut) else stack.copy().also { stack.stackSize = 0 }
				
				InventoryHelper.insertItemIntoInventory(inv, place, ForgeDirection.UP, i)
				
				did = true
				
				spark?.onItemExtracted(stack)
			}
		}
		
		if (did) {
			inv.markDirty()
		}
		
		return stack
	}
	
	fun isValidSlot(inv: IInventory, slot: Int): Boolean {
		return inv !is ISidedInventory || slot in inv.getAccessibleSlotsFromSide(ForgeDirection.UP.ordinal) && inv.canInsertItem(slot, inv.getStackInSlot(slot), ForgeDirection.UP.ordinal)
	}
}