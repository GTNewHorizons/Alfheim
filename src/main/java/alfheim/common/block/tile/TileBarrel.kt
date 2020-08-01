package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJTile
import alfheim.common.item.material.ElvenFoodMetas
import net.minecraft.nbt.NBTTagCompound

class TileBarrel: ASJTile() {
	
	var closed = true
	var stomps = 0
	var timer = 0
	var wineLevel = 0
	var wineStage = 0
	var wineType = WINE_TYPE_NONE
	
	override fun updateEntity() {
		if (timer > 0) {
			if (closed) {
				if (--timer == 0) {
					wineStage = WINE_STAGE_READY
					
					if (worldObj.isRemote)
						ASJUtilities.dispatchTEToNearbyPlayers(this)
				}
			} else {
				if (++timer >= MAX_OPEN_TIME)
					reset()
			}
		}
	}
	
	fun reset() {
		stomps = 0
		timer = 0
		wineLevel = 0
		wineStage = 0
		wineType = WINE_TYPE_NONE
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		closed = nbt.getBoolean(TAG_CLOSED)
		stomps = nbt.getInteger(TAG_STOMPS)
		timer = nbt.getInteger(TAG_TIMER)
		wineLevel = nbt.getInteger(TAG_WINE_LEVEL)
		wineStage = nbt.getInteger(TAG_WINE_STAGE)
		wineType = nbt.getInteger(TAG_WINE_TYPE)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setBoolean(TAG_CLOSED, closed)
		nbt.setInteger(TAG_STOMPS, stomps)
		nbt.setInteger(TAG_TIMER, timer)
		nbt.setInteger(TAG_WINE_LEVEL, wineLevel)
		nbt.setInteger(TAG_WINE_STAGE, wineStage)
		nbt.setInteger(TAG_WINE_TYPE, wineType)
	}
	
	override fun getRenderBoundingBox() = boundingBox(1)
	
	companion object {
		
		const val WINE_STAGE_GRAPE = 1
		const val WINE_STAGE_MASH = 2
		const val WINE_STAGE_LIQUID = 3
		const val WINE_STAGE_READY = 4
		
		val WINE_TYPE_NONE = 0
		val WINE_TYPE_WHITE = ElvenFoodMetas.WhiteGrapes
		val WINE_TYPE_RED = ElvenFoodMetas.RedGrapes
		
		const val MAX_WINE_LEVEL = 12
		const val FERMENTATION_TIME = 6000
		const val MAX_OPEN_TIME = FERMENTATION_TIME + 666
		
		const val TAG_CLOSED = "closed"
		const val TAG_STOMPS = "stomps"
		const val TAG_TIMER = "timer"
		const val TAG_WINE_LEVEL = "wine_level"
		const val TAG_WINE_STAGE = "wine_stage"
		const val TAG_WINE_TYPE = "wine_type"
	}
}