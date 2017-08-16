package alfheim;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import alfheim.common.proxy.CommonProxy;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.utils.AlfheimCreativeTabs;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;


@Mod(
		   modid = ModInfo.MODID,
		   name = ModInfo.NAME,
		   version = ModInfo.VERSION,
		   guiFactory = ModInfo.MODID + ".client.gui.GUIFactory",
		   dependencies = "required-after:Botania;"
		)
public class AlfheimCore {

	@Instance(ModInfo.MODID)
	public static AlfheimCore instance;

	@SidedProxy(clientSide = ModInfo.MODID + ".client.proxy.ClientProxy", serverSide = ModInfo.MODID + ".common.proxy.CommonProxy")
	public static CommonProxy proxy;
    
	public static Configuration config;
	
	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(ModInfo.MODID)) {
			AlfheimConfig.syncConfig();
		}
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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
