package alfheim.common.world.dim.alfheim

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.world.dim.alfheim.biome.*
import alfheim.common.world.dim.alfheim.customgens.WorldGenAlfheimThaumOre
import ru.vamig.worldengine.*
import ru.vamig.worldengine.standardcustomgen.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks

class WorldProviderAlfheim: WE_WorldProvider() {
	
	override fun getCloudHeight() = 180f
	override fun isSurfaceWorld() = true
	
	override fun calculateCelestialAngle(var1: Long, var3: Float): Float {
		val j = (var1 % 24000L).I
		var f1 = (j.F + var3) / 24000f - 0.25f
		
		if (f1 < 0f) {
			++f1
		}
		
		if (f1 > 1f) {
			--f1
		}
		
		val f2 = f1
		f1 = 1f - ((Math.cos(f1.D * Math.PI) + 1.0) / 2.0).F
		f1 = f2 + (f1 - f2) / 3f
		return f1
	}
	
	override fun canRespawnHere() = AlfheimConfigHandler.enableElvenStory xor AlfheimConfigHandler.enableAlfheimRespawn
	
	override fun getDimensionName() = "Alfheim"
	
	override fun genSettings(cp: WE_ChunkProvider) {
		cp.createChunkGen_List.clear()
		cp.createChunkGen_InXZ_List.clear()
		cp.createChunkGen_InXYZ_List.clear()
		cp.decorateChunkGen_List.clear()
		
		WE_Biome.setBiomeMap(cp, 1.2, 6, 8000.0, 0.4)
		
		val terrainGenerator = WE_TerrainGenerator()
		terrainGenerator.worldStoneBlock = ModBlocks.livingrock
		cp.createChunkGen_List.add(terrainGenerator)
		val cg = WE_CaveGen()
		cg.replaceBlocksList.clear()
		cg.replaceBlocksMetaList.clear()
		cg.addReplacingBlock(ModBlocks.livingrock, 0.toByte())
		cp.createChunkGen_List.add(cg)
		val rg = WE_RavineGen()
		rg.replaceBlocksList.clear()
		rg.replaceBlocksMetaList.clear()
		rg.addReplacingBlock(ModBlocks.livingrock, 0.toByte())
		cp.createChunkGen_List.add(rg)
		val snowGen = WE_SnowGen()
		snowGen.snowPoint = 164
		snowGen.randomSnowPoint = 8
		cp.createChunkGen_InXZ_List.add(snowGen)
		
		if (Botania.thaumcraftLoaded)
			cp.decorateChunkGen_List.add(WorldGenAlfheimThaumOre())
		
		val ores = WE_OreGen()
		for (i in 0 until AlfheimConfigHandler.oregenMultiplier) {
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 0, 1, 8, 1, 2, 75, 1, 16)
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 1, 1, 8, 3, 6, 100, 1, 64)
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 2, 4, 8, 1, 1, 100, 1, 48)
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 3, 1, 8, 2, 3, 100, 1, 32)
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 4, 1, 4, 1, 1, 50, 1, 16)
			ores.add(AlfheimBlocks.elvenOre, ModBlocks.livingrock, 5, 4, 8, 1, 1, 100, 1, 48)
		}
		
		cp.decorateChunkGen_List.add(ores)
		
		//cp.decorateChunkGen_List.add(new StructureSpawnpoint()); WE generator is stupid
		val waterLakes = WE_LakeGen()
		waterLakes.fY = 192
		cp.decorateChunkGen_List.add(waterLakes)
		
		WE_Biome.addBiomeToGeneration(cp, BiomeField)
		WE_Biome.addBiomeToGeneration(cp, BiomeBeach)
		WE_Biome.addBiomeToGeneration(cp, BiomeSandbank)
		WE_Biome.addBiomeToGeneration(cp, BiomeRiver)
		WE_Biome.addBiomeToGeneration(cp, BiomeMount1)
		WE_Biome.addBiomeToGeneration(cp, BiomeMount2)
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3)
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3Trees)
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3Field)
		WE_Biome.addBiomeToGeneration(cp, BiomeForest)
		WE_Biome.addBiomeToGeneration(cp, BiomeForest2)
	}
}
