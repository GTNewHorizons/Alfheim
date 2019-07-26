//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WE_WorldProvider extends WorldProvider {
	final int we_id = AlfheimConfig.INSTANCE.getBiomeIDAlfheim();
	final float rainfall = 0.1F;
	
	@Override
	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(new WE_Biome(we_id, true), rainfall);
    }
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new WE_ChunkProvider(this);
	}
	
	public abstract void genSettings(WE_ChunkProvider cp);
}