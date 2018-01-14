package alfheim;

import java.io.IOException;

import alfheim.common.core.command.CommandRace;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.network.KeyBindMessage;
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
import vazkii.botania.client.core.handler.BotaniaPlayerController;
import vazkii.botania.common.core.handler.ConfigHandler;


@Mod(modid = Constants.MODID,
	name = Constants.NAME,
	version = Constants.VERSION,
	guiFactory = Constants.MODID + ".client.gui.GUIFactory",
	dependencies = "required-after:Botania;before:elvenstory")

public class AlfheimCore {

	@Instance(Constants.MODID)
	public static AlfheimCore instance;

	@SidedProxy(clientSide = Constants.MODID + ".client.core.proxy.ClientProxy", serverSide = Constants.MODID + ".common.core.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	
	public static boolean enableElvenStory = false;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
		network.registerMessage(KeyBindMessage.Handler.class, KeyBindMessage.class, 0, Side.SERVER);
		
    	proxy.initializeAndRegisterHandlers();
    	proxy.preInit(e);
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
    	if (AlfheimCore.enableElvenStory) {
    		AlfheimConfig.initWorldCoordsForElvenStory(event.getServer().getEntityWorld());
    		event.registerServerCommand(new CommandRace());
    	}
	}

    public static CreativeTabs alfheimTab = new CreativeTabs("Alfheim") {
    	@Override
    	public Item getTabIconItem() {
    		return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal);
    	}
    }.setNoTitle().setBackgroundImageName("Alfheim.png");
}
