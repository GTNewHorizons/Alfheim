package alfheim;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class Constants {
	public static final String major_version = "pre";
	public static final String minor_version = "ALPHA";
	public static final String build_version = "1";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version + "." + minor_version + "-" + build_version;
	
	public static void debug(String message) { 
		FMLRelaunchLog.log("[DEBUG] " + NAME, Level.INFO, message);
	}
}