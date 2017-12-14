package elvenstory;

@cpw.mods.fml.common.Mod(dependencies = "required-after:alfheim", modid = "elvenstory", name = "Elven Story", version = "$$$")
public class ElvenStoryCore {
	
	@cpw.mods.fml.common.Mod.EventHandler
	public void preInit(cpw.mods.fml.common.event.FMLPreInitializationEvent e) {
		cpw.mods.fml.relauncher.FMLRelaunchLog.log("ELVENSTORY", org.apache.logging.log4j.Level.INFO, "Congradulations! You've successfully installed Elven Story! Let's get ready to rumble!");
	}
}
