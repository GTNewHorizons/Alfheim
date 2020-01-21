package alfheim.common.item.equipment.bauble

import alfheim.common.core.util.AlfheimTab
import baubles.api.BaubleType
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import kotlin.math.min

class ItemManaStorageRing(name: String, maxManaCap: Double): ItemBauble(name), IManaItem, IManaTooltipDisplay {
	
	val MAX_MANA = (TilePool.MAX_MANA * maxManaCap).toInt()
	
	init {
		creativeTab = AlfheimTab
		maxDamage = 1000
		maxStackSize = 1
		setNoRepair()
	}

	override fun getSubItems(par1: Item, par2CreativeTabs: CreativeTabs?, par3List: MutableList<Any?>) {
		par3List.add(ItemStack(par1, 1, 1000))
		/*ItemStack full = new ItemStack(par1, 1, 1);
		setMana(full, MAX_MANA);
		par3List.add(full);*/
	}
	
	override fun getDamage(stack: ItemStack): Int {
		val mana = getMana(stack).toFloat()
		return 1000 - (mana / getMaxMana(stack) * 1000).toInt()
	}
	
	override fun getDisplayDamage(stack: ItemStack) = getDamage(stack)
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?) = Integer.MAX_VALUE
	
	override fun getMana(stack: ItemStack?) = ItemNBTHelper.getInt(stack, TAG_MANA, 0)
	
	override fun getMaxMana(stack: ItemStack?) = MAX_MANA
	
	override fun addMana(stack: ItemStack, mana: Int) {
		setMana(stack, min(getMana(stack) + mana, getMaxMana(stack)))
		stack.itemDamage = getDamage(stack)
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, pool: TileEntity) = true
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack) = true
	
	override fun canExportManaToPool(stack: ItemStack, pool: TileEntity) = true
	
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack) = true
	
	override fun isNoExport(stack: ItemStack) = false
	
	override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack).toFloat() / getMaxMana(stack).toFloat()
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.RING
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc0", MAX_MANA / TilePool.MAX_MANA))
		list.add("")
		
		super.addHiddenTooltip(stack, player, list, adv)
	}
	
	companion object {
		const val TAG_MANA = "mana"
		
		fun setMana(stack: ItemStack, mana: Int) {
			ItemNBTHelper.setInt(stack, TAG_MANA, mana)
		}
	}
}
