package alfheim.common.world.dim.alfheim.customgens

import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

class WorldGenReedAlfheim(val perChunk: Int): IWorldGenerator {
	
	override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider?, chunkProvider: IChunkProvider?) {
		if (world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
		
		for (i in 0 until perChunk) {
			val x = chunkX * 16 + random.nextInt(16) + 8
			val z = chunkZ * 16 + random.nextInt(16) + 8
			val y = world.getTopSolidOrLiquidBlock(x, z)
			
			if (Blocks.reeds.canBlockStay(world, x, y, z)) {
				val height = random.nextInt(4) + 2
				for (h in 0 until height) {
					if (world.isAirBlock(x, y + h, z))
						world.setBlock(x, y + h, z, Blocks.reeds)
					else
						break
				}
			}
		}
	}
}