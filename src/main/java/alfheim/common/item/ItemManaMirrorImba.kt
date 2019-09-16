package alfheim.common.item

import alfheim.common.core.util.AlfheimTab
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World
import vazkii.botania.api.mana.*
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper
import kotlin.math.max

class ItemManaMirrorImba: ItemMod("manaMirrorImba"), IManaItem, ICoordBoundItem, IManaTooltipDisplay {
	
	val TAG_MANA_BACKLOG = "manaBacklog"
	val TAG_MANA = "mana"
	val TAG_POS_X = "posX"
	val TAG_POS_Y = "posY"
	val TAG_POS_Z = "posZ"
	val TAG_DIM = "dim"
	
	init {
		creativeTab = AlfheimTab
		setFull3D()
		setMaxStackSize(1)
		maxDamage = 1000
		setNoRepair()
	}
	
	override fun getDamage(stack: ItemStack): Int {
		val mana = getMana(stack).toFloat()
		return 1000 - (mana / TilePool.MAX_MANA * 1000).toInt()
	}
	
	override fun getDisplayDamage(stack: ItemStack): Int {
		return getDamage(stack)
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slot: Int, inHand: Boolean) {
		if (world.isRemote)
			return
		
		val pool = getManaPool(stack)
		if (pool !is DummyPool) {
			if (pool == null)
				setMana(stack, 0)
			else {
				pool.recieveMana(getManaBacklog(stack))
				setManaBacklog(stack, 0)
				setMana(stack, pool.currentMana)
			}
		}
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.isSneaking && !world.isRemote) {
			val tile = world.getTileEntity(x, y, z)
			if (tile is IManaPool) {
				bindPool(stack, tile)
				world.playSoundAtEntity(player, "botania:ding", 1f, 1f)
				return true
			}
		}
		
		return false
	}
	
	override fun getMana(stack: ItemStack): Int {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0)
	}
	
	fun setMana(stack: ItemStack, mana: Int) {
		ItemNBTHelper.setInt(stack, TAG_MANA, max(0, mana))
	}
	
	fun getManaBacklog(stack: ItemStack): Int {
		return ItemNBTHelper.getInt(stack, TAG_MANA_BACKLOG, 0)
	}
	
	fun setManaBacklog(stack: ItemStack, backlog: Int) {
		ItemNBTHelper.setInt(stack, TAG_MANA_BACKLOG, backlog)
	}
	
	override fun getMaxMana(stack: ItemStack): Int {
		val pool = getManaPool(stack) ?: return 0
		
		return if (pool !is DummyPool) TilePool.MAX_MANA else 0
	}
	
	override fun addMana(stack: ItemStack, mana: Int) {
		setMana(stack, getMana(stack) + mana)
		setManaBacklog(stack, getManaBacklog(stack) + mana)
		
		// update pool
		
		val pool = getManaPool(stack)
		
		if (pool !is DummyPool) {
			if (pool == null)
				setMana(stack, 0)
			else {
				pool.recieveMana(getManaBacklog(stack))
				setManaBacklog(stack, 0)
				setMana(stack, pool.currentMana)
			}
		}
	}
	
	fun bindPool(stack: ItemStack, pool: TileEntity?) {
		ItemNBTHelper.setInt(stack, TAG_POS_X, pool?.xCoord ?: 0)
		ItemNBTHelper.setInt(stack, TAG_POS_Y, pool?.yCoord ?: -1)
		ItemNBTHelper.setInt(stack, TAG_POS_Z, pool?.zCoord ?: 0)
		ItemNBTHelper.setInt(stack, TAG_DIM, pool?.worldObj?.provider?.dimensionId ?: 0)
	}
	
	fun getPoolCoords(stack: ItemStack): ChunkCoordinates {
		val x = ItemNBTHelper.getInt(stack, TAG_POS_X, 0)
		val y = ItemNBTHelper.getInt(stack, TAG_POS_Y, -1)
		val z = ItemNBTHelper.getInt(stack, TAG_POS_Z, 0)
		return ChunkCoordinates(x, y, z)
	}
	
	fun getDimension(stack: ItemStack): Int {
		return ItemNBTHelper.getInt(stack, TAG_DIM, 0)
	}
	
	fun getManaPool(stack: ItemStack): IManaPool? {
		val server = MinecraftServer.getServer() ?: return DummyPool
		
		val coords = getPoolCoords(stack)
		if (coords.posY == -1)
			return null
		
		val dim = getDimension(stack)
		var world: World? = null
		for (w in server.worldServers)
			if (w.provider.dimensionId == dim) {
				world = w
				break
			}
		
		if (world != null) {
			val tile = world.getTileEntity(coords.posX, coords.posY, coords.posZ)
			if (tile != null && tile is IManaPool)
				return tile
		}
		
		return null
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, from: TileEntity): Boolean {
		val pool = getManaPool(stack) ?: return false
		
		return if (pool !is DummyPool) !pool.isOutputtingPower && !pool.isFull else false
	}
	
	override fun canExportManaToPool(stack: ItemStack, to: TileEntity): Boolean {
		val pool = getManaPool(stack) ?: return false
		
		return if (pool !is DummyPool) pool.isOutputtingPower else false
	}
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack) = false
	
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack) = false
	
	override fun isNoExport(stack: ItemStack) = false
	
	override fun getBinding(stack: ItemStack): ChunkCoordinates? {
		val pool = getManaPool(stack)
		
		return if (pool == null || pool is DummyPool) null else getPoolCoords(stack)
	}
	
	override fun getManaFractionForDisplay(stack: ItemStack): Float {
		return getMana(stack).toFloat() / getMaxMana(stack).toFloat()
	}
}

private object DummyPool: IManaPool {
	
	override fun isFull(): Boolean {
		return false
	}
	
	override fun recieveMana(mana: Int) {}
	
	override fun canRecieveManaFromBursts(): Boolean {
		return false
	}
	
	override fun getCurrentMana(): Int {
		return 0
	}
	
	override fun isOutputtingPower(): Boolean {
		return false
	}
}