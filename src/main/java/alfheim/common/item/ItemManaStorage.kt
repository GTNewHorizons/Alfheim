package alfheim.common.item

import alfheim.common.core.util.*
import net.minecraft.client.gui.GuiScreen
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper
import kotlin.math.min

class ItemManaStorage(name: String, maxManaCap: Double): ItemMod(name), IManaItem, IManaTooltipDisplay {
	
	val MAX_MANA = (TilePool.MAX_MANA * maxManaCap).I
	
	init {
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
		val mana = getMana(stack).F
		return 1000 - (mana / getMaxMana(stack) * 1000).I
	}
	
	override fun getDisplayDamage(stack: ItemStack) = getDamage(stack)
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?) = Integer.MAX_VALUE
	
	override fun getMana(stack: ItemStack?) = ItemNBTHelper.getInt(stack, TAG_MANA, 0)
	
	override fun getMaxMana(stack: ItemStack?) = MAX_MANA
	
	override fun addMana(stack: ItemStack, mana: Int) {
		setMana(stack, min(getMana(stack) + mana, getMaxMana(stack)))
		stack.meta = getDamage(stack)
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, pool: TileEntity) = true
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack) = true
	
	override fun canExportManaToPool(stack: ItemStack, pool: TileEntity) = true
	
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack) = true
	
	override fun isNoExport(stack: ItemStack) = false
	
	override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack).F / getMaxMana(stack).F
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		if (GuiScreen.isShiftKeyDown()) {
			list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc0", MAX_MANA / TilePool.MAX_MANA))
		} else
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), list)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) {
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	companion object {
		const val TAG_MANA = "mana"
		
		fun setMana(stack: ItemStack, mana: Int) {
			ItemNBTHelper.setInt(stack, TAG_MANA, mana)
		}
	}
}
