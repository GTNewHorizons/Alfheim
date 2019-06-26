package alfheim.common.integration.thaumcraft

import alfheim.common.integration.thaumcraft.handler.TCHandlerAspects

object ThaumcraftAlfheimConfig {
	
	fun loadConfig() {
		TCHandlerAspects.addAspects()
	}
}