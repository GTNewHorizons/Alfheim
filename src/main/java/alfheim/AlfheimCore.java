package alfheim;

import static alfheim.api.ModInfo.*;

import java.io.File;

import alfheim.api.ModInfo;
import alfheim.common.core.command.*;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.InfoLoader;
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig;
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimConfig;
import alfheim.common.integration.travellersgear.TravellersGearAlfheimConfig;
import alfheim.common.integration.waila.WAILAAlfheimConfig;
import alfheim.common.network.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import vazkii.botania.common.Botania;

@Mod(modid = MODID,
	name = NAME,
	version = VERSION,
	guiFactory = MODID + ".client.gui.GUIFactory",
	dependencies = "required-after:Botania;before:elvenstory;after:MineTweaker3;after:Thaumcraft")

public class AlfheimCore {
	
	@Instance(MODID)
	public static AlfheimCore instance;
	
	@SidedProxy(clientSide = MODID + ".client.core.proxy.ClientProxy", serverSide = MODID + ".common.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
	public static int nextPacketID = 0;
	
	public static String save = "";
	
	public static boolean enableElvenStory = true;
	public static boolean enableMMO = true;
	public static boolean MineTweakerLoaded = false;
	public static boolean TravellersGearLoaded = false;
	public static boolean WAILALoaded = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		AlfheimConfig.readModes();
		MineTweakerLoaded = Loader.isModLoaded("MineTweaker3");
		TravellersGearLoaded = Loader.isModLoaded("TravellersGear");
		WAILALoaded = Loader.isModLoaded("Waila");
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		AlfheimConfig.loadConfig(new File(e.getModConfigurationDirectory(), ModInfo.NAME + ".cfg"));
		
		if (AlfheimConfig.info) InfoLoader.start();
		
		registerPackets();
		
		proxy.initializeAndRegisterHandlers();
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) { 
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerKeyBinds();
		proxy.registerRenderThings();
		proxy.postInit();
		AlfheimRegistry.loadAllPinkStuff();
		if (MineTweakerLoaded) MinetweakerAlfheimConfig.loadConfig();
		if (Botania.thaumcraftLoaded) ThaumcraftAlfheimConfig.loadConfig();
		if (TravellersGearLoaded) TravellersGearAlfheimConfig.loadConfig();
		if (WAILALoaded) WAILAAlfheimConfig.loadConfig();
	}
	
	@EventHandler
	public void starting(FMLServerStartingEvent event) {
		save = event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath();
		if (enableElvenStory) AlfheimConfig.initWorldCoordsForElvenStory(save);
		CardinalSystem.load(save);
		event.registerServerCommand(new CommandAlfheim());
		event.registerServerCommand(new CommandDimTP());
		event.registerServerCommand(new CommandRace());
	}
	
	@EventHandler
	public void stopping(FMLServerStoppingEvent event) {
		CardinalSystem.save(save);
	}
	
	public static void registerPackets() {
		AlfheimCore.network.registerMessage(Message0d.Handler.class,		Message0d.class,		nextPacketID++, Side.SERVER);
		AlfheimCore.network.registerMessage(MessageHotSpellS.Handler.class,	MessageHotSpellS.class,	nextPacketID++, Side.SERVER);
		AlfheimCore.network.registerMessage(MessageKeyBind.Handler.class,	MessageKeyBind.class,	nextPacketID++, Side.SERVER);
		
		AlfheimCore.network.registerMessage(Message1d.Handler.class,		Message1d.class,		nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(Message2d.Handler.class,		Message2d.class,		nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(Message3d.Handler.class,		Message3d.class,		nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageEffect.Handler.class,	MessageEffect.class,	nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageHotSpellC.Handler.class,	MessageHotSpellC.class,	nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageParticles.Handler.class,	MessageParticles.class,	nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageParty.Handler.class,		MessageParty.class,		nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageTileItem.Handler.class,	MessageTileItem.class,	nextPacketID++, Side.CLIENT);
		AlfheimCore.network.registerMessage(MessageTimeStop.Handler.class,	MessageTimeStop.class,	nextPacketID++, Side.CLIENT);
	}
	
	public static CreativeTabs alfheimTab = new CreativeTabs("Alfheim") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal);
		}
	}.setNoTitle().setBackgroundImageName("Alfheim.png");
}
