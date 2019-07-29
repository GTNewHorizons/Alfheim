package alfheim.common.integration.thaumcraft

object ThaumcraftAlfheimConfig {
	
	fun loadConfig() {
		TCHandlerAlfheimAspects.addAspects()
		TCHandlerShadowFoxAspects.addAspects()
	}
}