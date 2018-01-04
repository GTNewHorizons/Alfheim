package alfheim.common.world.dim;

import alfheim.Constants;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim;
import alfheim.common.world.dim.alfheim.gen.BiomeGenAlfheim;
import alfheim.common.world.dim.alfheim.gen.BiomeGenAlfheimBeach;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;

public class DimensionUtil {

	public static BiomeGenBase alfheimBiome = new BiomeGenAlfheim(152);
	public static BiomeGenBase alfheimBeachBiome = new BiomeGenAlfheimBeach(153);

	public static void init() {
		addDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim.class, false);
	}

	private static void addDimension(int id, Class<? extends WorldProvider> w, boolean keeploading) {
		Constants.debug("Registering dimension ID: " + id);
		DimensionManager.registerProviderType(id, w, keeploading);
		DimensionManager.registerDimension(id, id);
	}
}
