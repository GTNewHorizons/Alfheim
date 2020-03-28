package alfheim.common.block.magtrees.sealing

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent17
import java.util.*

class BlockSealingLeaves: BlockLeavesMod(), ISoundSilencer {
	
	init {
		setBlockName("sealingLeaves")
		setStepSound(Block.soundTypeCloth)
	}
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = dist <= 8
	
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = 0.5f
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.sealingSapling.toItem()
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("sealingLeaves")
	
	override fun decayBit() = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.silencer
}
