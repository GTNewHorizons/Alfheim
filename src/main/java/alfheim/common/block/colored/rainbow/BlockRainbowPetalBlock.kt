package alfheim.common.block.colored.rainbow

import alfheim.common.block.base.BlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

class BlockRainbowPetalBlock: BlockMod(Material.plants), ILexiconable {
	
	init {
		setHardness(0.4f)
		setStepSound(soundTypeGrass)
		setBlockName("rainbowPetalBlock")
	}
	
	override fun isInterpolated() = true
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.rainbowFlora
}
