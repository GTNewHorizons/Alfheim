package alfheim.common.world.dim;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.utils.DimensionUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.botania.api.item.IFlowerlessWorld;

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
        return true;
    }

    @Override
    public float calculateCelestialAngle(long var1, float var3) {
    	int j = (int)(var1 % 24000L);
        float f1 = ((float)j + var3) / 24000.0F - 0.25F;

        if (f1 < 0.0F) {
            ++f1;
        }

        if (f1 > 1.0F) {
            --f1;
        }

        float f2 = f1;
        f1 = 1.0F - (float)((Math.cos((double)f1 * Math.PI) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }

    @Override
    public boolean canRespawnHere() {
        return AlfheimCore.enableElvenStory;
    }
    
    @Override
    public String getDimensionName() {
        return "Alfheim";
    }
}
