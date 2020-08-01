package alfheim.common.block.tile.sub.flower

import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.FakeLightning
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.IIcon
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.subtile.signature.PassiveFlower
import vazkii.botania.common.block.subtile.generating.SubTilePassiveGenerating

@PassiveFlower
class SubTileStormFlower: SubTilePassiveGenerating() {
	
	var cooldown = 0
	
	override fun canGeneratePassively(): Boolean {
		if (--cooldown > 0) return false
		
		for (i in supertile.worldObj.weatherEffects.indices) {
			val e = supertile.worldObj.weatherEffects[i] as? Entity ?: continue
			
			if ((e is EntityLightningBolt || e is FakeLightning) && !e.isDead && Vector3.entityTileDistance(e, supertile) < 2) {
				e.setDead()
				cooldown = 1200
				addMana(maxMana)
			}
		}
		
		return false
	}
	
	override fun getDelayBetweenPassiveGeneration(): Int {
		return 1
	}
	
	override fun getValueForPassiveGeneration(): Int {
		return 0
	}
	
	override fun getMaxMana(): Int {
		return if (isOnSpecialSoil) 6000 else 3000
	}
	
	override fun getColor(): Int {
		return 0x53DFDF
	}
	
	override fun writeToPacketNBT(nbt: NBTTagCompound) {
		super.writeToPacketNBT(nbt)
		nbt.setInteger(TAG_COOLDOWN, cooldown)
	}
	
	override fun readFromPacketNBT(nbt: NBTTagCompound) {
		super.readFromPacketNBT(nbt)
		cooldown = nbt.getInteger(TAG_COOLDOWN)
	}
	
	override fun getEntry(): LexiconEntry? {
		return null // TODO entry
	}
	
	override fun getIcon(): IIcon? = BotaniaAPI.getSignatureForName("stormFlower").getIconForStack(null)
	
	companion object {
		const val TAG_COOLDOWN = "cooldown"
	}
}