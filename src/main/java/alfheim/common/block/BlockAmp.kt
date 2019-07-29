package alfheim.common.block

import alfheim.common.block.base.BlockMod
import alfheim.common.block.magtrees.sealing.ISoundSilencer
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent17
import alfheim.common.lexicon.ShadowFoxLexiconData
import vazkii.botania.api.lexicon.ILexiconable

class BlockAmp: BlockMod(Material.wood), ISoundSilencer, ILexiconable {
	
	init {
		setBlockName("amplifier")
		setStepSound(soundTypeCloth)
	}
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17): Boolean = dist <= 2
	
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17): Float = 5f
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = ShadowFoxLexiconData.amp
}
