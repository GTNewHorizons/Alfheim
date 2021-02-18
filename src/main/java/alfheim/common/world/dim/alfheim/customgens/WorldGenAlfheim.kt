package alfheim.common.world.dim.alfheim.customgens

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.api.block.tile.SubTileAnomalyBase.EnumAnomalityRarity
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.ragnarok.RagnarokHandler
import alfheim.common.world.dim.alfheim.structure.StructureSpawnpoint
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

object WorldGenAlfheim: IWorldGenerator {
	
	val common = ArrayList<String>()
	
	val epic = ArrayList<String>()
	val rare = ArrayList<String>()
	val handWinter = SchemaUtils.loadStructure("${ModInfo.MODID}/schemas/WinterHand")
	val handSummer = SchemaUtils.loadStructure("${ModInfo.MODID}/schemas/SummerHand")
	
	init {
		for (s in AlfheimAPI.anomalies.keys)
			when (AlfheimAPI.anomalyInstances[s]!!.rarity) {
				EnumAnomalityRarity.COMMON -> common.add(s)
				EnumAnomalityRarity.EPIC   -> epic.add(s)
				EnumAnomalityRarity.RARE   -> rare.add(s)
			}
	}
	
	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		if (world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim)
			generateAlfheim(world, chunkX, chunkZ, rand)
	}
	
	fun generateAlfheim(world: World, chunkX: Int, chunkZ: Int, rand: Random) {
		if (chunkX == 0 && chunkZ == 0 && !world.isRemote) StructureSpawnpoint.generate(world, rand)
		
		if (world.rand.nextInt(6000) == 0) generateHand(world, chunkX, chunkZ, rand)
		
		if (AlfheimConfigHandler.anomaliesDispersion <= 0) return
		
		if (rand.nextInt(AlfheimConfigHandler.anomaliesDispersion) == 0) {
			val chance = rand.nextInt(32) + 1
			when {
				chance == 32 -> genRandomAnomalyOfRarity(world, chunkX, chunkZ, rand, EnumAnomalityRarity.EPIC)
				chance >= 24 -> genRandomAnomalyOfRarity(world, chunkX, chunkZ, rand, EnumAnomalityRarity.RARE)
				chance >= 16 -> genRandomAnomalyOfRarity(world, chunkX, chunkZ, rand, EnumAnomalityRarity.COMMON)
			}
		}
	}
	
	fun generateHand(world: World, chunkX: Int, chunkZ: Int, rand: Random) {
		if (!AlfheimCore.ENABLE_RAGNAROK) return
		
		val x = chunkX * 16 + rand.nextInt(16) + 8
		val z = chunkZ * 16 + rand.nextInt(16) + 8
		val y = world.getTopSolidOrLiquidBlock(x, z)
		
		ASJUtilities.chatLog("$x $y $z")
		
		SchemaUtils.generate(world, x, y + 1, z, if (rand.nextBoolean()) handWinter else handSummer)
		
		RagnarokHandler.handsSet.add(Vector3(x, y, z))
	}
	
	fun genRandomAnomalyOfRarity(world: World, chunkX: Int, chunkZ: Int, rand: Random, rarity: EnumAnomalityRarity) {
		val type = when (rarity) {
			EnumAnomalityRarity.COMMON -> common[rand.nextInt(common.size)]
			EnumAnomalityRarity.EPIC   -> epic[rand.nextInt(epic.size)]
			EnumAnomalityRarity.RARE   -> rare[rand.nextInt(rare.size)]
		}
		
		setAnomality(world, chunkX, chunkZ, rand, type)
	}
	
	fun setAnomality(world: World, chunkX: Int, chunkZ: Int, rand: Random, type: String) {
		val x = chunkX * 16 + rand.nextInt(16) + 8
		val z = chunkZ * 16 + rand.nextInt(16) + 8
		val y = world.getTopSolidOrLiquidBlock(x, z) + 1
		
		world.setBlock(x, y, z, AlfheimBlocks.anomaly)
		val te = world.getTileEntity(x, y, z)
		if (te is TileAnomaly) {
			te.lock(x, y, z, world.provider.dimensionId)
			
			val sub = SubTileAnomalyBase.forName(type) ?: return
			sub.worldGen = true
			
			te.addSubTile(sub, type)
			
			for (i in 0 until AlfheimConfigHandler.anomaliesUpdate) te.updateEntity()
			
			sub.worldGen = false
		}
	}
}