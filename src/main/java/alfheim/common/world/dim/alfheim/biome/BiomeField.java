package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass;
import alfheim.common.world.dim.alfheim.struct.StructureArena;
import alfheim.common.world.dim.alfheim.struct.StructureDreamsTree;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import ru.vamig.worldengine.standardcustomgen.WE_LakeGen;
import ru.vamig.worldengine.standardcustomgen.WE_StructureGen;
import vazkii.botania.common.block.ModBlocks;

public class BiomeField extends BiomeAlfheim {
	
	public BiomeField() {
		super(0);
		
		biomeMinValueOnMap		=  -0.4;
		biomeMaxValueOnMap		=  0.82;
		biomePersistence		=	1.8;
		biomeNumberOfOctaves	=	  3;
		biomeScaleX				= 250.0;
		biomeScaleY				=	2.0;
		biomeSurfaceHeight		=	 71;
		biomeInterpolateQuality	=	  2;
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.dirt, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -4, -2, true);
		standardBiomeLayers.add(Blocks.grass, (byte)0, Blocks.dirt, (byte)0, -256, 0, -256, 0, false);
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		WE_StructureGen t = new WE_StructureGen();
		t.add(new StructureArena(), 0, 0, 0, 0, 0, 0, 1000, false, false);
		decorateChunkGen_List.add(t);
		WorldGenGrass g = new WorldGenGrass(true, true, true, true, 1.0);
		decorateChunkGen_List.add(g);
	}
}