package alfheim.client.lib;

import alfheim.api.lib.LibResourceLocations;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public class LibResourceLocationsActual {

	public static void init() {
		LibResourceLocations.babylon = new ResourceLocation(LibResources.MISC_BABYLON);
		LibResourceLocations.elvenPylon = new ResourceLocation(LibResources.MODEL_PYLON_PINK);
		LibResourceLocations.elvenPylonOld = new ResourceLocation(LibResources.MODEL_PYLON_PINK_OLD);
		LibResourceLocations.glowCyan = new ResourceLocation(LibResources.MISC_GLOW_CYAN);
		LibResourceLocations.halo = new ResourceLocation(LibResources.MISC_HALO);
		LibResourceLocations.lexica = new ResourceLocation(LibResources.MODEL_LEXICA);
		LibResourceLocations.manaInfuserOverlay = new ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY);
		LibResourceLocations.petalOverlay = new ResourceLocation(LibResources.GUI_PETAL_OVERLAY);
		LibResourceLocations.pixie = new ResourceLocation(LibResources.MODEL_PIXIE);
		LibResourceLocations.spreader = new ResourceLocation(LibResources.MODEL_SPREADER);
	}
}