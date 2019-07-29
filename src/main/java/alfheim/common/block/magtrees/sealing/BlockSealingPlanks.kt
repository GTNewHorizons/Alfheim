package alfheim.common.block.magtrees.sealing

import alfheim.common.block.base.BlockMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent17
import alfheim.common.block.material.MaterialCustomSmeltingWood
import alfheim.common.lexicon.ShadowFoxLexiconData
import vazkii.botania.api.lexicon.ILexiconable

class BlockSealingPlanks: BlockMod(MaterialCustomSmeltingWood.instance), ILexiconable, ISoundSilencer {
	
	private val name = "sealingPlanks"
	
	init {
		blockHardness = 2F
		setLightLevel(0f)
		setStepSound(soundTypeCloth)
		
		setBlockName(name)
	}
	
	override fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = dist <= 8
	
	override fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17) = 0.5f
	
	override fun isInterpolated() = true
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun damageDropped(par1: Int) = par1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.silencer
}
