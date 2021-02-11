//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import ru.vamig.worldengine.additions.*;
import ru.vamig.worldengine.standardcustomgen.*;

import java.util.*;

public class WE_Biome extends BiomeGenBase {
	
	//////////////////
	//- Generators -//
	//////////////////
	public final List<WE_CreateChunkGen_InXZ> createChunkGen_InXZ_List = new ArrayList<>();
	public final List<WE_CreateChunkGen_InXYZ> createChunkGen_InXYZ_List = new ArrayList<>();
	public final List<IWorldGenerator> decorateChunkGen_List = new ArrayList<>();
	public int id;
	//////////////////
	//- Biome Info -//
	//////////////////
	public double
		biomeMinValueOnMap = -1.0D,
		biomeMaxValueOnMap = 1.0D,
		biomePersistence = 1.0D,
		biomeScaleX = 1.0D, biomeScaleY = 1.0D;
	public int biomeNumberOfOctaves = 1,
		biomeSurfaceHeight = 63,
		biomeInterpolateQuality = 16;
	public int grassColor = 0x08F500;
	
	/////
	//=//
	/////
	
	public WE_Biome(int ID_FOR_ALL_WE_BIOMES,
	                double minMapValue, double maxMapValue,
	                double persistence, int numOctaves, double sx, double sy,
	                int height, int interpolateQuality) {
		this(ID_FOR_ALL_WE_BIOMES);
		//-//
		biomeMinValueOnMap = minMapValue;
		biomeMaxValueOnMap = maxMapValue;
		//-//
		biomePersistence = persistence;
		biomeNumberOfOctaves = numOctaves;
		biomeScaleX = sx;
		biomeScaleY = sy;
		//-//
		biomeSurfaceHeight = height;
		biomeInterpolateQuality = interpolateQuality;
	}
	
	public WE_Biome(int ID_FOR_ALL_WE_BIOMES) {
		this(ID_FOR_ALL_WE_BIOMES, false);
	}
	
	public WE_Biome(int ID_FOR_ALL_WE_BIOMES, boolean r) {
		super(ID_FOR_ALL_WE_BIOMES, r);
		setBiomeName("-=|World-Engine|=-");
		
		BiomeDictionary.registerBiomeType(this, Type.MAGICAL);
		
		WE_BiomeLayer standardBiomeLayers = new WE_BiomeLayer();
		standardBiomeLayers.add(Blocks.dirt, (byte) 0, Blocks.stone, (byte) 0, -256, 0, -4, -2, true);
		standardBiomeLayers.add(Blocks.grass, (byte) 0, Blocks.dirt, (byte) 0, -256, 0, -256, 0, false);
		standardBiomeLayers.add(Blocks.bedrock, (byte) 0, 0, 2, 0, 0, true);
		createChunkGen_InXZ_List.add(standardBiomeLayers);
		//-//
		createChunkGen_InXZ_List.add(new WE_SnowGen());
		
		decorateChunkGen_List.add(new WE_LakeGen());
		//-//
		WE_WorldTreeGen treeGen = new WE_WorldTreeGen();
		treeGen.add(Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, Blocks.vine, Blocks.cocoa, 8, 1, 8, 4, false,
			false, (byte) 2, (byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 1, 1, 12, 4, 0.618D, 0.381D, 1.0D, 1.0D);
		decorateChunkGen_List.add(treeGen);
		//-//
		WE_GrassGen grassGen = new WE_GrassGen();
		grassGen.add(Blocks.tallgrass, (byte) 1, 8, false, Blocks.grass, (byte) 0);
		grassGen.add(Blocks.tallgrass, (byte) 2, 16, false, Blocks.grass, (byte) 0);
		grassGen.add(Blocks.red_flower, (byte) 0, 128, false, Blocks.grass, (byte) 0);
		grassGen.add(Blocks.yellow_flower, (byte) 0, 128, false, Blocks.grass, (byte) 0);
		grassGen.add(Blocks.deadbush, (byte) 0, 512, false, Blocks.grass, (byte) 0);
		grassGen.add(Blocks.waterlily, (byte) 0, 256, false, Blocks.water, (byte) 0);
		decorateChunkGen_List.add(grassGen);
	}
	
	public static void setBiomeMap(WE_ChunkProvider cp, double persistence, int numOctaves, double sx, double sy) {
		cp.biomemapPersistence = persistence;
		cp.biomemapNumberOfOctaves = numOctaves;
		cp.biomemapScaleX = sx;
		cp.biomemapScaleY = sy;
	}
	
	public static int addBiomeToGeneration(WE_ChunkProvider cp, WE_Biome biome) {
		if (biome.biomeMaxValueOnMap < biome.biomeMinValueOnMap) {
			double t = biome.biomeMaxValueOnMap;
			biome.biomeMaxValueOnMap = biome.biomeMinValueOnMap;
			biome.biomeMinValueOnMap = t;
		}
		//-//
		cp.biomesList.add(biome);
		biome.id = cp.biomesList.size();
		//-//
		if (cp.standardBiomeOnMap == null)
			cp.standardBiomeOnMap = biome;
		//-//
		return biome.id;
	}
	
	@SuppressWarnings("RedundantCast")
	public static WE_Biome getBiomeAt(WE_ChunkProvider cp, int x, int z) {
		return getBiomeAt(cp, (long) x, (long) z);
	}
	
	public static WE_Biome getBiomeAt(WE_ChunkProvider cp, long x, long z) {
		double biomeMapData = WE_PerlinNoise.PerlinNoise2D((long) Math.pow(cp.worldObj.getSeed() * 84, 6),
			x / cp.biomemapScaleX, z / cp.biomemapScaleX,
			cp.biomemapPersistence, cp.biomemapNumberOfOctaves)
			                      * cp.biomemapScaleY;
		//-//
		WE_Biome r = null;
		for (int i = 0; i < cp.biomesList.size(); i++)
			if (biomeMapData >= cp.biomesList.get(i).biomeMinValueOnMap && biomeMapData <= cp.biomesList.get(i).biomeMaxValueOnMap)
				if (r != null) {
					if (cp.biomesList.get(i).biomeMaxValueOnMap - cp.biomesList.get(i).biomeMinValueOnMap < r.biomeMaxValueOnMap - r.biomeMinValueOnMap)
						r = cp.biomesList.get(i);
				} else
					r = cp.biomesList.get(i);
		//-//
		if (r == null)
			r = cp.standardBiomeOnMap;
		return r;
	}
	
	public static int getBiggestInterpolationQuality(WE_ChunkProvider cp) {
		int r = 0;
		for (int i = 0; i < cp.biomesList.size(); i++)
			if (cp.biomesList.get(i).biomeInterpolateQuality > r)
				r = cp.biomesList.get(i).biomeInterpolateQuality;
		return r;
	}
	
	public WE_Biome clearSpawn() {
		spawnableCaveCreatureList.clear();
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		return this;
	}
	
	@Override
	public int getBiomeGrassColor(int R, int G, int B) {
		return grassColor;
	}
	
	@Override
	public int getBiomeFoliageColor(int R, int G, int B) {
		return grassColor;
	}
	
	@Override
	public boolean isEqualTo(BiomeGenBase biome) {
		return super.isEqualTo(biome);
	}
}