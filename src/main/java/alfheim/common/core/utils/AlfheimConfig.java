package alfheim.common.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AlfheimConfig extends Configuration {

	// DIMENSIONS
	public static int dimensionIDAlfheim;
	public static int biomeIDAlfheim;
	public static int biomeIDAlfheimBeach;
	
	// OHTER
	public static boolean destroyPortal;
	public static boolean enableAlfheimRespawn;
	public static boolean showNumbersInTooltip;
	
	// Elven Story
	public static Vec3[] zones = new Vec3[9];
	public static boolean enableElvenStory;
	public static boolean prolongDeathScreen;
	public static int deathScreenAdditionalTime;
	public static int flightTime;
	public static boolean enableWingsNonAlfheim;
	
	public static double deathTimerScale;
	public static int deathTimerX;
	public static int deathTimerY;
	public static int deathTimerFontX;
	public static int deathTimerFontY;

	public static void syncConfig() {
		List<String> propOrder = new ArrayList<String>();
		try {
			Property prop;

			prop = AlfheimCore.config.get("Dimensions", "biomeIDAlfheim", 152);
			prop.comment = "Biome ID for standart biome";
			prop.setLanguageKey("alfheim.configgui.biomeIDAlfheim").setRequiresMcRestart(true);
			biomeIDAlfheim = prop.getInt();
			propOrder.add(prop.getName());

			prop = AlfheimCore.config.get("Dimensions", "biomeIDAlfheimBeach", 153);
			prop.comment = "Biome ID for beach biome";
			prop.setLanguageKey("alfheim.configgui.biomeIDAlfheimBeach").setRequiresMcRestart(true);
			biomeIDAlfheimBeach = prop.getInt();
			propOrder.add(prop.getName());

			prop = AlfheimCore.config.get("Dimensions", "dimensionIDAlfheim", -105);
			prop.comment = "Dimension ID for Alfheim";
			prop.setLanguageKey("alfheim.configgui.dimensionIDAlfheim").setRequiresMcRestart(true);
			dimensionIDAlfheim = prop.getInt();
			propOrder.add(prop.getName());

			prop = AlfheimCore.config.get("Other", "destroyPortal", true);
			prop.comment = "Set this to false to disable destroying portals in non-zero coords in Alfheim";
			prop.setLanguageKey("alfheim.configgui.destroyPortal").setRequiresMcRestart(false);
			destroyPortal = prop.getBoolean();
			propOrder.add(prop.getName());
			
			prop = AlfheimCore.config.get("Other", "enableAlfheimRespawn", true);
			prop.comment = "Set this to false to disable respawning in Alfheim";
			prop.setLanguageKey("alfheim.configgui.enableAlfheimRespawn").setRequiresMcRestart(false);
			enableAlfheimRespawn = prop.getBoolean();
			propOrder.add(prop.getName());
			
			prop = AlfheimCore.config.get("Other", "showNumbersInTooltip", false);
			prop.comment = "Set this to true to enable numerical mana representation";
			prop.setLanguageKey("alfheim.configgui.showNumbersInTooltip").setRequiresMcRestart(false);
			showNumbersInTooltip = prop.getBoolean();
			propOrder.add(prop.getName());
			
			prop = AlfheimCore.config.get("Elven Story", "enableElvenStory", false);
			prop.comment = "Set this to true to enable Elven Story mode without mod";
			prop.setLanguageKey("alfheim.configgui.enableElvenStory").setRequiresMcRestart(false);
			enableElvenStory = prop.getBoolean();
			propOrder.add(prop.getName());
			
			if (Loader.isModLoaded("elvenstory") || enableElvenStory) {
				prop = AlfheimCore.config.get("Elven Story", "prolongDeathScreen", true);
				prop.comment = "Set this to false to disable death screen prolongation";
				prop.setLanguageKey("alfheim.configgui.prolongDeathScreen").setRequiresMcRestart(false);
				prolongDeathScreen = prop.getBoolean();
				propOrder.add(prop.getName());
				
				prop = AlfheimCore.config.get("Elven Story", "deathScreenAdditionalTime", 1200);
				prop.comment = "How longer (in ticks) \"Respawn\" button will be unavailable";
				prop.setLanguageKey("alfheim.configgui.deathScreenAdditionalTime").setRequiresMcRestart(false);
				deathScreenAdditionalTime = prop.getInt();
				propOrder.add(prop.getName());
				
				prop = AlfheimCore.config.get("Elven Story", "flightTime", 12000);
				prop.comment = "How long can you fly as elf";
				prop.setLanguageKey("alfheim.configgui.flightTime").setRequiresMcRestart(true);
				flightTime = prop.getInt();
				propOrder.add(prop.getName());
				
				prop = AlfheimCore.config.get("Elven Story", "enableWingsNonAlfheim", true);
				prop.comment = "Set this to false to disable wings in other worlds";
				prop.setLanguageKey("alfheim.configgui.enableWingsNonAlfheim").setRequiresMcRestart(false);
				enableWingsNonAlfheim = prop.getBoolean();
				propOrder.add(prop.getName());
				
				{
					prop = AlfheimCore.config.get("Death Timer", "deathTimerScale", 1.0);
					prop.comment = "Death Timer Scale (1 < bigger; 1 > smaller)";
					prop.setLanguageKey("alfheim.configgui.deathTimerScale").setRequiresMcRestart(false);
					deathTimerScale = prop.getDouble();
					propOrder.add(prop.getName());
		
					prop = AlfheimCore.config.get("Death Timer", "deathTimerX", 0);
					prop.comment = "Death Timer additional X from center";
					prop.setLanguageKey("alfheim.configgui.deathTimerX").setRequiresMcRestart(false);
					deathTimerX = prop.getInt();
					propOrder.add(prop.getName());
		
					prop = AlfheimCore.config.get("Death Timer", "deathTimerY", 0);
					prop.comment = "Death Timer additional Y from bottom";
					prop.setLanguageKey("alfheim.configgui.deathTimerY").setRequiresMcRestart(false);
					deathTimerY = prop.getInt();
					propOrder.add(prop.getName());
		
					prop = AlfheimCore.config.get("Death Timer", "deathTimerFontX", 0);
					prop.comment = "Death Timer text additional X from center";
					prop.setLanguageKey("alfheim.configgui.deathTimerFontX").setRequiresMcRestart(false);
					deathTimerFontX = prop.getInt();
					propOrder.add(prop.getName());
		
					prop = AlfheimCore.config.get("Death Timer", "deathTimerFontY", 0);
					prop.comment = "Death Timer text additional Y from bottom";
					prop.setLanguageKey("alfheim.configgui.deathTimerFontY").setRequiresMcRestart(false);
					deathTimerFontY = prop.getInt();
					propOrder.add(prop.getName());
				}
			}
			
			if (AlfheimCore.config.hasChanged()) {
				AlfheimCore.config.save();
			}
		} catch (final Exception e) {
			FMLLog.log(Level.ERROR, e, Constants.NAME + " has a problem loading it's config");
			FMLCommonHandler.instance().exitJava(-1, false);
		}
	}
	
	public static void initWorldCoordsForElvenStory(World world) throws IOException {
		File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/data/AlfheimCoords.txt"); 
		if (!f.exists()) makeDefaultWorldCoords(world);
		
		BufferedReader fin = new BufferedReader(new FileReader(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/data/AlfheimCoords.txt"));
		for (int i = 0; i < zones.length; i++) {
			fin.readLine();
			zones[i] = makeVectorFromString(fin.readLine());
		}
		fin.close();
	}

	private static void makeDefaultWorldCoords(World world) throws IOException {
		FileWriter fout = new FileWriter(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/data/AlfheimCoords.txt");
		
		StringBuilder s = new StringBuilder();
		double angle = 0;
		s.append("Salamander start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle));
		s.append("Sylph start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Cait Sith start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Puca start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Gnome start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Leprechaun start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Spriggan start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Undine start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		s.append("Imp start city and players spawnpoint coords:\n");
		s.append(writeStandardCoords(angle += 40));
		fout.write(s.toString());
		fout.close();
	}
	
	private static String writeStandardCoords(double angle) {
		Vec3 v = mkVecLenRotMine(1000, angle);
		return new String(MathHelper.floor_double(v.xCoord) + " : 300 : " + MathHelper.floor_double(v.zCoord) + "\n");
	}
	
	private static Vec3 makeVectorFromString(String s) {
		String[] ss = s.split(" : ");
		if (ss.length != 3) throw new IllegalArgumentException("Something went wrong in sourcecode, contact author");
		return Vec3.createVectorHelper(Integer.valueOf(ss[0]).intValue(), Integer.valueOf(ss[1]).intValue(), Integer.valueOf(ss[2]).intValue());
	}
	
	private static Vec3 mkVecLenRotMine(double length, double angle) {
		return makeVectorOfLengthRotated(length, angle + 90);
	}
	
	private static Vec3 makeVectorOfLengthRotated(double length, double angle) {
		return Vec3.createVectorHelper(Math.cos(Math.toRadians(angle)) * length, 64, Math.sin(Math.toRadians(angle)) * length);
	}
}