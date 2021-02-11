//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import alexsocol.patcher.PatcherConfigHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WE_WorldProvider extends WorldProvider {
	
	public static final int we_id = PatcherConfigHandler.INSTANCE.getWEBiomeID();
	public final float rainfall = 0.1F;
	public WE_ChunkProvider cp = null;
	
	@Override
	public void registerWorldChunkManager() {
		if (cp == null) cp = new WE_ChunkProvider(this);
		worldChunkMgr = new WE_WorldChunkManager(new WE_Biome(we_id, true), cp, rainfall);
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		if (cp == null) cp = new WE_ChunkProvider(this);
		return cp;
	}
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		//worldObj.getChunkProvider()
		if (cp == null) cp = new WE_ChunkProvider(this);
		return WE_Biome.getBiomeAt(cp, x, z);
	}
	
	public abstract void genSettings(WE_ChunkProvider cp);
}