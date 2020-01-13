package alfheim.common.world.dim.alfheim.biome

import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass
import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer
import vazkii.botania.common.block.ModBlocks

class BiomeMount3Field: BiomeAlfheim(0) {
	
	init {
		setBiomeName("High plateau field")
		
		BiomeDictionary.registerBiomeType(this, Type.PLAINS, Type.MOUNTAIN, Type.DENSE, Type.LUSH)
		
		biomeMinValueOnMap = 0.49
		biomeMaxValueOnMap = 0.58
		biomePersistence = 1.8
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 2.4
		biomeSurfaceHeight = 144
		biomeInterpolateQuality = 1
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.dirt, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -256, -2, true)
		standardBiomeLayers.add(Blocks.grass, 0.toByte(), Blocks.dirt, 0.toByte(), -256, 0, -256, 0, false)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		val g = WorldGenGrass(true, true, true, true, 1.2)
		decorateChunkGen_List.add(g)
	}
}