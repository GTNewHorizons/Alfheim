//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine.additions;

import net.minecraft.block.Block;
import ru.vamig.worldengine.*;

public class WE_GeneratorData {
	
	public final WE_ChunkProvider chunkProvider;
	//-//
	public final Block[] chunkBlocks;
	public final byte[] chunkBlocksMeta;
	//-//
	public final long chunk_X;
	public final long chunk_Z;
	//-//
	public final WE_Biome[][] biomes;
	//-//
	public final int cr_x;
	public final int cr_y;
	public final int cr_z;
	
	public WE_GeneratorData(WE_ChunkProvider cp, Block[] cb, byte[] cbm, long cx, long cz, WE_Biome[][] b, int x, int y, int z) {
		chunkProvider = cp;
		//-//
		chunkBlocks = cb;
		chunkBlocksMeta = cbm;
		//-//
		chunk_X = cx;
		chunk_Z = cz;
		//-//
		biomes = b;
		//-//
		cr_x = x;
		cr_y = y;
		cr_z = z;
	}
}