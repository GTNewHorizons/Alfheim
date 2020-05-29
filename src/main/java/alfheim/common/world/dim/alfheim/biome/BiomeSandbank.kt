package alfheim.common.world.dim.alfheim.biome

import alfheim.common.block.AlfheimBlocks
import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer
import vazkii.botania.common.block.ModBlocks

object BiomeSandbank: BiomeAlfheim() {
	
	init {
		setBiomeName("Sandbank")
		
		BiomeDictionary.registerBiomeType(this, Type.SANDY, Type.SPARSE, Type.DRY)
		
		biomeMinValueOnMap = -0.41
		biomeMaxValueOnMap = -0.38
		biomePersistence = 1.33
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 0.5
		biomeSurfaceHeight = 62 + offset
		biomeInterpolateQuality = 2
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(AlfheimBlocks.elvenSand, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -4, -2, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
	}
}