package alfheim.common.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AlfheimConfig extends Configuration {

	// DIMENSIONS
	public static int dimensionIDAlfheim;
	public static boolean enableAlfheimRespawn;
	
	// Elven Story
	public static Vec3[] zones = new Vec3[9];

	public static void syncConfig() {
		List<String> propOrder = new ArrayList<String>();
		try {
			Property prop;

			prop = AlfheimCore.config.get("alfheim", "dimensionIDAlfheim", -105);
			prop.comment = "Dimension ID for Alfheim";
			prop.setLanguageKey("alfheim.configgui.dimensionIDAlfheim").setRequiresMcRestart(true);
			dimensionIDAlfheim = prop.getInt();
			propOrder.add(prop.getName());
			
			prop = AlfheimCore.config.get("alfheim", "enableAlfheimRespawn", true);
			prop.comment = "Set this to false to disable respawning in Alfheim";
			prop.setLanguageKey("alfheim.configgui.dimensionIDAlfheim").setRequiresMcRestart(false);
			enableAlfheimRespawn = prop.getBoolean();
			propOrder.add(prop.getName());

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
		return new String(MathHelper.floor_double(v.xCoord) + " : 64 : " + MathHelper.floor_double(v.zCoord) + "\n");
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