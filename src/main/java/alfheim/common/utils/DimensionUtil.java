package alfheim.common.utils;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import alfheim.Constants;
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim;
import alfheim.common.world.dim.gen.BiomeGenAlfheim;

public class DimensionUtil {
	
	public static BiomeGenBase alfheimBiome	= new BiomeGenAlfheim(152);
	
	public static void init(){
		addDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim.class, false);
	}
	
	private static void addDimension(int id, Class<? extends WorldProvider> w, boolean keeploading){
	    Constants.debug("Registering dimension ID: " + id);
		DimensionManager.registerProviderType(id, w, keeploading);
		DimensionManager.registerDimension(id, id);
	}
}
