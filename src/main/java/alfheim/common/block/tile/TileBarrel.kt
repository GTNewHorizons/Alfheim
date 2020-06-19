package alfheim.common.block.tile

import alexsocol.asjlib.extendables.ASJTile
import alfheim.common.item.material.ElvenFoodMetas
import net.minecraft.nbt.NBTTagCompound

class TileBarrel: ASJTile() {
	
	var closed = true
	var vineLevel = 0
	var vineStage = 0
	var vineType = VINE_TYPE_NONE
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		closed = nbt.getBoolean(TAG_CLOSED)
		vineLevel = nbt.getInteger(TAG_VINE_LEVEL)
		vineStage = nbt.getInteger(TAG_VINE_STAGE)
		vineType = nbt.getInteger(TAG_VINE_TYPE)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setBoolean(TAG_CLOSED, closed)
		nbt.setInteger(TAG_VINE_LEVEL, vineLevel)
		nbt.setInteger(TAG_VINE_STAGE, vineStage)
		nbt.setInteger(TAG_VINE_TYPE, vineType)
	}
	
	companion object {
		
		val VINE_STAGE_GRAPE = 1
		val VINE_STAGE_MASH = 2
		val VINE_STAGE_LIQUID = 3
		val VINE_STAGE_READY = 4
		
		val VINE_TYPE_NONE = -1
		val VINE_TYPE_WHITE = ElvenFoodMetas.GreenGrapes
		val VINE_TYPE_RED = ElvenFoodMetas.RedGrapes
		
		const val MAX_VINE_LEVEL = 12
		
		const val TAG_CLOSED = "closed"
		const val TAG_VINE_LEVEL = "vine_level"
		const val TAG_VINE_STAGE = "vine_stage"
		const val TAG_VINE_TYPE = "vine_type"
	}
}