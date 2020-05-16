package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJTile
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.ElvenSkinSystem
import alfheim.common.network.MessageSkinInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*

class TileRaceSelector: ASJTile() {
	
	fun giveRaceAndReset(player: EntityPlayer): Boolean {
		if (!AlfheimConfigHandler.enableElvenStory) return false
		if (!ModInfo.DEV && player.race != EnumRace.HUMAN) return false
		
		val race = EnumRace[rotation+1]
		selectRace(player, race)
		
		if (ASJUtilities.isServer) {
			ElvenSkinSystem.setGender(player, female)
			ElvenSkinSystem.setCustomSkin(player, custom)
			
			AlfheimCore.network.sendToAll(MessageSkinInfo(player.commandSenderName, female, custom))
		}
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
		
		female = false
		custom = false
		activeRotation = 0
		rotation = 0
		
		return true
	}
	
	fun selectRace(player: EntityPlayer, race: EnumRace) {
		EnumRace[player] = race
		player.capabilities.allowFlying = true
		player.sendPlayerAbilities()
		
		val (x, y, z) = AlfheimConfigHandler.zones[race.ordinal - 1]
		player.setSpawnChunk(ChunkCoordinates(x.mfloor(), y.mfloor(), z.mfloor()), true, AlfheimConfigHandler.dimensionIDAlfheim)
		ASJUtilities.sendToDimensionWithoutPortal(player, AlfheimConfigHandler.dimensionIDAlfheim, x, y, z)
	}
	
	var timer = 0
	
	var female = false
	var custom = false
	var activeRotation = 0
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
		if (--timer == 0) {
			female = false
			custom = false
			activeRotation = 0
			rotation = 0
			
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
		}
		
		
		// remove when there will be genders
		// if (getBlockMetadata() != 1) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3)
	}
	
	override fun getRenderBoundingBox() = AxisAlignedBB.getBoundingBox(xCoord -3.0, yCoord.D, zCoord -6.0, xCoord + 4.0, yCoord + 2.0, zCoord + 1.0)!!
	
	val TAG_TIMER = "timer"
	val TAG_GENDER = "gender"
	val TAG_ROTATION = "rotation"
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setInteger(TAG_TIMER, timer)
		nbt.setBoolean(TAG_GENDER, female)
		nbt.setInteger(TAG_ROTATION, rotation)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		timer = nbt.getInteger(TAG_TIMER)
		female = nbt.getBoolean(TAG_GENDER)
		rotation = nbt.getInteger(TAG_ROTATION)
	}
}
