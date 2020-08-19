package alfheim.common.block.corporea

import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.corporea.TileCorporeaInjector
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.material.Material
import net.minecraft.world.World

class BlockCorporeaInjector: BlockContainerMod(Material.iron) {
	
	init {
		setBlockName("CorporeaInjector")
		setCreativeTab(AlfheimTab)
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileCorporeaInjector()
}
