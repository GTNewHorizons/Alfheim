package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import net.minecraft.block.material.Material

class BlockShrineRock: BlockModMeta(Material.rock, 15) {
	
	init {
		setBlockName("ShrineRock")
		setCreativeTab(AlfheimCore.alfheimTab)
		setHardness(10f)
		setHarvestLevel("pickaxe", 2)
		setResistance(10000f)
		setStepSound(soundTypeStone)
	}
}
