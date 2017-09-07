package alfheim;

import alfheim.common.proxy.CommonProxy;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;


@Mod(modid = Constants.MODID,
	name = Constants.NAME,
	version = Constants.VERSION,
	guiFactory = Constants.MODID + ".client.gui.GUIFactory",
	dependencies = "required-after:Botania;")

public class AlfheimCore {

	@Instance(Constants.MODID)
	public static AlfheimCore instance;

	@SidedProxy(clientSide = Constants.MODID + ".client.proxy.ClientProxy", serverSide = Constants.MODID + ".common.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static boolean enableElvenStory = false;
	public static Configuration config;
	
	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(Constants.MODID)) {
			AlfheimConfig.syncConfig();
		}
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	enableElvenStory = Loader.isModLoaded("elvenstory");
    	
		FMLCommonHandler.instance().bus().register(AlfheimCore.instance);

		config = new Configuration(event.getSuggestedConfigurationFile());
		AlfheimConfig.syncConfig();
		
    	proxy.initializeAndRegisterHandlers();
    	proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) { 
    	proxy.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit();
    	proxy.registerKeyBinds();
    	proxy.registerRenderThings();
    }
    
    public static CreativeTabs alfheimTab = new CreativeTabs("Alfheim") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal);
		}
	};
}
