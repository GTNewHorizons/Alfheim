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
import alfheim.api.ModInfo;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AlfheimConfig extends Configuration {

	public static Configuration config;
	
	public static final String CATEGORY_DIMENSION	= CATEGORY_GENERAL + CATEGORY_SPLITTER + "dimension";
	public static final String CATEGORY_ESMODE	= CATEGORY_GENERAL + CATEGORY_SPLITTER + "elvenstory";
	public static final String CATEGORY_HUD			= CATEGORY_ESMODE + CATEGORY_SPLITTER + "hud";
	
	// DIMENSIONS
	public static int		biomeIDAlfheim			= 152;
	public static int		biomeIDAlfheimBeach		= 153;
	public static int		dimensionIDAlfheim		= -105;
	
	// OHTER
	public static boolean	destroyPortal			= true;
	public static boolean	enableAlfheimRespawn	= true;
	public static boolean	fancies					= true;
	public static boolean	looniumOverseed			= false;
	public static boolean	numericalMana			= true;
	public static int		potionIDPossession		= 30;
	public static int		potionIDSoulburn		= 31;
	
	// Elven Story
	public static boolean	bothSpawnStructures		= false;
	public static int		deathScreenAddTime		= 1200;
	public static boolean	enableWingsNonAlfheim	= true;
	public static int		flightTime				= 12000;
	public static boolean	prolongDeathScreen		= true;
	public static Vec3[]	zones					= new Vec3[9];

	// ES - HUD
	public static double	deathTimerScale			= 1.0;
	public static int		deathTimerX				= 0;
	public static int		deathTimerY				= 0;
	public static double	flightTimerScale		= 1.0;
	public static int		flightTimerX			= 0;
	public static int		flightTimerY			= 0;
	
	public static void loadConfig(File suggestedConfigurationFile) {
		config = new Configuration(suggestedConfigurationFile);

		config.load();
		config.addCustomCategoryComment(CATEGORY_DIMENSION, "Alfheim dimension settings");
		config.addCustomCategoryComment(CATEGORY_ESMODE,	"Elven Story optional features");
		config.addCustomCategoryComment(CATEGORY_HUD, "Coordinates and sizes of HUD elements");
		
		syncConfig();
		FMLCommonHandler.instance().bus().register(new AlfheimChangeListener());
	}
	
	public static void syncConfig() {
		biomeIDAlfheim		= loadProp(CATEGORY_DIMENSION,	"biomeIDAlfheim",		biomeIDAlfheim,			true,	"Biome ID for standart biome");
		biomeIDAlfheimBeach	= loadProp(CATEGORY_DIMENSION,	"biomeIDAlfheimBeach",	biomeIDAlfheimBeach,	true,	"Biome ID for beach biome");
		dimensionIDAlfheim	= loadProp(CATEGORY_DIMENSION,	"dimensionIDAlfheim",	dimensionIDAlfheim,		true,	"Dimension ID for Alfheim");
		destroyPortal		= loadProp(CATEGORY_GENERAL,	"destroyPortal",		destroyPortal,			false,	"Set this to false to disable destroying portals in non-zero coords in Alfheim");
		enableAlfheimRespawn= loadProp(CATEGORY_GENERAL,	"enableAlfheimRespawn",	enableAlfheimRespawn,	false,	"Set this to false to disable respawning in Alfheim");
		fancies				= loadProp(CATEGORY_GENERAL,	"fancies",				fancies,				false,	"Set this to false to disable fancies rendering on you ([CLIENTSIDE] for contributors only)");
		looniumOverseed		= loadProp(CATEGORY_GENERAL,	"looniumOverseed",		looniumOverseed,		true,	"Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)");
		numericalMana		= loadProp(CATEGORY_GENERAL,	"numericalMana",		numericalMana,			false,	"Set this to true to enable numerical mana representation");
		potionIDPossession	= loadProp(CATEGORY_GENERAL, 	"potionIDPossession",	potionIDPossession,		true,	"Potion id for Possession");
		potionIDSoulburn	= loadProp(CATEGORY_GENERAL, 	"potionIDSoulburn",		potionIDSoulburn,		true,	"Potion id for Soulburn");
		
		if (AlfheimCore.enableElvenStory) {
			bothSpawnStructures		= loadProp(CATEGORY_ESMODE,	"bothSpawnStructures",		bothSpawnStructures,	false,	"Set this to true to generate both cube and castle (!contains portal!) on zero coords of Alfheim");
			deathScreenAddTime		= loadProp(CATEGORY_ESMODE,	"deathScreenAdditionalTime",deathScreenAddTime,		false,	"How longer (in ticks) \"Respawn\" button will be unavailable");
			enableWingsNonAlfheim	= loadProp(CATEGORY_ESMODE,	"enableWingsNonAlfheim",	enableWingsNonAlfheim,	false,	"Set this to false to disable wings in other worlds");
			flightTime				= loadProp(CATEGORY_ESMODE,	"flightTime",				flightTime,				true,	"How long can you fly as elf");
			prolongDeathScreen		= loadProp(CATEGORY_ESMODE,	"prolongDeathScreen",		prolongDeathScreen,		false,	"Set this to false to disable death screen prolongation");
			
			deathTimerScale			= loadProp(CATEGORY_HUD,	"deathTimerScale",	deathTimerScale,	false,	"Death Timer Scale (1 < bigger; 1 > smaller)");
			deathTimerX				= loadProp(CATEGORY_HUD,	"deathTimerX",		deathTimerX,		false,	"Death Timer additional X");
			deathTimerY				= loadProp(CATEGORY_HUD,	"deathTimerY",		deathTimerY,		false,	"Death Timer additional Y");
			
			flightTimerScale		= loadProp(CATEGORY_HUD,	"flightTimerScale",	flightTimerScale,	false,	"Flight Timer Scale (1 < bigger; 1 > smaller)");
			flightTimerX			= loadProp(CATEGORY_HUD,	"flightTimerX",		flightTimerX,		false,	"Flight Timer additional X");
			flightTimerY			= loadProp(CATEGORY_HUD,	"flightTimerY",		flightTimerY,		false,	"Flight Timer additional Y");
		}

		if (config.hasChanged()) {
			config.save();
		}

	}
	
	public static int loadProp(String category, String propName, int default_, boolean restart, String desc) {
		Property prop = config.get(category, propName, default_);
		prop.comment = desc;
		prop.setRequiresMcRestart(restart);
		return prop.getInt(default_);
	}
	
	public static double loadProp(String category, String propName, double default_, boolean restart, String desc) {
		Property prop = config.get(category, propName, default_);
		prop.comment = desc;
		prop.setRequiresMcRestart(restart);
		return prop.getDouble(default_);
	}

	public static boolean loadProp(String category, String propName, boolean default_, boolean restart, String desc) {
		Property prop = config.get(category, propName, default_);
		prop.comment = desc;
		prop.setRequiresMcRestart(restart);
		return prop.getBoolean(default_);
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
	
	public static class AlfheimChangeListener {
		@SubscribeEvent
		public void onConfigChanged(OnConfigChangedEvent e) {
			if(e.modID.equals(ModInfo.MODID)) syncConfig();
		}
	}
}