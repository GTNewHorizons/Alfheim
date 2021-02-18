package alfheim.common.block.mana

import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileManaTuner
import net.minecraft.block.material.Material
import net.minecraft.world.World
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.IManaTrigger

class BlockManaTuner: BlockContainerMod(Material.iron), IManaTrigger {
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileManaTuner()
	
	override fun onBurstCollision(burst: IManaBurst, world: World, x: Int, y: Int, z: Int) {
		(world.getTileEntity(x, y, z) as TileManaTuner).onBurstCollision(burst)
	}
}
