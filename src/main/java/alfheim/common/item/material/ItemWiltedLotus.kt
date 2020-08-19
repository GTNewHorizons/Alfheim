package alfheim.common.item.material

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.common.item.ItemMod
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.botania.api.item.IManaDissolvable
import vazkii.botania.api.mana.IManaPool
import vazkii.botania.common.Botania

class ItemWiltedLotus: ItemMod("wiltedLotus"), IManaDissolvable {
	
	init {
		setHasSubtypes(true)
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0..1) list.add(ItemStack(item, 1, i))
	}
	
	override fun hasEffect(par1ItemStack: ItemStack, pass: Int) = par1ItemStack.meta > 0
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocal("misc.${ModInfo.MODID}:lotusDesc"))
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack) =
		super.getUnlocalizedNameInefficiently(stack) + stack.meta
	
	override fun onDissolveTick(pool: IManaPool, stack: ItemStack, item: EntityItem) {
		if (pool.isFull || pool.currentMana == 0) return
		
		val tile = pool as TileEntity
		val t2 = stack.meta > 0
		
		val mult = if (item.worldObj.rand.nextBoolean()) 2 else -1
		val mana = if (t2) MANA_PER_T2 else MANA_PER
		
		if (mult < -1 && pool.currentMana < mana)
			return
		
		if (!item.worldObj.isRemote) {
			pool.recieveMana(mult * mana)
			stack.stackSize--
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(item.worldObj, tile.xCoord, tile.yCoord, tile.zCoord)
		}
		
		for (i in 0..49) {
			val r = Math.random().F * 0.25f
			val g = 0f
			val b = Math.random().F * 0.25f
			val s = 0.45f * Math.random().F * 0.25f
			val m = 0.045f
			val mx = (Math.random().F - 0.5f) * m
			val my = Math.random().F * m
			val mz = (Math.random().F - 0.5f) * m
			Botania.proxy.wispFX(item.worldObj, item.posX, tile.yCoord + 0.5, item.posZ, r, g, b, s, mx, my, mz)
		}
		
		item.worldObj.playSoundAtEntity(item, "botania:blackLotus", 0.5f, if (t2) 0.1f else 1f)
	}
	
	companion object {
		const val MANA_PER = 8000
		const val MANA_PER_T2 = 100000
	}
}
