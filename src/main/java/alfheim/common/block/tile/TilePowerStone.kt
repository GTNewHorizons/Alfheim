package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import vazkii.botania.common.block.tile.TileMod
import kotlin.math.max

class TilePowerStone: TileMod() {
	
	val TAG_COOLDOWN = "cooldown"
	var cooldown: Int = 0
	
	override fun updateEntity() {
		cooldown = max(0, --cooldown)
	}
	
	fun onBlockActivated(player: EntityPlayer): Boolean {
		if (cooldown <= 0 && SpellBase.consumeMana(player, 10000, false) && press(player)) {
			SpellBase.consumeMana(player, 10000, true)
			cooldown = 6000
			ASJUtilities.dispatchTEToNearbyPlayers(this)
			return true
		}
		
		return false
	}
	
	fun press(player: EntityPlayer): Boolean {
		return when (getBlockMetadata()) {
			1    -> makePlayerBerserk(player)
			2    -> makePlayerTank(player)
			3    -> makePlayerNinja(player)
			4    -> makePlayerOvermage(player)
			
			else -> {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord)
				false
			}
		}
	}
	
	// +20% DMG, -20% HP
	fun makePlayerBerserk(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBerserk, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Spell DMG, +20% Spell Cost
	fun makePlayerOvermage(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDBerserk) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDOvermage, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Resistance, -20% Speed
	fun makePlayerTank(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDBerserk) && !player.isPotionActive(AlfheimConfigHandler.potionIDNinja)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDTank, 72000, 0))
			return true
		}
		return false
	}
	
	// +20% Speed, -20% DMG
	fun makePlayerNinja(player: EntityPlayer): Boolean {
		if (!player.isPotionActive(AlfheimConfigHandler.potionIDOvermage) && !player.isPotionActive(AlfheimConfigHandler.potionIDTank) && !player.isPotionActive(AlfheimConfigHandler.potionIDBerserk)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDNinja, 72000, 0))
			return true
		}
		return false
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		
		nbt.setInteger(TAG_COOLDOWN, cooldown)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		
		cooldown = nbt.getInteger(TAG_COOLDOWN)
	}
}
