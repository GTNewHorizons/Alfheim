package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass;
import alfheim.common.world.dim.alfheim.struct.StructureArena;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import ru.vamig.worldengine.standardcustomgen.WE_StructureGen;
import vazkii.botania.common.block.ModBlocks;

public class BiomeBeach extends BiomeAlfheim {
	
	public BiomeBeach() {
		super(0);
		
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