package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import vazkii.botania.common.block.ModBlocks;

public class BiomeBeach extends BiomeAlfheim {
	
	public BiomeBeach() {
		super(0);
		
		BiomeDictionary.registerBiomeType(this, Type.SANDY, Type.SPARSE, Type.BEACH);
		
		biomeMinValueOnMap		=  -0.5;
		biomeMaxValueOnMap		= -0.35;
		biomePersistence		=  1.33;
		biomeNumberOfOctaves	=	  3;
		biomeScaleX				= 250.0;
		biomeScaleY				=	1.4;
		biomeSurfaceHeight		=	 65;
		biomeInterpolateQuality	=	  4;
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(AlfheimBlocks.elvenSand, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -4, -2, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
	}
}