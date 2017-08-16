package alfheim;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
		   modid = AlfheimCore.MODID,
		   version = AlfheimCore.VERSION,
		   dependencies = "required-after:Botania;",
		   name = AlfheimCore.MODID
		)
public class AlfheimCore {

	public static final int major_version = 0;
	public static final int minor_version = 0;
	public static final int build_version = 1;
	
    public static final String MODID = "Alfheim";
    public static final String VERSION = major_version + "." + minor_version + "." + build_version;
    public static final String ASSET_PREFIX = "alfheim";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    { 
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
}
