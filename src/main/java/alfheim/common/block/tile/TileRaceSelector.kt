package alfheim.common.block.tile

import alexsocol.asjlib.extendables.ASJTile
import alfheim.api.entity.EnumRace
import alfheim.common.core.command.CommandRace
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

class TileRaceSelector: ASJTile() {
	
	fun giveRaceAndReset(player: EntityPlayer) {
		CommandRace().processCommand(player, arrayOf("${EnumRace.values()[rotation + 1]}"))
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
		
		rotation = 0
		activeRotation = 0
		female = false
	}
	
	var activeRotation = 0
	var female = false
	var rotation = 0
		set(value) {
			val lower = value > field
			
			field = when {
				value < 0 -> 8
				value > 8 -> 0
				else      -> value
			}
			
			activeRotation = 20 * if (lower) -1 else 1
		}
	
	override fun updateEntity() {
		if (activeRotation != 0) if (activeRotation > 0) --activeRotation else ++activeRotation
	}
	
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
		activeRotation = 0
	}
}
