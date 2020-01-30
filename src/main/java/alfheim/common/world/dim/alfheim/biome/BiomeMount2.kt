package alfheim.common.world.dim.alfheim.biome

import alfheim.AlfheimCore
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass
import alfheim.common.world.dim.alfheim.structure.StructureDreamsTree
import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.*
import vazkii.botania.common.block.ModBlocks

object BiomeMount2: BiomeAlfheim() {
	
	init {
		setBiomeName("Mid plateau")
		
		BiomeDictionary.registerBiomeType(this, Type.MOUNTAIN, Type.FOREST, Type.PLAINS)
		
		biomeMinValueOnMap = 0.3
		biomeMaxValueOnMap = 0.75
		biomePersistence = 1.8
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 1.6
		biomeSurfaceHeight = 120
		biomeInterpolateQuality = 1
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.dirt, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -256, -2, true)
		standardBiomeLayers.add(if (AlfheimCore.winter && AlfheimConfigHandler.winterGrassReadyGen) AlfheimBlocks.snowGrass else Blocks.grass, 0.toByte(), Blocks.dirt, 0.toByte(), -256, 0, -256, 0, false)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		val waterLakes = WE_LakeGen()
		waterLakes.chunksForLake = 2
		waterLakes.minY = 100
		decorateChunkGen_List.add(waterLakes)
		val b = WE_WorldTreeGen()
		b.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, Blocks.vine, Blocks.cocoa, 32, 3, 1, 4, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 1, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, Blocks.vine, Blocks.cocoa, 32, 3, 1, 4, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 2, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, Blocks.vine, Blocks.cocoa, 32, 3, 1, 4, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 1, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, Blocks.vine, Blocks.cocoa, 32, 3, 1, 4, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 2, 12, 4, 0.618, 0.381, 1.0, 1.0)
		decorateChunkGen_List.add(b)
		val t = WE_StructureGen()
		t.add(StructureDreamsTree(Blocks.log, Blocks.leaves, 0, 4, 8, 0), 12)
		t.add(StructureDreamsTree(AlfheimBlocks.dreamLog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0), 20)
		decorateChunkGen_List.add(t)
		val g = WorldGenGrass(true, true, true, true, 1.2)
		decorateChunkGen_List.add(g)
	}
}