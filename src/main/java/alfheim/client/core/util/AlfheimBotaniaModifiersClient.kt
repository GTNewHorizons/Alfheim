package alfheim.client.core.util

import alexsocol.asjlib.ASJReflectionHelper
import alfheim.api.lib.LibResourceLocations
import alfheim.common.item.equipment.bauble.ItemBalanceCloak
import vazkii.botania.client.render.tile.RenderTilePylon
import vazkii.botania.common.item.ModItems

object AlfheimBotaniaModifiersClient {
	
	fun postInit() {
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon::class.java, LibResourceLocations.gaiaPylon, "texturePink")
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon::class.java, LibResourceLocations.gaiaPylonOld, "texturePinkOld")
		
		ModItems.holyCloak.itemIcon = ItemBalanceCloak.iconHoly
		ModItems.unholyCloak.itemIcon = ItemBalanceCloak.iconUnholy
	}
}