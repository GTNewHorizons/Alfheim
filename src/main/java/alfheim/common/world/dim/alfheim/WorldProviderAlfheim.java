package alfheim.common.world.dim.alfheim;

import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.world.dim.alfheim.biome.BiomeField;
import alfheim.common.world.dim.alfheim.biome.BiomeForest;
import alfheim.common.world.dim.alfheim.struct.StructureSpawnpoint;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.WE_Biome;
import ru.vamig.worldengine.WE_ChunkProvider;
import ru.vamig.worldengine.WE_WorldProvider;
import ru.vamig.worldengine.standardcustomgen.WE_BiomeLayer;
import ru.vamig.worldengine.standardcustomgen.WE_CaveGen;
import ru.vamig.worldengine.standardcustomgen.WE_LakeGen;
import ru.vamig.worldengine.standardcustomgen.WE_OreGen;
import ru.vamig.worldengine.standardcustomgen.WE_RavineGen;
import ru.vamig.worldengine.standardcustomgen.WE_SnowGen;
import ru.vamig.worldengine.standardcustomgen.WE_StructureGen;
import ru.vamig.worldengine.standardcustomgen.WE_TerrainGenerator;
import vazkii.botania.common.block.ModBlocks;

public class WorldProviderAlfheim extends WE_WorldProvider {
	@Override
	public float getCloudHeight() {
		return 128.0F;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return true;
	}
	
	@Override
	public float calculateCelestialAngle(long var1, float var3) {
		int j = (int)(var1 % 24000L);
		float f1 = ((float)j + var3) / 24000.0F - 0.25F;
		
		if(f1 < 0.0F) {
			++f1;
		}
		
		if(f1 > 1.0F) {
			--f1;
		}
		
		float f2 = f1;
		f1 = 1.0F - (float)((Math.cos((double)f1 * Math.PI) + 1.0) / 2.0);
		f1 = f2 + (f1 - f2) / 3.0F;
		return f1;
	}
	
	@Override
	public boolean canRespawnHere() {
		return AlfheimCore.enableElvenStory || AlfheimConfig.enableAlfheimRespawn;
	}
	
	@Override
	public String getDimensionName() {
		return "Alfheim";
	}
	
	@Override
	public void genSettings(WE_ChunkProvider cp) {
		cp.createChunkGen_List.clear();
		cp.createChunkGen_InXZ_List.clear();
		cp.createChunkGen_InXYZ_List.clear();
		cp.decorateChunkGen_List.clear();
		
		WE_Biome.setBiomeMap(cp, 1.2, 4, 3200.0, 0.475);
		
		WE_TerrainGenerator terrainGenerator = new WE_TerrainGenerator();
		terrainGenerator.worldStoneBlock = ModBlocks.livingrock;
		cp.createChunkGen_List.add(terrainGenerator);
		WE_CaveGen cg = new WE_CaveGen();
		cg.replaceBlocksList.clear();
		cg.replaceBlocksMetaList.clear();
		cg.addReplacingBlock(ModBlocks.livingrock, (byte)0);
		cp.createChunkGen_List.add(cg);
		WE_RavineGen rg = new WE_RavineGen();
		rg.replaceBlocksList.clear();
		rg.replaceBlocksMetaList.clear();
		rg.addReplacingBlock(ModBlocks.livingrock, (byte)0);
		cp.createChunkGen_List.add(rg);
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.dirt, (byte)0, ModBlocks.livingrock, (byte)0, -256, 0, -4, -2, true);
		standardBiomeLayers.add(Blocks.grass, (byte)0, Blocks.dirt, (byte)0, -256, 0, -256, 0, false);
		standardBiomeLayers.add(Blocks.bedrock, (byte)0, 0, 0, 0, 0, true);
		cp.createChunkGen_InXZ_List.add(standardBiomeLayers);
		WE_SnowGen snowGen = new WE_SnowGen();
		snowGen.snowPoint = 127;
		snowGen.randomSnowPoint = 8;
		cp.createChunkGen_InXZ_List.add(snowGen);
		WE_OreGen ores = new WE_OreGen();
		ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 0, 1, 8, 1, 2, 75,  1, 16);
		ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 1, 1, 8, 3, 6, 100, 1, 64);
		ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 2, 4, 8, 1, 1, 100, 1, 48);
		ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 3, 1, 8, 2, 3, 100, 1, 32);
		ores.add(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 4, 1, 4, 1, 1, 50,  1, 16);
		cp.decorateChunkGen_List.add(ores);
		WE_StructureGen t = new WE_StructureGen();
		t.add(new StructureSpawnpoint(), 0, 0, 0, 0, 0, 0, 1000, false, true);
		cp.decorateChunkGen_List.add(t);
		WE_LakeGen waterLakes = new WE_LakeGen();
		cp.decorateChunkGen_List.add(waterLakes);
		
		WE_Biome.addBiomeToGeneration(cp, new BiomeField());
		WE_Biome.addBiomeToGeneration(cp, new BiomeForest());
	}
}
