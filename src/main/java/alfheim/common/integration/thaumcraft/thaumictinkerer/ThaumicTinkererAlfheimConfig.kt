package alfheim.common.integration.thaumcraft.thaumictinkerer

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block

object ThaumicTinkererAlfheimConfig {
	
	val focusDislocationBlacklist: java.util.ArrayList<Block>
	
	init {
		focusDislocationBlacklist =
		
		try {
			val ItemFocusDislocation_c = Class.forName("thaumic.tinkerer.common.item.foci.ItemFocusDislocation") as Class<Any>
			ASJReflectionHelper.getStaticValue<Any, java.util.ArrayList<Block>>(ItemFocusDislocation_c, "blacklist")
		} catch (e: Throwable) {
			ASJUtilities.error("Error occured while configuring Dislocation Focus blacklist for Thaumic Tinkerer: $e")
			e.printStackTrace()
			
			ArrayList()
		}
		
		focusDislocationBlacklist.add(AlfheimBlocks.alfheimPortal)
		focusDislocationBlacklist.add(AlfheimBlocks.anyavil)
		focusDislocationBlacklist.add(AlfheimBlocks.manaAccelerator)
	}
}