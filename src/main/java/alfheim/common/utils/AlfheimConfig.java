package alfheim.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AlfheimConfig extends Configuration {

	// DIMENSIONS
	public static int dimensionIDAlfheim;

	public static void syncConfig() {
		List<String> propOrder = new ArrayList<String>();
		try {
			Property prop;

			prop = AlfheimCore.config.get("dimensions", "dimensionIDAlfheim", -105);
			prop.comment = "Dimension ID for Alfheim";
			prop.setLanguageKey("gc.configgui.dimensionIDAlfheim").setRequiresMcRestart(true);
			dimensionIDAlfheim = prop.getInt();
			propOrder.add(prop.getName());

			if (AlfheimCore.config.hasChanged()) {
				AlfheimCore.config.save();
			}
		} catch (final Exception e) {
			FMLLog.log(Level.ERROR, e, Constants.NAME + " has a problem loading it's config");
		}
	}
}