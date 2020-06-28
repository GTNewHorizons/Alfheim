package alfheim.common.block.tile.sub.flower

import net.minecraft.world.World
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.block.subtile.generating.SubTilePassiveGenerating

abstract class SubTileWeatherFlower: SubTilePassiveGenerating() {
	
	override fun getDelayBetweenPassiveGeneration(): Int {
		return if (isOnSpecialSoil) 1 else 2
	}
	
	override fun getValueForPassiveGeneration(): Int {
		return if (isOnSpecialSoil) 2 else 1
	}
	
	override fun getMaxMana(): Int {
		return if (isOnSpecialSoil) 400 else 200
	}
	
	override fun getColor(): Int {
		return 0x9CFFFF
	}
	
	override fun canGeneratePassively(): Boolean {
		return canGenerate(supertile.worldObj, supertile.xCoord, supertile.yCoord, supertile.zCoord)
	}
	
	abstract fun canGenerate(world: World, x: Int, y: Int, z: Int): Boolean
	
	abstract override fun getEntry(): LexiconEntry?
}

class SubTileRainFlower: SubTileWeatherFlower() {
	
	override fun canGenerate(world: World, x: Int, y: Int, z: Int): Boolean {
		return world.canLightningStrikeAt(x, y, z)
	}
	
	override fun getEntry(): LexiconEntry? {
		return null // FIXME entry
	}
}

class SubTileSnowFlower: SubTileWeatherFlower() {
	
	override fun canGenerate(world: World, x: Int, y: Int, z: Int): Boolean {
		return world.isRaining && world.func_147478_e(x, y, z, false) && world.canBlockSeeTheSky(x, y, z)
	}
	
	override fun getEntry(): LexiconEntry? {
		return null // FIXME entry
	}
}