package alfheim.common.item

import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import org.apache.commons.lang3.ArrayUtils
import vazkii.botania.api.mana.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import kotlin.math.min

class ItemLootInterceptor: ItemMod("LootInterceptor"), IManaItem, IManaTooltipDisplay {

	init {
		maxStackSize = 1
		FMLCommonHandler.instance().bus().register(this)
	}
	
	override fun onUpdate(stack: ItemStack?, world: World?, holder: Entity?, inSlot: Int, inHand: Boolean) {
		if (holder is EntityPlayer) {
			
			val player = holder as EntityPlayer?
			var slot: ItemStack?
			val ids = getIDs(stack)
			val metas = getMetas(stack)
			
			for (i in 0 until player!!.inventory.sizeInventory) {
				slot = player.inventory.getStackInSlot(i)
				
				if (slot != null) {
					val cid = getIdFromItem(slot.item)
					var pos = -1
					
					for (id in ids) {
						++pos
						
						if (id == cid) {
							if (metas[pos] == slot.itemDamage) {
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
	
	override fun getManaFractionForDisplay(stack: ItemStack): Float {
		return getMana(stack).toFloat() / getMaxMana(stack).toFloat()
	}
	
	override fun getMana(stack: ItemStack?): Int {
		return getInt(stack, TAG_MANA, 0)
	}
	
	override fun getMaxMana(stack: ItemStack?): Int {
		return TilePool.MAX_MANA
	}
	
	override fun addMana(stack: ItemStack?, mana: Int) {
		setMana(stack, min(getMana(stack) + mana, getMaxMana(stack)))
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, pool: TileEntity): Boolean {
		return false
	}
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack): Boolean {
		return false
	}
	
	override fun canExportManaToPool(stack: ItemStack, pool: TileEntity): Boolean {
		return true
	}
	
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack): Boolean {
		return true
	}
	
	override fun isNoExport(stack: ItemStack): Boolean {
		return false
	}
	
	companion object {
		
		const val TAG_IDS = "ids"
		const val TAG_METAS = "metas"
		const val TAG_MANA = "mana"
		val EMPTY = IntArray(0)
		const val PER_ITEM = 60
		
		fun add(stack: ItemStack, id: Int, meta: Int) {
			setIDs(stack, ArrayUtils.add(getIDs(stack), id))
			setMetas(stack, ArrayUtils.add(getMetas(stack), meta))
		}
		
		fun setIDs(stack: ItemStack, ids: IntArray) {
			setIntArray(stack, TAG_IDS, ids)
		}
		
		fun setMetas(stack: ItemStack, metas: IntArray) {
			setIntArray(stack, TAG_METAS, metas)
		}
		
		fun setIntArray(stack: ItemStack, tag: String, array: IntArray) {
			getNBT(stack).setIntArray(tag, array)
		}
		
		fun getIDs(stack: ItemStack?): IntArray {
			return getIntArray(stack, TAG_IDS, EMPTY)
		}
		
		fun getMetas(stack: ItemStack?): IntArray {
			return getIntArray(stack, TAG_METAS, EMPTY)
		}
		
		fun getIntArray(stack: ItemStack?, tag: String, defaultExpected: IntArray): IntArray {
			return if (verifyExistance(stack, tag)) getNBT(stack!!).getIntArray(tag) else defaultExpected
		}
		
		fun setMana(stack: ItemStack?, mana: Int) {
			setInt(stack!!, TAG_MANA, mana)
		}
	}
}
