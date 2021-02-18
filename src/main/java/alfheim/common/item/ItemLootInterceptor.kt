package alfheim.common.item

import alexsocol.asjlib.*
import alexsocol.asjlib.ItemNBTHelper.getInt
import alexsocol.asjlib.ItemNBTHelper.getIntArray
import alexsocol.asjlib.ItemNBTHelper.getNBT
import alexsocol.asjlib.ItemNBTHelper.setInt
import alfheim.client.core.helper.IconHelper
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import org.apache.commons.lang3.ArrayUtils
import vazkii.botania.api.mana.*
import kotlin.math.min

class ItemLootInterceptor: ItemMod("LootInterceptor"), IManaItem, IManaTooltipDisplay {
	
	init {
		maxStackSize = 1
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, player: Entity?, inSlot: Int, inHand: Boolean) {
		if (player !is EntityPlayer || stack.meta == 0)
			return
		
		val ids = getIDs(stack)
		val metas = getMetas(stack)
		
		for (i in 0 until player.inventory.sizeInventory) {
			if (i == inSlot) continue
			
			val slot = player.inventory[i] ?: continue
			
			val pos = ids.indexOf(slot.item.id)
			if (pos == -1 || metas[pos] != slot.meta) continue
			
			val size = slot.stackSize
			player.inventory[i] = null
			addMana(stack, PER_ITEM * size)
		}
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!player.isSneaking) return stack
		
		stack.meta = stack.meta.inv() and 1
		world.playSoundAtEntity(player, "random.orb", 0.3f, 0.1f)
		
		return stack
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		addStringToTooltip(list, StatCollector.translateToLocal("botaniamisc.${if (stack.meta == 0) "in" else ""}active"))
	}
	
	override fun registerIcons(reg: IIconRegister) {
		super.registerIcons(reg)
		iconActive = IconHelper.forItem(reg, this, "Active")
	}
	
	override fun getIconFromDamage(meta: Int) = if (meta == 1) iconActive else itemIcon
	override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack).F / getMaxMana(stack).F
	override fun getMana(stack: ItemStack) = getInt(stack, TAG_MANA, 0)
	override fun getMaxMana(stack: ItemStack) = 1000000
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
		
		lateinit var iconActive: IIcon
		
		fun add(stack: ItemStack, id: Int, meta: Int) {
			setIDs(stack, ArrayUtils.add(getIDs(stack), id))
			setMetas(stack, ArrayUtils.add(getMetas(stack), meta))
		}
		
		fun setIDs(stack: ItemStack, ids: IntArray) = setIntArray(stack, TAG_IDS, ids)
		fun setMetas(stack: ItemStack, metas: IntArray) = setIntArray(stack, TAG_METAS, metas)
		fun setIntArray(stack: ItemStack, tag: String, array: IntArray) = getNBT(stack).setIntArray(tag, array)
		fun getIDs(stack: ItemStack) = getIntArray(stack, TAG_IDS)
		fun getMetas(stack: ItemStack) = getIntArray(stack, TAG_METAS)
		fun setMana(stack: ItemStack, mana: Int) = setInt(stack, TAG_MANA, mana)
	}
}
