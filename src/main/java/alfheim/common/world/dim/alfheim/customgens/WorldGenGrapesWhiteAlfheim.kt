package alfheim.common.world.dim.alfheim.customgens

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.world.dim.alfheim.biome.BiomeRiver
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import ru.vamig.worldengine.*
import java.util.*

class WorldGenGrapesWhiteAlfheim(val perChunk: Int, val block: Block): IWorldGenerator {
	
	override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, prov: IChunkProvider?, chunkProvider: IChunkProvider?) {
		if (world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
		
		for (i in 0 until perChunk) {
			val x = chunkX * 16 + random.nextInt(16) + 8
			val z = chunkZ * 16 + random.nextInt(16) + 8
			
			if (prov is WE_ChunkProvider && WE_Biome.getBiomeAt(prov, x, z) === BiomeRiver) continue
			
			val y = world.getTopLiquidBlock(x, z)
			
			if (block.canBlockStay(world, x, y, z))
				if (world.isAirBlock(x, y, z)) {
					world.setBlock(x, y, z, block, 0, 3)
				}
		}
	}
	
	fun World.getTopLiquidBlock(x: Int, z: Int): Int {
		val chunk = getChunkFromBlockCoords(x, z)
		val i = x and 15
		var j = chunk.topFilledSegment + 15
		val k = z and 15
		while (j > 0) {
			val block = chunk.getBlock(i, j, k)
			if (block.material.isLiquid)
				return j + 1
			
			--j
		}
		return -1
	}
}