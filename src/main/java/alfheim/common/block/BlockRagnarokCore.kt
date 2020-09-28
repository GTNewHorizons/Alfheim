package alfheim.common.block

import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileRagnarokCore
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class BlockRagnarokCore: BlockContainerMod(Material.rock) {
	
	init {
		setCreativeTab(null) // ???
		setBlockName("RagnarokCore")
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileRagnarokCore()
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val tile = world.getTileEntity(x, y, z) as? TileRagnarokCore ?: return false
		if (!tile.checkStructure(false)) return false
		tile.started = true
		return true
	}
}
