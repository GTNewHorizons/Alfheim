package alfheim.common.world.dim.alfheim.customgens

import alfheim.api.AlfheimAPI
import alfheim.api.block.tile.SubTileEntity
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.world.dim.alfheim.struct.StructureSpawnpoint
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider

import java.util.*

class WorldGenAlfheim: IWorldGenerator {
	
	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim)
			generateAlfheim(rand, chunkX, chunkZ, world)
	}
	
	companion object {
		
		val common = ArrayList<String>()
		val epic = ArrayList<String>()
		val rare = ArrayList<String>()
		
		init {
			for (s in AlfheimAPI.anomalies.keys)
				when (AlfheimAPI.anomalyInstances[s].rarity) {
					SubTileEntity.EnumAnomalityRarity.COMMON -> common.add(s)
					SubTileEntity.EnumAnomalityRarity.EPIC   -> epic.add(s)
					SubTileEntity.EnumAnomalityRarity.RARE   -> rare.add(s)
				}
		}
		
		private fun generateAlfheim(rand: Random, chunkX: Int, chunkZ: Int, world: World) {
			if (chunkX == 0 && chunkZ == 0 && !world.isRemote) {
				Thread(Runnable { StructureSpawnpoint.generate(world, rand) }, "Alf Spawn Gen").start()
			}
			
			if (AlfheimConfig.anomaliesDispersion <= 0) return
			
			if (rand.nextInt(AlfheimConfig.anomaliesDispersion) == 0) {
				val chance = rand.nextInt(32) + 1
				if (chance == 32)
					genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.EPIC)
				else if (chance >= 24)
					genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.RARE)
				else if (chance >= 16)
					genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.COMMON)
			}
		}
		
		private fun genRandomAnomalyOfRarity(rand: Random, chunkX: Int, chunkZ: Int, world: World, rarity: EnumAnomalityRarity) {
			var type = ""
			when (rarity) {
				SubTileEntity.EnumAnomalityRarity.COMMON -> type = common[rand.nextInt(common.size)]
				SubTileEntity.EnumAnomalityRarity.EPIC   -> type = epic[rand.nextInt(epic.size)]
				SubTileEntity.EnumAnomalityRarity.RARE   -> type = rare[rand.nextInt(rare.size)]
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
				
				for (i in 0 until AlfheimConfig.anomaliesUpdate) te.updateEntity()
				
				sub.worldGen = false
			}
		}
	}
}