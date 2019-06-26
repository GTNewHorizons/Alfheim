package alfheim.common.world.dim.alfheim.biome

import alfheim.common.entity.EntityAlfheimPixie
import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass
import alfheim.common.world.dim.alfheim.struct.StructureArena
import net.minecraft.init.Blocks
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer
import ru.vamig.worldengine.standardcustomgen.WE_StructureGen
import vazkii.botania.common.block.ModBlocks

class BiomeField: BiomeAlfheim(0) {
	init {
		
		BiomeDictionary.registerBiomeType(this, Type.PLAINS, Type.DENSE)
		
		biomeMinValueOnMap = -0.4
		biomeMaxValueOnMap = 0.82
		biomePersistence = 1.8
		biomeNumberOfOctaves = 3
		biomeScaleX = 250.0
		biomeScaleY = 2.0
		biomeSurfaceHeight = 71
		biomeInterpolateQuality = 2
		
		var standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.dirt, 0.toByte(), ModBlocks.livingrock, 0.toByte(), -256, 0, -4, -2, true)
		standardBiomeLayers.add(Blocks.grass, 0.toByte(), Blocks.dirt, 0.toByte(), -256, 0, -256, 0, false)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		standardBiomeLayers = WE_BiomeLayer()
		standardBiomeLayers.add(Blocks.bedrock, 0.toByte(), 0, 0, 0, 0, true)
		createChunkGen_InXZ_List.add(standardBiomeLayers)
		val t = WE_StructureGen()
		t.add(StructureArena(), 1000)
		decorateChunkGen_List.add(t)
		val g = WorldGenGrass(true, true, true, true, 1.0)
		decorateChunkGen_List.add(g)
		
		spawnableCreatureList.add(BiomeGenBase.SpawnListEntry(EntityAlfheimPixie::class.java, 1, 1, 2))
	}
}