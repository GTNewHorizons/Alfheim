package alfheim.common.block.tile

import alexsocol.asjlib.extendables.ASJTile
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB

class TileRaceSelector: ASJTile() {
	
	fun giveRaceAndReset(player: EntityPlayer): Boolean {
		if (!AlfheimCore.enableElvenStory) return false
		if (EnumRace.getRace(player) != EnumRace.HUMAN) return false
		
		val race = EnumRace.values()[rotation+1]
		EnumRace.selectRace(player, race)
		
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
		
		rotation = 0
		activeRotation = 0
		female = false
		
		return true
	}
	
	var activeRotation = 0
	var female = false
	var rotation = 0
		set(value) {
			//val lower = value > field
			
			field = when {
				value < 0 -> 8
				value > 8 -> 0
				else      -> value
			}
			
			//activeRotation = 20 * if (lower) -1 else 1
		}
	
	override fun updateEntity() {
		if (activeRotation != 0) if (activeRotation > 0) --activeRotation else ++activeRotation
	}
	
	override fun getRenderBoundingBox() = AxisAlignedBB.getBoundingBox(xCoord -3.0, yCoord.toDouble(), zCoord -6.0, xCoord + 4.0, yCoord + 2.0, zCoord + 1.0)!!
	
	val TAG_GENDER = "gender"
	val TAG_ROTATION = "rotation"
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setBoolean(TAG_GENDER, female)
		nbt.setInteger(TAG_ROTATION, rotation)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		female = nbt.getBoolean(TAG_GENDER)
		rotation = nbt.getInteger(TAG_ROTATION)
	}
}
