package alfheim.common.dimension;

import alfheim.Constants;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.utils.DimensionUtil;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderAlfheim extends WorldProvider {

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(DimensionUtil.alfheimBiome, 0.5F);
        this.dimensionId = AlfheimConfig.dimensionIDAlfheim;
        isHellWorld = false;
    }
    
    @Override
    public String getSaveFolder() {
    	return "Alfheim";
    }

    @Override
    public float getCloudHeight() {
        return 128.0F;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderAlfheim(this.worldObj, this.worldObj.getSeed());
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public float calculateCelestialAngle(long var1, float var3) {
        return 0.1F;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public String getDimensionName() {
        return "Alfheim";
    }
}
