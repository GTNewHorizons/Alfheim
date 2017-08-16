package alfheim.common.utils;

import alfheim.AlfheimCore;
import net.minecraftforge.common.config.Configuration;

public class AlfheimConfig extends Configuration {

	public static void syncConfig() {
		if (AlfheimCore.config.hasChanged()) {
			AlfheimCore.config.save();
		}
	}
}