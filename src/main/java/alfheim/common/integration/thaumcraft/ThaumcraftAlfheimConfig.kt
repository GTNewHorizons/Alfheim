package alfheim.common.integration.thaumcraft

object ThaumcraftAlfheimConfig {
	
	fun loadConfig() {
		TCHandlerAlfheimAspects.addAspects()
		TCHandlerShadowFoxAspects.addAspects()
	}
	
//	val outerLandsID = if (Botania.thaumcraftLoaded) Config.dimensionOuterId else Int.MIN_VALUE
}