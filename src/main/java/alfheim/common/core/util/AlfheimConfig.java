package alfheim.common.core.util;

import java.awt.AlphaComposite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.asm.ASJPacketCompleter;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.asm.AlfheimSyntheticMethodsInjector;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AlfheimConfig extends Configuration {

	public static Configuration config;
	
	public static final String CATEGORY_DIMENSION	= CATEGORY_GENERAL	 + CATEGORY_SPLITTER + "dimension";
	public static final String CATEGORY_POTIONS		= CATEGORY_GENERAL	 + CATEGORY_SPLITTER + "potions";
	public static final String CATEGORY_ESMODE		= CATEGORY_GENERAL	 + CATEGORY_SPLITTER + "elvenstory";
	public static final String CATEGORY_MMO			= CATEGORY_ESMODE	 + CATEGORY_SPLITTER + "mmo";
	public static final String CATEGORY_HUD			= CATEGORY_MMO	 	+ CATEGORY_SPLITTER + "hud";
	
	// DIMENSIONS
	public static int		biomeIDAlfheim			= 152;
	public static int		dimensionIDAlfheim		= -105;
	
	// OHTER
	public static boolean	destroyPortal			= true;
	public static boolean	enableAlfheimRespawn	= true;
	public static boolean	fancies					= true;
	public static boolean	flugelBossBar			= true;
	public static boolean 	info					= true;
	public static boolean	looniumOverseed			= false;
	public static boolean	numericalMana			= true;
	public static int		oregenMultiplier		= 3;
	public static int		tradePortalRate			= 1200;
	
	// POTIONS
	public static int		potionID___COUNTER		= 30;
	public static int		potionIDBleeding		= potionID___COUNTER++;
	public static int		potionIDButterShield	= potionID___COUNTER++;
	public static int		potionIDDeathMark		= potionID___COUNTER++;
	public static int		potionIDDecay			= potionID___COUNTER++;
	public static int		potionIDGoldRush		= potionID___COUNTER++;
	public static int		potionIDIceLens			= potionID___COUNTER++;
	public static int		potionIDLeftFlame		= potionID___COUNTER++;
	public static int		potionIDNineLifes		= potionID___COUNTER++;
	public static int		potionIDNoclip			= potionID___COUNTER++;
	public static int		potionIDPossession		= potionID___COUNTER++;
	public static int		potionIDQuadDamage		= potionID___COUNTER++;
	public static int		potionIDSacrifice		= potionID___COUNTER++;
	public static int		potionIDSharedHP		= potionID___COUNTER++;
	public static int		potionIDShowMana		= potionID___COUNTER++;
	public static int		potionIDSoulburn		= potionID___COUNTER++;
	public static int		potionIDStoneSkin		= potionID___COUNTER++;
	public static int 		potionIDThrow			= potionID___COUNTER++;
	public static int 		potionIDWellOLife		= potionID___COUNTER++;
	
	// Elven Story
	public static boolean	bothSpawnStructures		= false;
	public static int		deathScreenAddTime		= 1200;
	public static boolean	enableWingsNonAlfheim	= true;
	public static int		flightTime				= 1200;
	public static boolean	frienldyFire			= false;
	public static int		maxPartyMembers			= 5;
	public static Vec3[]	zones					= new Vec3[9];

	// HUD
	public static double	partyHUDScale			= 1.0;
	public static boolean	selfHealthUI			= true;
	public static boolean	targethUI				= true;
	
	public static void loadConfig(File suggestedConfigurationFile) {
		config = new Configuration(suggestedConfigurationFile);

		config.load();
		config.addCustomCategoryComment(CATEGORY_DIMENSION, "Alfheim dimension settings");
		config.addCustomCategoryComment(CATEGORY_ESMODE,	"Elven Story optional features");
		config.addCustomCategoryComment(CATEGORY_HUD,		"HUD elements customizations");
		
		syncConfig();
		FMLCommonHandler.instance().bus().register(new AlfheimChangeListener());
	}
	
	public static void syncConfig() {
		biomeIDAlfheim			= loadProp(CATEGORY_DIMENSION,	"biomeIDAlfheim",			biomeIDAlfheim,			true,	"Biome ID for standart biome");
		dimensionIDAlfheim		= loadProp(CATEGORY_DIMENSION,	"dimensionIDAlfheim",		dimensionIDAlfheim,		true,	"Dimension ID for Alfheim");
		
		destroyPortal			= loadProp(CATEGORY_GENERAL,	"destroyPortal",			destroyPortal,			false,	"Set this to false to disable destroying portals in non-zero coords in Alfheim");
		enableAlfheimRespawn	= loadProp(CATEGORY_GENERAL,	"enableAlfheimRespawn",		enableAlfheimRespawn,	false,	"Set this to false to disable respawning in Alfheim");
		fancies					= loadProp(CATEGORY_GENERAL,	"fancies",					fancies,				false,	"Set this to false to disable fancies rendering on you ([CLIENTSIDE] for contributors only)");
		flugelBossBar			= loadProp(CATEGORY_GENERAL,	"flugelBossBar",			flugelBossBar,			false,	"Set this to false to disable displaying flugel's boss bar");
		info					= loadProp(CATEGORY_GENERAL,	"info",						info,					false,	"Set this to false to disable loading info about addon");
		looniumOverseed			= loadProp(CATEGORY_GENERAL,	"looniumOverseed",			looniumOverseed,		true,	"Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)");
		numericalMana			= loadProp(CATEGORY_GENERAL,	"numericalMana",			numericalMana,			false,	"Set this to false to disable numerical mana representation");
		oregenMultiplier		= loadProp(CATEGORY_GENERAL, 	"oregenMultiplier",			oregenMultiplier,		true,	"Multiplier for Alfheim oregen");
		tradePortalRate			= loadProp(CATEGORY_GENERAL, 	"tradePortalRate",			tradePortalRate,		false,	"Portal updates every {N} ticks");

		potionIDBleeding		= loadProp(CATEGORY_POTIONS, 	"potionIDBleeding",			potionIDBleeding,		true,	"Potion id for Bleeding");
		potionIDButterShield	= loadProp(CATEGORY_POTIONS, 	"potionIDButterShield",		potionIDButterShield,	true,	"Potion id for Butterfly Shield");
		potionIDDeathMark		= loadProp(CATEGORY_POTIONS, 	"potionIDDeathMark",		potionIDDeathMark,		true,	"Potion id for Death Mark");
		potionIDDecay			= loadProp(CATEGORY_POTIONS, 	"potionIDDecay",			potionIDDecay,			true,	"Potion id for Decay");
		potionIDGoldRush		= loadProp(CATEGORY_POTIONS, 	"potionIDGoldRush",			potionIDGoldRush,		true,	"Potion id for Gold Rush");
		potionIDIceLens			= loadProp(CATEGORY_POTIONS, 	"potionIDIceLens",			potionIDIceLens,		true,	"Potion id for Ice Lense");
		potionIDLeftFlame		= loadProp(CATEGORY_POTIONS, 	"potionIDLeftFlame",		potionIDLeftFlame,		true,	"Potion id for Leftover Flame");
		potionIDNoclip			= loadProp(CATEGORY_POTIONS, 	"potionIDNoclip",			potionIDNoclip,			true,	"Potion id for Noclip");
		potionIDPossession		= loadProp(CATEGORY_POTIONS, 	"potionIDPossession",		potionIDPossession,		true,	"Potion id for Possession");
		potionIDQuadDamage		= loadProp(CATEGORY_POTIONS, 	"potionIDQuadDamage",		potionIDQuadDamage,		true,	"Potion id for Quad Damage");
		potionIDSacrifice		= loadProp(CATEGORY_POTIONS, 	"potionIDSacrifice",		potionIDSacrifice,		true,	"Potion id for Sacrifice");
		potionIDSharedHP		= loadProp(CATEGORY_POTIONS, 	"potionIDSharedHP",			potionIDSharedHP,		true,	"Potion id for Shared Health Pool");
		potionIDShowMana		= loadProp(CATEGORY_POTIONS, 	"potionIDShowMana",			potionIDShowMana,		true,	"Potion id for Mana Showing Effect");
		potionIDSoulburn		= loadProp(CATEGORY_POTIONS, 	"potionIDSoulburn",			potionIDSoulburn,		true,	"Potion id for Soulburn");
		potionIDStoneSkin		= loadProp(CATEGORY_POTIONS, 	"potionIDStoneSkin",		potionIDStoneSkin,		true,	"Potion id for Stone Skin");
		potionIDThrow			= loadProp(CATEGORY_POTIONS, 	"potionIDThrow",			potionIDThrow,			true,	"Potion id for Throw");
		potionIDWellOLife		= loadProp(CATEGORY_POTIONS, 	"potionIDWellOLife",		potionIDWellOLife,		true,	"Potion id for Well'o'Life");
		
		bothSpawnStructures		= loadProp(CATEGORY_ESMODE,		"bothSpawnStructures",		bothSpawnStructures,	false,	"Set this to true to generate both cube and castle (!contains portal!) on zero coords of Alfheim");
		enableWingsNonAlfheim	= loadProp(CATEGORY_ESMODE,		"enableWingsNonAlfheim",	enableWingsNonAlfheim,	false,	"Set this to false to disable wings in other worlds");
		flightTime				= loadProp(CATEGORY_ESMODE,		"flightTime",				flightTime,				true,	"How long can you fly as elf");
		
		deathScreenAddTime		= loadProp(CATEGORY_MMO,		"deathScreenAdditionalTime",deathScreenAddTime,		false,	"How longer (in ticks) \"Respawn\" button will be unavailable");
		frienldyFire			= loadProp(CATEGORY_MMO,		"frienldyFire",				frienldyFire,			false,	"Set this to true to enable da,age to party members");
		maxPartyMembers			= loadProp(CATEGORY_MMO,		"maxPartyMembers",			maxPartyMembers,		false,	"How many people can be in single party at the same time");
		
		partyHUDScale			= loadProp(CATEGORY_HUD,		"partyHUDScale",			partyHUDScale,			false,	"Party HUD Scale (1 < bigger; 1 > smaller)");
		selfHealthUI			= loadProp(CATEGORY_HUD,		"selfHealthUI",				selfHealthUI,			false,	"Set this to false to hide player's healthbar");
		targethUI				= loadProp(CATEGORY_HUD,		"targethUI",				targethUI,				false,	"Set this to false to hide target's healthbar");

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
	
	public static void initWorldCoordsForElvenStory(String save) {
		File f = new File(save + "/data/AlfheimCoords.txt"); 
		if (!f.exists()) makeDefaultWorldCoords(save);
			
		try {
			FileReader fr = new FileReader(save + "/data/AlfheimCoords.txt");
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; i < zones.length; i++) {
				br.readLine();
				try {
					zones[i] = makeVectorFromString(br.readLine());
				} catch (IllegalArgumentException e) {
					br.close();
					fr.close();
					throw e;
				}
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			System.err.println("Unable to read Alfheim Coords data. Creating default...");
			e.printStackTrace();
			makeDefaultWorldCoords(save);
		}
	}

	private static void makeDefaultWorldCoords(String save) {
		try {
			FileWriter fw = new FileWriter(save + "/data/AlfheimCoords.txt");
			
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
			fw.write(s.toString());
			fw.close();
		} catch (IOException e) {
			ASJUtilities.error("Unable to generate default Alfheim Coords data. Setting all to [0, 300, 0]...");
			e.printStackTrace();
			
			for (int i = 0; i < zones.length; i++) {
				zones[i] = Vec3.createVectorHelper(0, 300, 0);
			}
		}
	}
	
	private static String writeStandardCoords(double angle) {
		Vec3 v = mkVecLenRotMine(1000, angle);
		return new String(MathHelper.floor_double(v.xCoord) + " : 300 : " + MathHelper.floor_double(v.zCoord) + "\n");
	}
	
	private static Vec3 makeVectorFromString(String s) {
		String[] ss = s.split(" : ");
		if (ss.length != 3) throw new IllegalArgumentException(String.format("Wrong coords count. Expected 3 got %d", ss.length));
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
	
	public static void readModes() {
		File f = new File("config/esm.cfg"); 
		if (!f.exists()) return;
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			String[] flags = br.readLine().split(" ");
			
			br.close();
			fr.close();
			
			if (flags.length != 2) throw new IllegalArgumentException(String.format("Wrong flags count. Expected 2 got %d", flags.length));
			
			if (flags[0].equals("false")) {
				if (flags[1].equals("false")) AlfheimCore.enableElvenStory = AlfheimCore.enableMMO = false;
				else throw new IllegalArgumentException(flags[1].equals("true") ? "Unable to enable MMO mode without ESM" : String.format("Unknown param value for MMO mode: %s", flags[1]));
			} else if (flags[0].equals("true")) {
				AlfheimCore.enableElvenStory = true;
				if (flags[1].equals("false")) AlfheimCore.enableMMO = false;
				else if (flags[1].equals("true")) AlfheimCore.enableMMO = true;
				else throw new IllegalArgumentException(String.format("Unknown param value for MMO mode: %s", flags[1]));
			} else throw new IllegalArgumentException(String.format("Unknown param value for ESM mode: %s", flags[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeModes() {
		if (!AlfheimCore.enableElvenStory && AlfheimCore.enableMMO) throw new IllegalArgumentException("Unable to write modes state when ESM is disabled and MMO is enabled");
		File f = new File("config/esm.cfg"); 
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(String.format("ESM mode, MMO mode (second requires first). Default \"true true\"\n%s %s", AlfheimCore.enableElvenStory ? "true" : "false", AlfheimCore.enableMMO ? "true" : "false"));
			fw.close();
		} catch (IOException e) {
			ASJUtilities.error("Unable to write modes state");
			e.printStackTrace();
		}
	}
}