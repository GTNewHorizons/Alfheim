package alfheim.common.world.dim.alfheim

import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.world.dim.alfheim.biome.*
import alfheim.common.world.dim.alfheim.customgens.WorldGenAlfheimThaumOre
import ru.vamig.worldengine.*
import ru.vamig.worldengine.standardcustomgen.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks

class WorldProviderAlfheim: WE_WorldProvider() {
	
	override fun getCloudHeight(): Float {
		return 128.0f
	}
	
	override fun isSurfaceWorld(): Boolean {
		return true
	}
	
	override fun calculateCelestialAngle(var1: Long, var3: Float): Float {
		val j = (var1 % 24000L).toInt()
		var f1 = (j.toFloat() + var3) / 24000.0f - 0.25f
		
		if (f1 < 0.0f) {
			++f1
		}
		
		if (f1 > 1.0f) {
			--f1
		}
		
		val f2 = f1
		f1 = 1.0f - ((Math.cos(f1.toDouble() * Math.PI) + 1.0) / 2.0).toFloat()
		f1 = f2 + (f1 - f2) / 3.0f
		return f1
	}
	
	override fun canRespawnHere(): Boolean {
		return AlfheimCore.enableElvenStory || AlfheimConfig.enableAlfheimRespawn
	}
	
	override fun getDimensionName(): String {
		return "Alfheim"
	}
	
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
		for (i in 0 until AlfheimConfig.oregenMultiplier) {
			ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 0, 1, 8, 1, 2, 75, 1, 16)
			ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 1, 1, 8, 3, 6, 100, 1, 64)
			ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 2, 4, 8, 1, 1, 100, 1, 48)
			ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 3, 1, 8, 2, 3, 100, 1, 32)
			ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 4, 1, 4, 1, 1, 50, 1, 16)
		}
		
		cp.decorateChunkGen_List.add(ores)
		
		//cp.decorateChunkGen_List.add(new StructureSpawnpoint()); WE generator is stupid
		val waterLakes = WE_LakeGen()
		waterLakes.fY = 192
		cp.decorateChunkGen_List.add(waterLakes)
		
		WE_Biome.addBiomeToGeneration(cp, BiomeField())
		WE_Biome.addBiomeToGeneration(cp, BiomeBeach())
		WE_Biome.addBiomeToGeneration(cp, BiomeSandbank())
		WE_Biome.addBiomeToGeneration(cp, BiomeLake())
		WE_Biome.addBiomeToGeneration(cp, BiomeMount1())
		WE_Biome.addBiomeToGeneration(cp, BiomeMount2())
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3())
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3Trees())
		WE_Biome.addBiomeToGeneration(cp, BiomeMount3Field())
		WE_Biome.addBiomeToGeneration(cp, BiomeForest())
		WE_Biome.addBiomeToGeneration(cp, BiomeForest2())
	}
}
