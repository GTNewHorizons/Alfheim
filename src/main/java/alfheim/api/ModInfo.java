package alfheim.api;

import java.lang.reflect.Field;

import alexsocol.asjlib.ASJReflectionHelper;
import cpw.mods.fml.relauncher.CoreModManager;

public class ModInfo {
	public static final String major_version = "BETA";
	//public static final String minor_version = "";
	public static final String build_version = "BETA-pre5.25";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version /*+ "." + minor_version*/ + "-" + build_version;
	
	public static final boolean DEV;
	
	static {
		DEV = (Boolean) ASJReflectionHelper.getStaticValue(CoreModManager.class, "deobfuscatedEnvironment");
	}
}