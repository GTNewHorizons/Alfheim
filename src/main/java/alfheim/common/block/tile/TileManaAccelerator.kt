package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.TileItemContainer
import alfheim.AlfheimCore
import alfheim.common.network.MessageTileItem
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.client.core.handler.LightningHandler
import vazkii.botania.common.block.tile.mana.*
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.lib.LibMisc
import kotlin.math.min

class TileManaAccelerator: TileItemContainer() {
	
	// for some reason it updates as many times per tick as many times you right-click on it
	private var lastTick: Long = 0
	
	//private var dummy: DummyUpdater? = null
	
	override fun updateEntity() {
		//if (dummy == null) dummy = DummyUpdater(worldObj)
		
		val tick = worldObj.totalWorldTime
		if (lastTick == tick) return
		
		val te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)
		if (te !is TilePool) {
			if (worldObj.isRemote) return
			worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, ItemStack(worldObj.getBlock(xCoord, yCoord, zCoord), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord))))
			if (item != null) worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, item!!.copy()))
			item = null
			worldObj.setBlockToAir(xCoord, yCoord, zCoord)
			return
		}
		
		val stack = item ?: return
		
		if (stack.item is IManaItem) {
			val manaItem = stack.item as IManaItem
			
			if (te.isOutputtingPower && manaItem.canReceiveManaFromPool(stack, te) || !te.isOutputtingPower && manaItem.canExportManaToPool(stack, te)) {
				var didSomething = false
				
				var bellowCount = 0
				if (te.isOutputtingPower)
					for (dir in LibMisc.CARDINAL_DIRECTIONS) {
						val tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord - 1, zCoord + dir.offsetZ)
						if (tile is TileBellows && tile.linkedTile === te)
							bellowCount++
					}
				val transfRate = 1000 * (bellowCount + 2)
				
				if (te.isOutputtingPower) {
					if (te.canSpare) {
						if (te.currentMana > 0 && manaItem.getMana(stack) < manaItem.getMaxMana(stack))
							didSomething = true
						
						val manaVal = min(transfRate, min(te.currentMana, manaItem.getMaxMana(stack) - manaItem.getMana(stack)))
						if (!worldObj.isRemote)
							manaItem.addMana(stack, manaVal)
						te.recieveMana(-manaVal)
					}
				} else {
					if (te.canAccept) {
						if (manaItem.getMana(stack) > 0 && !te.isFull)
							didSomething = true
						
						val manaVal = min(transfRate, min(te.manaCap - te.currentMana, manaItem.getMana(stack)))
						if (!worldObj.isRemote)
							manaItem.addMana(stack, -manaVal)
						te.recieveMana(manaVal)
					}
				}
				
				if (didSomething) {
					if (worldObj.isRemote && ConfigHandler.chargingAnimationEnabled && worldObj.rand.nextInt(100) == 0) {
						val itemVec = Vector3.fromTileEntity(te).add(0.5, 0.5 + Math.random() * 0.3, 0.5)
						val tileVec = Vector3.fromTileEntity(te).add(0.2 + Math.random() * 0.6, 0.0, 0.2 + Math.random() * 0.6)
						LightningHandler.spawnLightningBolt(worldObj, if (te.isOutputtingPower) tileVec else itemVec, if (te.isOutputtingPower) itemVec else tileVec, 80f, worldObj.rand.nextLong(), 0x4400799c, 0x4400C6FF)
					}
					te.isDoingTransfer = te.isOutputtingPower
					if (worldObj.totalWorldTime % 20 == 0L) {
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType())
						if (ASJUtilities.isServer) AlfheimCore.network.sendToDimension(MessageTileItem(xCoord, yCoord, zCoord, stack), worldObj.provider.dimensionId)
					}
					
					//stack.item.onUpdate(stack, worldObj, dummy, -1, false)
				}
			}
		}
		
		lastTick = worldObj.totalWorldTime
	}
}

/*private class DummyUpdater(world: World): Entity(world) {
	override fun writeEntityToNBT(nbt: NBTTagCompound?)  = Unit
	override fun readEntityFromNBT(nbt: NBTTagCompound?) = Unit
	override fun entityInit() = Unit
}*/