package alfheim.common.block.magtrees.sealing

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent17

class BlockSealingWoodSlab(full: Boolean, source: Block = ShadowFoxBlocks.sealingPlanks): BlockRainbowWoodSlab(full, source), ISoundSilencer {
	
	init {
		setStepSound(Block.soundTypeCloth)
	}
	
	override fun getFullBlock() = ShadowFoxBlocks.sealingSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = ShadowFoxBlocks.sealingSlabs as BlockSlab
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = dist <= 8
 
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = 0.5f
}

class BlockSealingWoodStairs(source: Block = ShadowFoxBlocks.sealingPlanks): BlockRainbowWoodStairs(source), ISoundSilencer {
	
	init {
		setStepSound(Block.soundTypeCloth)
	}
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = dist <= 8
 
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = 0.5f
}
