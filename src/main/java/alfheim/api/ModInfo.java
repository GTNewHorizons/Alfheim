package alfheim.api;

import java.lang.reflect.Field;

import alexsocol.asjlib.ASJReflectionHelper;
import cpw.mods.fml.relauncher.CoreModManager;

public class ModInfo {
	public static final String MAJOR = "BETA";
	//public static final String MINOR = "";
	public static final String BUILD = "6";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = MAJOR /*+ "." + MINOR*/ + "-" + BUILD;
	
	public static final boolean DEV;
	
	static {
		DEV = (Boolean) ASJReflectionHelper.getStaticValue(CoreModManager.class, "deobfuscatedEnvironment");
	}
}