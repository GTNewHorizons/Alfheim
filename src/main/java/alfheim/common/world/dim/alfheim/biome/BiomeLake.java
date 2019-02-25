package alfheim.common.world.dim.alfheim.biome;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import vazkii.botania.common.block.ModBlocks;

public class BiomeLake extends BiomeAlfheim {
	
	public BiomeLake() {
		super(0);
		
		BiomeDictionary.registerBiomeType(this, Type.RIVER, Type.WET);
		
		biomeMinValueOnMap		= -0.48;
		biomeMaxValueOnMap		= -0.38;
		biomePersistence		=  1.33;
		biomeNumberOfOctaves	=	  3;
		biomeScaleX				= 250.0;
		biomeScaleY				=	1.0;
		biomeSurfaceHeight		=	 58;
		biomeInterpolateQuality	=	  4;
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.clay, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -4, -2, true);
		standardBiomeLayers.add(Blocks.gravel, (byte)0, Blocks.clay, (byte)0, -256, 0, -256, 1, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		
		spawnableCreatureList.clear();
	}
}