package alfheim;

import java.io.IOException;

import alfheim.api.ModInfo;
import alfheim.common.core.command.CommandDimTP;
import alfheim.common.core.command.CommandRace;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


@Mod(modid = ModInfo.MODID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	guiFactory = ModInfo.MODID + ".client.gui.GUIFactory",
	dependencies = "required-after:Botania;before:elvenstory")

public class AlfheimCore {

	@Instance(ModInfo.MODID)
	public static AlfheimCore instance;

	@SidedProxy(clientSide = ModInfo.MODID + ".client.core.proxy.ClientProxy", serverSide = ModInfo.MODID + ".common.core.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	
	public static boolean enableElvenStory = false;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	enableElvenStory = Loader.isModLoaded("elvenstory");
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
		AlfheimConfig.loadConfig(e.getSuggestedConfigurationFile());

		proxy.registerPackets();
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
		AlfheimRegistry.loadAllPinkStuff();
    }

    @EventHandler
	public void starting(FMLServerStartingEvent event) throws IOException {
    	if (AlfheimCore.enableElvenStory) {
    		AlfheimConfig.initWorldCoordsForElvenStory(event.getServer().getEntityWorld());
    		event.registerServerCommand(new CommandRace());
    		event.registerServerCommand(new CommandDimTP());
    	}
	}

    public static CreativeTabs alfheimTab = new CreativeTabs("Alfheim") {
    	@Override
    	public Item getTabIconItem() {
    		return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal);
    	}
    }.setNoTitle().setBackgroundImageName("Alfheim.png");
}
