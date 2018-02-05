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

public class ModInfo {
	public static final String major_version = "BETA";
	//public static final String minor_version = "";
	public static final String build_version = "4";

	public static final String MODID = "alfheim";
	public static final String NAME = "Alfheim";
	public static final String VERSION = major_version /*+ "." + minor_version*/ + "-" + build_version;
	
	public static final boolean DEV = true;
}