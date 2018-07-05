package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import vazkii.botania.common.block.ModBlocks;

public class BiomeSandbank extends BiomeAlfheim {
	
	public BiomeSandbank() {
		super(0);
		
		biomeMinValueOnMap		= -0.41;
		biomeMaxValueOnMap		= -0.38;
		biomePersistence		=  1.33;
		biomeNumberOfOctaves	=	  3;
		biomeScaleX				= 250.0;
		biomeScaleY				=	0.5;
		biomeSurfaceHeight		=	 62;
		biomeInterpolateQuality	=	  2;
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(AlfheimBlocks.elvenSand, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -4, -2, true);
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
	}
}