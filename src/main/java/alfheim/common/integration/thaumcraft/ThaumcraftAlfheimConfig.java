package alfheim.common.integration.thaumcraft;

import alfheim.common.integration.thaumcraft.handler.TCHandlerAspects;

public class ThaumcraftAlfheimConfig {

	public static void loadConfig() {
		TCHandlerAspects.addAspects();
	}
}