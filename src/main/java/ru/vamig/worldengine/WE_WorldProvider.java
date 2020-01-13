//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import alfheim.common.core.handler.AlfheimConfigHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WE_WorldProvider extends WorldProvider {
	
	public final int we_id = AlfheimConfigHandler.INSTANCE.getBiomeIDAlfheim();
	public final float rainfall = 0.1F;
	public WE_ChunkProvider cp = null;
	
	@Override
	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(new WE_Biome(we_id, true), rainfall);
    }
	
	@Override
	public IChunkProvider createChunkGenerator() {
		if (cp == null) cp = new WE_ChunkProvider(this);
		return cp;
	}
	
	public abstract void genSettings(WE_ChunkProvider cp);
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		//worldObj.getChunkProvider()
		if (cp == null) cp = new WE_ChunkProvider(this);
		return WE_Biome.getBiomeAt(cp, x, z);
	}
}