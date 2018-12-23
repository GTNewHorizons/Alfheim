package alfheim.api;

import java.lang.reflect.Field;

import alexsocol.asjlib.ASJReflectionHelper;
import cpw.mods.fml.relauncher.CoreModManager;

public class ModInfo {
	
	public static final String MAJOR = "BETA";
	//public static final String MINOR = "";
	public static final String BUILD = "pre8.36";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = MAJOR /*+ "." + MINOR*/ + "-" + BUILD;
	
	public static boolean OBF;
	public static final boolean DEV = true; // !OBF 
}