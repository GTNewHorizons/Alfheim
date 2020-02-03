package alfheim.common.world.dim.alfheim.customgens

import alfheim.api.AlfheimAPI
import alfheim.api.block.tile.SubTileEntity
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.world.dim.alfheim.structure.StructureSpawnpoint
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

class WorldGenAlfheim: IWorldGenerator {
	
	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		if (world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim)
			generateAlfheim(rand, chunkX, chunkZ, world)
	}
	
	companion object {
		
		val common = ArrayList<String>()
		val epic = ArrayList<String>()
		val rare = ArrayList<String>()
		
		init {
			for (s in AlfheimAPI.anomalies.keys)
				when (AlfheimAPI.anomalyInstances[s]!!.rarity) {
					EnumAnomalityRarity.COMMON -> common.add(s)
					EnumAnomalityRarity.EPIC   -> epic.add(s)
					EnumAnomalityRarity.RARE   -> rare.add(s)
				}
		}
		
		private fun generateAlfheim(rand: Random, chunkX: Int, chunkZ: Int, world: World) {
			if (chunkX == 0 && chunkZ == 0 && !world.isRemote) {
				Thread(Runnable { StructureSpawnpoint.generate(world) }, "Alf Spawn Gen").start()
			}
			
			if (AlfheimConfigHandler.anomaliesDispersion <= 0) return
			
			if (rand.nextInt(AlfheimConfigHandler.anomaliesDispersion) == 0) {
				val chance = rand.nextInt(32) + 1
				when {
					chance == 32 -> genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.EPIC)
					chance >= 24 -> genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.RARE)
					chance >= 16 -> genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.COMMON)
				}
			}
		}
		
		private fun genRandomAnomalyOfRarity(rand: Random, chunkX: Int, chunkZ: Int, world: World, rarity: EnumAnomalityRarity) {
			val type = when (rarity) {
				EnumAnomalityRarity.COMMON -> common[rand.nextInt(common.size)]
				EnumAnomalityRarity.EPIC   -> epic[rand.nextInt(epic.size)]
				EnumAnomalityRarity.RARE   -> rare[rand.nextInt(rare.size)]
			}
			
			setAnomality(rand, chunkX, chunkZ, world, type)
		}
		
		private fun setAnomality(rand: Random, chunkX: Int, chunkZ: Int, world: World, type: String) {
			val x = chunkX * 16 + rand.nextInt(16) + 8
			val z = chunkZ * 16 + rand.nextInt(16) + 8
			val y = world.getTopSolidOrLiquidBlock(x, z) + 1
			
			world.setBlock(x, y, z, AlfheimBlocks.anomaly)
			val te = world.getTileEntity(x, y, z)
			if (te is TileAnomaly) {
				val sub = SubTileEntity.forName(type)
				sub!!.worldGen = true
				
				te.addSubTile(sub, type)
				
				for (i in 0 until AlfheimConfigHandler.anomaliesUpdate) te.updateEntity()
				
				sub.worldGen = false
			}
		}
	}
}