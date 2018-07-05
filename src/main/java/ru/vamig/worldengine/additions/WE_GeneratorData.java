//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine.additions;

import net.minecraft.block.Block;
import ru.vamig.worldengine.WE_Biome;
import ru.vamig.worldengine.WE_ChunkProvider;

public class WE_GeneratorData {
	public WE_ChunkProvider chunkProvider;
	//-//
	public Block[] chunkBlocks    ;
	public byte [] chunkBlocksMeta;
	//-//
	public long chunk_X, chunk_Z;
	//-//
	public WE_Biome[][] biomes;
	//-//
	public int cr_x, cr_y, cr_z;
	
	public WE_GeneratorData(WE_ChunkProvider cp, Block[] cb, byte[] cbm, long cx, long cz, WE_Biome[][] b, int x, int y, int z) {
		chunkProvider = cp;
		//-//
		chunkBlocks     =  cb;
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