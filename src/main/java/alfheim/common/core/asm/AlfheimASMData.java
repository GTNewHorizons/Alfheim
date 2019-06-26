package alfheim.common.core.asm;

import alfheim.api.ModInfo;
import cpw.mods.fml.relauncher.FMLRelaunchLog;

import java.io.*;
import java.util.Arrays;

public class AlfheimASMData {
	
	private static final int[] DATA = { 1, 1, 22 };
	
	static void load() {
		new File("config/Alfheim").mkdirs();
		instructions();
		
		File f = new File("config/Alfheim/ASMData.cfg"); 
		if (!f.exists()) create();
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String[] flags = br.readLine().split(" ");
			int i = 0;
			
			for (String flag : flags) 
				DATA[i++] = Integer.parseInt(flag);
			
			br.close();
			fr.close();
		} catch (Exception e) {
			FMLRelaunchLog.severe(String.format("[%s] Something went wrong while loading Alfheim ASM data", ModInfo.MODID.toUpperCase()));
			e.printStackTrace();
			throw new IllegalArgumentException("Incorrect ASM data");
		}
		
		configure();
	}
	
	private static void configure() {
		if (DATA[0] == 0) {
			AlfheimHookLoader.hpSpells = false;
			FMLRelaunchLog.warning(String.format("[%s] Alfheim is configured to disable hooks into health system, thing may not go well: Shared HP spell is unavailable, \"leftover flame\" of other players can somehow be \"healed\" (seems to lead to nothing).", ModInfo.MODID.toUpperCase()));
		}
		
		if (DATA[1] == 0) {
			AlfheimHookLoader.isThermos = true;
			FMLRelaunchLog.warning(String.format("[%s] Alfheim is configured to disable some features to provide compatibility with Thermos Core - thing may not go well", ModInfo.MODID.toUpperCase()));
		}
		
		FMLRelaunchLog.info(String.format("[%s] In case Thaumcraft will be loaded, Alfheim is configured to set Elementium Cluster metadata value to 22", ModInfo.MODID.toUpperCase()));
	}
	
	private static void instructions() {
		File f = new File("config/Alfheim/ASMDataInstruction.txt");
		
		try {
			FileWriter fw = new FileWriter(f);
			fw.write("This is instruction for configuring Alfheim ASM Data in ASMData.cfg\n");
			fw.write(String.format("There will be some numbers, separated with SPACE (%s by default). Those are:\n", Arrays.toString(DATA)));
			fw.write("1) Health ASM - toggles hooks to vanilla health system. Disable this if you have any issues with other systems (0 - off, 1 - on)\n");
			fw.write("2) World  ASM - toggles hooks to world class. Disable this if you have some world-related crash (or running Thermos Core) (0 - off, 1 - on)\n");
			fw.write("3) Elementium Cluster meta - Effective only if Thaumcraft is installed. Change this if some other mod adds own clusters (max value is 63); also edit and spread modified .lang files\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void create() {
		File f = new File("config/Alfheim/ASMData.cfg");
		
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(Arrays.toString(DATA).replaceAll(",", "").replaceAll("\\[", "").replaceAll("]", ""));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int elementiumClusterMeta() {
		return DATA[2];
	}
}