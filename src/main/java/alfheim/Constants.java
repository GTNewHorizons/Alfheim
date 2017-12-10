package alfheim;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class Constants {
	public static final String major_version = "pre";
	public static final String minor_version = "ALPHA";
	public static final String build_version = "1";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version + "." + minor_version + "-" + build_version;
	
	public static final boolean DEV = true;
	public static final int WE_ID = 255;

	public static final IAttribute RACE;
	public static final IAttribute FLIGHT;
	
	static {
		RACE = new BaseAttribute(Constants.MODID.toUpperCase() + ":RACE", 0) {
			@Override
			public double clampValue(double d) {
				return d;
			}
		}.setShouldWatch(true);
		
		FLIGHT = new BaseAttribute(Constants.MODID.toUpperCase() + ":FLIGHT", 1200) { 
			
			@Override
			public double clampValue(double d) {
				return Math.max(0, Math.min(12000, d));
			}
		}.setShouldWatch(true);
	}
	
	public static void debug(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase().concat("-debug"), Level.INFO, message);
	}
	public static void log(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase(), Level.INFO, message);
	}
}