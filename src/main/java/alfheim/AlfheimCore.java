package alfheim;

import java.io.IOException;

import alfheim.common.command.CommandRace;
import alfheim.common.network.KeyBindMessage;
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
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
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

	public static SimpleNetworkWrapper network;
	
	public static boolean enableElvenStory = false;
	public static Configuration config;
	
	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(Constants.MODID)) AlfheimConfig.syncConfig();
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
		network.registerMessage(KeyBindMessage.Handler.class, KeyBindMessage.class, 0, Side.SERVER);
		
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

    @EventHandler
	public void starting(FMLServerStartingEvent event) throws IOException {
    	AlfheimConfig.initWorldCoordsForElvenStory(event.getServer().getEntityWorld());
    	event.registerServerCommand(new CommandRace());
	}

    public static CreativeTabs alfheimTab = new CreativeTabs("Alfheim") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal);
		}
	};
}
