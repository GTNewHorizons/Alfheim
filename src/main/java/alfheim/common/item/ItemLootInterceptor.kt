package alfheim.common.item

import alexsocol.asjlib.*
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import org.apache.commons.lang3.ArrayUtils
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import kotlin.math.min

class ItemLootInterceptor: ItemMod("LootInterceptor"), IManaItem, IManaTooltipDisplay {

	init {
		maxStackSize = 1
		FMLCommonHandler.instance().bus().register(this)
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, player: Entity?, inSlot: Int, inHand: Boolean) {
		if (player is EntityPlayer) {
			
			var slot: ItemStack?
			val ids = getIDs(stack)
			val metas = getMetas(stack)
			
			for (i in 0 until player.inventory.sizeInventory) {
				slot = player.inventory.getStackInSlot(i)
				
				if (slot != null) {
					val cid = slot.item.id
					var pos = -1
					
					for (id in ids) {
						++pos
						
						if (id == cid) {
							if (metas[pos] == slot.meta) {
								val size = slot.stackSize
								player.inventory.setInventorySlotContents(i, null)
								addMana(stack, PER_ITEM * size)
							}
						}
					}
				}
			}
		}
	}
	
	override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack).F / getMaxMana(stack).F
	override fun getMana(stack: ItemStack) = getInt(stack, TAG_MANA, 0)
	override fun getMaxMana(stack: ItemStack) = Int.MAX_VALUE
	override fun addMana(stack: ItemStack, mana: Int) = setMana(stack, min(getMana(stack) + mana, getMaxMana(stack)))
	override fun isNoExport(stack: ItemStack) = false
	override fun canReceiveManaFromPool(stack: ItemStack, pool: TileEntity) = false
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack) = false
	override fun canExportManaToPool(stack: ItemStack, pool: TileEntity) = true
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack) = true
	
	companion object {
		
		const val TAG_IDS = "ids"
		const val TAG_METAS = "metas"
		const val TAG_MANA = "mana"
		const val PER_ITEM = 5
		
		fun add(stack: ItemStack, id: Int, meta: Int) {
			setIDs(stack, ArrayUtils.add(getIDs(stack), id))
			setMetas(stack, ArrayUtils.add(getMetas(stack), meta))
		}
		
		fun setIDs(stack: ItemStack, ids: IntArray) = setIntArray(stack, TAG_IDS, ids)
		fun setMetas(stack: ItemStack, metas: IntArray) = setIntArray(stack, TAG_METAS, metas)
		fun setIntArray(stack: ItemStack, tag: String, array: IntArray) = getNBT(stack).setIntArray(tag, array)
		fun getIDs(stack: ItemStack) = getIntArray(stack, TAG_IDS)
		fun getMetas(stack: ItemStack) = getIntArray(stack, TAG_METAS)
		fun getIntArray(stack: ItemStack, tag: String) = getNBT(stack).getIntArray(tag)
		fun setMana(stack: ItemStack, mana: Int) = setInt(stack, TAG_MANA, mana)
	}
}
