package alfheim.common.world.dim;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim;
import alfheim.common.world.dim.alfheim.gen.BiomeGenAlfheim;
import alfheim.common.world.dim.alfheim.gen.BiomeGenAlfheimBeach;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;

public class DimensionUtil {

	public static BiomeGenBase alfheimBiome;
	public static BiomeGenBase alfheimBeachBiome;

	public static void init() {
		addDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim.class, false);
		alfheimBiome = new BiomeGenAlfheim(AlfheimConfig.biomeIDAlfheim);
		alfheimBeachBiome = new BiomeGenAlfheimBeach(AlfheimConfig.biomeIDAlfheimBeach);
	}

	private static void addDimension(int id, Class<? extends WorldProvider> w, boolean keeploading) {
		ASJUtilities.log("Registering dimension ID: " + id);
		DimensionManager.registerProviderType(id, w, keeploading);
		DimensionManager.registerDimension(id, id);
	}
}
