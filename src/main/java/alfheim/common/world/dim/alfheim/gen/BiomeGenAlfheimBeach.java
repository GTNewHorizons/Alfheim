package alfheim.common.world.dim.alfheim.gen;

import alfheim.common.core.registry.AlfheimBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenAlfheimBeach extends BiomeGenBase {
	
	public BiomeGenAlfheimBeach(int p_i1969_1_) {
		super(p_i1969_1_);
		this.spawnableCreatureList.clear();
		this.setBiomeName("Alfheim Sands");
		this.topBlock = AlfheimBlocks.elvenSand;
		this.fillerBlock = AlfheimBlocks.elvenSand;
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.deadBushPerChunk = 0;
		this.theBiomeDecorator.reedsPerChunk = 0;
		this.theBiomeDecorator.cactiPerChunk = 0;

		this.waterColorMultiplier = 0x00C8FF;
		this.setColor(0x00FF00);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeFoliageColor(int R, int G, int B) {
		return 0x08F500;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor(int R, int G, int B) {
		return 0x08F500;
	}
}