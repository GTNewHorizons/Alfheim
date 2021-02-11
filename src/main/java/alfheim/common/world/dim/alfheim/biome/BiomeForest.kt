package alfheim.common.world.dim.alfheim.biome

import alfheim.AlfheimCore
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.world.dim.alfheim.customgens.*
import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.*
import vazkii.botania.common.block.ModBlocks

object BiomeForest: BiomeAlfheim() {
	
	init {
		setBiomeName("Forest")
		
		BiomeDictionary.registerBiomeType(this, Type.FOREST, Type.DENSE, Type.LUSH)
		
		biomeMinValueOnMap = -1.0
		biomeMaxValueOnMap = 0.82
		biomePersistence = 1.8
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 1.0
		biomeSurfaceHeight = 75 + offset
		biomeInterpolateQuality = 4
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.dirt, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -4, -2, true)
		standardBiomeLayers.add(if (AlfheimCore.winter && AlfheimConfigHandler.winterGrassReadyGen) AlfheimBlocks.snowGrass else Blocks.grass, 0.toByte(), Blocks.dirt, 0.toByte(), -256, 0, -256, 0, false)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		val b = WE_WorldTreeGen()
		b.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, AlfheimBlocks.grapesRed[0], null, 2, 1, 0, 6, true, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 1, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, null, null, 2, 1, 1, 4, false, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 1, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, null, null, 2, 1, 2, 4, false, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 2, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, null, null, 2, 1, 1, 4, false, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 1, 12, 4, 0.618, 0.381, 1.0, 1.0)
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, null, null, 2, 1, 2, 4, false, false, 2.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), 2.toByte(), 1.toByte(), 2, 12, 4, 0.618, 0.381, 1.0, 1.0)
		decorateChunkGen_List.add(b)
		val t = WE_StructureGen()
		t.add(sadOak, 1)
		t.add(dreamTree, 2)
		decorateChunkGen_List.add(t)
		val g = WorldGenGrass(true, false, false, false, 2.5)
		decorateChunkGen_List.add(g)
		val w = WorldGenGrapesWhiteAlfheim(2, AlfheimBlocks.grapesWhite)
		decorateChunkGen_List.add(w)
	}
}