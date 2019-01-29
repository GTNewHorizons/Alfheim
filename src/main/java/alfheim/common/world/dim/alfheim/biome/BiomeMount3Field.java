package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import vazkii.botania.common.block.ModBlocks;

public class BiomeMount3Field extends BiomeAlfheim {
	public BiomeMount3Field() {
		super(0);
		
		biomeMinValueOnMap		=  0.49;
		biomeMaxValueOnMap		=  0.58;
		biomePersistence		=	1.8;
		biomeNumberOfOctaves	=	  3;
		biomeScaleX				= 250.0;
		biomeScaleY				=	2.4;
		biomeSurfaceHeight		=	144;
		biomeInterpolateQuality	=	  1;
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.dirt, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -256, -2, true);
		standardBiomeLayers.add(Blocks.grass, (byte)0, Blocks.dirt, (byte)0, -256, 0, -256, 0, false);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		WorldGenGrass g = new WorldGenGrass(true, true, true, true, 1.2);
		decorateChunkGen_List.add(g);
	}
}