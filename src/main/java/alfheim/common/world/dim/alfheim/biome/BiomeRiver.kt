package alfheim.common.world.dim.alfheim.biome

import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer
import vazkii.botania.common.block.ModBlocks

object BiomeRiver: BiomeAlfheim() {
	
	init {
		setBiomeName("River")
		
		BiomeDictionary.registerBiomeType(this, Type.RIVER, Type.WET)
		
		biomeMinValueOnMap = -0.48
		biomeMaxValueOnMap = -0.38
		biomePersistence = 1.33
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 1.0
		biomeSurfaceHeight = 58
		biomeInterpolateQuality = 4
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.clay, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -4, -2, true)
		standardBiomeLayers.add(Blocks.gravel, 0.toByte(), Blocks.clay, 0.toByte(), -256, 0, -256, 1, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		
		spawnableCreatureList.clear()
	}
}