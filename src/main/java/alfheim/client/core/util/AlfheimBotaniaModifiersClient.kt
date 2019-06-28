package alfheim.client.core.util

import alexsocol.asjlib.ASJReflectionHelper
import alfheim.api.lib.LibResourceLocations
import vazkii.botania.client.render.tile.RenderTilePylon

object AlfheimBotaniaModifiersClient {
	
	fun postInit() {
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon::class.java, LibResourceLocations.gaiaPylon, "texturePink")
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon::class.java, LibResourceLocations.gaiaPylonOld, "texturePinkOld")
	}
}