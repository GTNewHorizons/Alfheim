package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.util.mc
import alfheim.common.core.helper.InterpolatedIconHelper
import alfheim.common.core.util.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
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
		
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun registerIcons(reg: IIconRegister) = Unit
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1)
			itemIcon = InterpolatedIconHelper.forName(event.map, "manaMirrorImba")
	}
	
	override fun getDamage(stack: ItemStack): Int {
		val mana = getMana(stack).F
		return 1000 - (mana / TilePool.MAX_MANA * 1000).I
	}
	
	override fun getDisplayDamage(stack: ItemStack) = getDamage(stack)
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slot: Int, inHand: Boolean) {
		if (world.isRemote)
			return
		
		val pool = getManaPool(stack)
		if (pool !== DummyPool) {
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
	
	override
	fun getMana(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_MANA, 0)
	fun setMana(stack: ItemStack, mana: Int) = ItemNBTHelper.setInt(stack, TAG_MANA, max(0, mana))
	fun getManaBacklog(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_MANA_BACKLOG, 0)
	fun setManaBacklog(stack: ItemStack, backlog: Int) = ItemNBTHelper.setInt(stack, TAG_MANA_BACKLOG, backlog)
	
	override fun getMaxMana(stack: ItemStack): Int {
		if (!ASJUtilities.isServer) return TilePool.MAX_MANA
		
		val pool = getManaPool(stack) ?: return 0
		
		return if (pool !== DummyPool) TilePool.MAX_MANA else 0
	}
	
	override fun addMana(stack: ItemStack, mana: Int) {
		setMana(stack, getMana(stack) + mana)
		setManaBacklog(stack, getManaBacklog(stack) + mana)
		
		// update pool
		
		val pool = getManaPool(stack)
		
		if (pool !== DummyPool) {
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
	
	fun getDimension(stack: ItemStack): Int = ItemNBTHelper.getInt(stack, TAG_DIM, 0)
	
	fun getManaPool(stack: ItemStack): IManaPool? {
		val server = MinecraftServer.getServer() ?: return DummyPool
		
		val coords = getPoolCoords(stack)
		if (coords.posY == -1)
			return null
		
		val dim = getDimension(stack)
		val world = server.worldServers.firstOrNull { it.provider.dimensionId == dim } ?: return null
		
		val tile = world.getTileEntity(coords.posX, coords.posY, coords.posZ)
		if (tile != null && tile is IManaPool)
			return tile
		
		return null
	}
	
	override fun canReceiveManaFromPool(stack: ItemStack, from: TileEntity): Boolean {
		val pool = getManaPool(stack) ?: return false
		
		return if (pool !== DummyPool) !pool.isOutputtingPower && !pool.isFull else false
	}
	
	override fun canExportManaToPool(stack: ItemStack, to: TileEntity): Boolean {
		val pool = getManaPool(stack) ?: return false
		
		return if (pool !== DummyPool) pool.isOutputtingPower else false
	}
	
	override fun canReceiveManaFromItem(stack: ItemStack, otherStack: ItemStack) = false
	override fun canExportManaToItem(stack: ItemStack, otherStack: ItemStack) = false
	override fun isNoExport(stack: ItemStack) = false
	
	override fun getBinding(stack: ItemStack): ChunkCoordinates? {
		val world = mc.theWorld ?: return null
		
		if (world.provider.dimensionId != ItemNBTHelper.getInt(stack, "dim", Int.MAX_VALUE)) return null
		
		val coords = getPoolCoords(stack)
		if (coords.posY == -1)
			return null
		
		val tile = world.getTileEntity(coords.posX, coords.posY, coords.posZ)
		
		return if (tile is IManaPool) coords else null
	}
	
	override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack).F / getMaxMana(stack).F
}

private object DummyPool: IManaPool {
	override fun isFull() = false
	override fun recieveMana(mana: Int) = Unit
	override fun canRecieveManaFromBursts() = false
	override fun getCurrentMana() = 0
	override fun isOutputtingPower() = false
}