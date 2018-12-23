package alfheim.common.core.util;

import alexsocol.asjlib.ASJReflectionHelper;
import alfheim.api.lib.LibResourceLocations;
import vazkii.botania.client.render.tile.RenderTilePylon;

public class AlfheimBotaniaModifiers {
	
	public static void postInit() {
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon.class, LibResourceLocations.gaiaPylon, "texturePink");
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon.class, LibResourceLocations.gaiaPylonOld, "texturePinkOld");
	}
}