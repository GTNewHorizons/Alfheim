package alfheim.api;

public class ModInfo {
	
	public static final String MAJOR = "BETA";
	//public static final String MINOR = "";
	public static final String BUILD = "pre17.4";
	
	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = MAJOR /*+ "." + MINOR*/ + "-" + BUILD;
	
	public static boolean OBF;
	public static final boolean DEV = !OBF;
}