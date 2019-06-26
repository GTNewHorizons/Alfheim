package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimConfig
import baubles.api.BaubleType
import baubles.api.IBauble
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.api.mana.IManaTooltipDisplay
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemManaStorage(name: String, maxManaCap: Double, val type: BaubleType): Item(), IManaItem, IManaTooltipDisplay, IBauble {
	val MAX_MANA: Int
	
	init {
		MAX_MANA = (TilePool.MAX_MANA * maxManaCap).toInt()
		this.creativeTab = AlfheimCore.alfheimTab
		this.maxDamage = 1000
		this.setMaxStackSize(1)
		this.setNoRepair()
		this.setTextureName(ModInfo.MODID + ':'.toString() + name)
		this.unlocalizedName = name
	}
	
	override fun getSubItems(par1: Item, par2CreativeTabs: CreativeTabs?, par3List: MutableList<*>) {
		par3List.add(ItemStack(par1, 1, 1000))
		/*ItemStack full = new ItemStack(par1, 1, 1);
		setMana(full, MAX_MANA);
		par3List.add(full);*/
	}
	
	override fun getDamage(stack: ItemStack): Int {
		val mana = getMana(stack).toFloat()
		return 1000 - (mana / getMaxMana(stack) * 1000).toInt()
	}
	
	override fun getDisplayDamage(stack: ItemStack): Int {
		return getDamage(stack)
	}
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
		return Integer.MAX_VALUE
	}
	
	override fun getMana(stack: ItemStack?): Int {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0)
	}
	
	override fun getMaxMana(stack: ItemStack?): Int {
		return MAX_MANA
	}
	
	override fun addMana(stack: ItemStack, mana: Int) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)))
		stack.itemDamage = getDamage(stack)
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, pool: TileEntity): Boolean {
		return true
	}
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack): Boolean {
		return true
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
	
	override fun getManaFractionForDisplay(stack: ItemStack): Float {
		return getMana(stack).toFloat() / getMaxMana(stack).toFloat()
	}
	
	override fun getBaubleType(stack: ItemStack): BaubleType {
		return type
	}
	
	override fun onWornTick(stack: ItemStack, entity: EntityLivingBase) {
		// NO-OP
	}
	
	override fun onEquipped(stack: ItemStack, entity: EntityLivingBase) {
		// NO-OP
	}
	
	override fun onUnequipped(stack: ItemStack, entity: EntityLivingBase) {
		// NO-OP
	}
	
	override fun canEquip(stack: ItemStack, entity: EntityLivingBase): Boolean {
		return true
	}
	
	override fun canUnequip(stack: ItemStack, entity: EntityLivingBase): Boolean {
		return true
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<*>, b: Boolean) {
		list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc0", MAX_MANA / TilePool.MAX_MANA))
		if (AlfheimConfig.numericalMana) list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc1", getMana(stack), getMaxMana(stack)))
	}
	
	companion object {
		val TAG_MANA = "mana"
		
		fun setMana(stack: ItemStack, mana: Int) {
			ItemNBTHelper.setInt(stack, TAG_MANA, mana)
		}
	}
}
