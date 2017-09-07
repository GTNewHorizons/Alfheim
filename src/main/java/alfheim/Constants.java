package alfheim;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class Constants {
	public static final int major_version = 0;
	public static final int minor_version = 0;
	public static final int build_version = 1;

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version + "." + minor_version + "." + build_version;
	
	public static void debug(String message) { 
		FMLRelaunchLog.log("[DEBUG] " + NAME, Level.INFO, message);
	}
}