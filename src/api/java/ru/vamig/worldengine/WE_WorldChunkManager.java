package ru.vamig.worldengine;

import net.minecraft.world.biome.*;

public class WE_WorldChunkManager extends WorldChunkManagerHell {
	
	public WE_ChunkProvider cp;
	
	public WE_WorldChunkManager(WE_Biome biome, WE_ChunkProvider cp, float rainfall) {
		super(biome, rainfall);
		this.cp = cp;
	}
	
	@Override
	public BiomeGenBase getBiomeGenAt(int x, int z) {
		return WE_Biome.getBiomeAt(cp, x, z);
	}
}
