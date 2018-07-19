//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WE_WorldProvider extends WorldProvider {
	int we_id = 0;
	float rainfall = 0.1F;
	
	@Override
	public void registerWorldChunkManager() {
		/*System.out.println("////////////////////////////////////-"                                      );
		System.out.println("//#===============================//=* Version: " + WE_Main.VERSION + "."   );
		System.out.println("//#=-------| WorldEngine |-------=//=* By Vamig Aliev (vk.com/win_vista)."  );
		System.out.println("//#===============================//=* Part of VamigA_core (vk.com/vamiga).");
		System.out.println("////////////////////////////////////-"                                      );
		//-//
		System.out.println("WorldEngine: -Registering WorldEngine..."          );*/
		worldChunkMgr = new WorldChunkManagerHell(new WE_Biome(we_id, true), rainfall);
		//System.out.println("WorldEngine: -Registration completed successfully!");
    }
	
	@Override
	public IChunkProvider createChunkGenerator() {
		//System.out.println("WorldEngine: -Starting WorldEngine..."          );
		WE_ChunkProvider m = new WE_ChunkProvider(this);
		//System.out.println("WorldEngine: -WorldEngine started successfully!");
		//-//
		return m;
	}
	
	public abstract void genSettings(WE_ChunkProvider cp);
}