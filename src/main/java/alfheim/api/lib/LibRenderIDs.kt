package alfheim.api.lib

import cpw.mods.fml.client.registry.RenderingRegistry

object LibRenderIDs {
	
	var idAniTorch = -1
	var idAnomaly = -1
	var idAnyavil = -1
	var idItemHolder = -1
	var idPylon = -1
	var idShrinePanel = -1
	var idTransferer = -1
	
	// Iridescence
	var idDoubleFlower = -1
	var idMultipass = -1
	var idHopper = -1
	
	fun init() {
		idAniTorch = RenderingRegistry.getNextAvailableRenderId()
		idAnomaly = RenderingRegistry.getNextAvailableRenderId()
		idAnyavil = RenderingRegistry.getNextAvailableRenderId()
		idItemHolder = RenderingRegistry.getNextAvailableRenderId()
		idPylon = RenderingRegistry.getNextAvailableRenderId()
		idShrinePanel = RenderingRegistry.getNextAvailableRenderId()
		idTransferer = RenderingRegistry.getNextAvailableRenderId()
		
		idDoubleFlower = RenderingRegistry.getNextAvailableRenderId()
		idMultipass = RenderingRegistry.getNextAvailableRenderId()
		idHopper = RenderingRegistry.getNextAvailableRenderId()
	}
}
