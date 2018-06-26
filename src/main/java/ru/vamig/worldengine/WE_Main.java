//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = WE_Main.MODID, name = WE_Main.NAME, version = WE_Main.VERSION)
public class WE_Main {
	public static final String
		MODID   = "WorldEngine",
		NAME	= "WorldEngine",
		VERSION = "1.1710.0"   ;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println("////////////////////////////////////-"										);
		System.out.println("//#===============================//=* Version: " + VERSION + "."			);
		System.out.println("//#=-------| WorldEngine |-------=//=* By Vamig Aliev (vk.com/win_vista)."	);
		System.out.println("//#===============================//=* Part of VamigA_core (vk.com/vamiga).");
		System.out.println("////////////////////////////////////-"										);
	}
}