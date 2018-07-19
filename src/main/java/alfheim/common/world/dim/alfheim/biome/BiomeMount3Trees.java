package alfheim.common.world.dim.alfheim.biome;

import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.world.dim.alfheim.customgens.WorldGenGrass;
import alfheim.common.world.dim.alfheim.struct.StructureArena;
import alfheim.common.world.dim.alfheim.struct.StructureDreamsTree;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import ru.vamig.worldengine.standardcustomgen.WE_LakeGen;
import ru.vamig.worldengine.standardcustomgen.WE_StructureGen;
import ru.vamig.worldengine.standardcustomgen.WE_WorldTreeGen;
import vazkii.botania.common.block.ModBlocks;

public class BiomeMount3Trees extends BiomeAlfheim {
	public BiomeMount3Trees() {
		super(0);
		
		biomeMinValueOnMap		=  0.43;
		biomeMaxValueOnMap		=  0.65;
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
		WE_LakeGen waterLakes = new WE_LakeGen();
		waterLakes.chunksForLake = 2;
		waterLakes.minY = 124;
		decorateChunkGen_List.add(waterLakes);
		WE_WorldTreeGen b = new WE_WorldTreeGen();
		b.add(Blocks.log,  0, Blocks.leaves,  0, Blocks.sapling, Blocks.vine, Blocks.cocoa, 2, 1, 1, 4, false, (byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1, 1, 12, 4, 0.618, 0.381, 1.0, 1.0);
		b.add(Blocks.log,  0, Blocks.leaves,  0, Blocks.sapling, Blocks.vine, Blocks.cocoa, 2, 1, 1, 4, false, (byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1, 2, 12, 4, 0.618, 0.381, 1.0, 1.0);
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, Blocks.vine, Blocks.cocoa, 2, 1, 1, 4, false, (byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1, 1, 12, 4, 0.618, 0.381, 1.0, 1.0);
		b.add(Blocks.log2, 1, Blocks.leaves2, 1, Blocks.sapling, Blocks.vine, Blocks.cocoa, 2, 1, 1, 4, false, (byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1, 2, 12, 4, 0.618, 0.381, 1.0, 1.0);
		decorateChunkGen_List.add(b);
		WE_StructureGen t = new WE_StructureGen();
		t.add(new StructureDreamsTree(Blocks.log, Blocks.leaves, 0, 4, 8, 0), 1);
		t.add(new StructureDreamsTree(AlfheimBlocks.dreamlog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0), 2);
		decorateChunkGen_List.add(t);
		WorldGenGrass g = new WorldGenGrass(true, true, true, true, 1.2);
		decorateChunkGen_List.add(g);
	}
}