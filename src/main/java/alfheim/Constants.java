package alfheim;

import org.apache.logging.log4j.Level;

import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class Constants {
	public static final String major_version = "BETA";
	//public static final String minor_version = "";
	public static final String build_version = "4";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version /*+ "." + minor_version*/ + "-" + build_version;
	
	public static final boolean DEV = true;

	public static final IAttribute RACE = new BaseAttribute(Constants.MODID.toUpperCase() + ":RACE", 0) {
		@Override
		public double clampValue(double d) {
			return d;
		}
	}.setShouldWatch(true);
	
	public static final IAttribute FLIGHT = new BaseAttribute(Constants.MODID.toUpperCase() + ":FLIGHT", AlfheimConfig.flightTime) { 
		
		@Override
		public double clampValue(double d) {
			return Math.max(0, Math.min(AlfheimConfig.flightTime, d));
		}
	}.setShouldWatch(true);
	
	public static void debug(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase().concat("-debug"), Level.INFO, message);
	}
	public static void log(String message) { 
		FMLRelaunchLog.log(NAME.toUpperCase(), Level.INFO, message);
	}
	public static void chatLog(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	public static void chatLog(String message, World world) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((world.isRemote ? "[CLIENT] " : "[SERVER] ") + message));
	}
	public static void say(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(message)));
	}
}