//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine.standardcustomgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import ru.vamig.worldengine.additions.*;

public class WE_SnowGen extends WE_CreateChunkGen_InXZ {
	
	//-//
	public final Block snowBlock = Blocks.snow_layer;
	public final byte snowBlockMeta = 0;
	public final boolean genSnow = true;
	//-//
	public final Block iceBlock = Blocks.ice;
	public final byte iceBlockMeta = 0;
	public final Material freezeMaterial = Material.water;
	//-//
	public final int snowOnWaterRandom = 2;
	public int snowPoint = 111;
	public int randomSnowPoint = 2;
	
	@Override
	public void gen(WE_GeneratorData data) {
		for (int y = 255; y >= snowPoint + data.chunkProvider.rand.nextInt(randomSnowPoint + 1); y--)
			if (getBlock(data, y) != null) {
				if (getBlock(data, y).getMaterial() == freezeMaterial) {
					setBlock(data, iceBlock, iceBlockMeta, y);
					if (genSnow && data.chunkProvider.rand.nextInt(snowOnWaterRandom + 1) == 0)
						setBlock(data, snowBlock, snowBlockMeta, y + 1);
				} else if (genSnow)
					setBlock(data, snowBlock, snowBlockMeta, y + 1);
				break;
			}
	}
}