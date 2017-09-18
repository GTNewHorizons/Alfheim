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
	
	public static final boolean DEV = true;

	public static void debug(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase().concat("-debug"), Level.INFO, message);
	}
	public static void log(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase(), Level.INFO, message);
	}
}