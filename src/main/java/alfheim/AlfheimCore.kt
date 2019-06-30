package alfheim

import alexsocol.asjlib.command.CommandDimTP
import alfheim.api.ModInfo.MODID
import alfheim.api.ModInfo.NAME
import alfheim.api.ModInfo.VERSION
import alfheim.common.core.command.*
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.proxy.CommonProxy
import alfheim.common.core.registry.*
import alfheim.common.core.util.*
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig
import alfheim.common.integration.thaumcraft.*
import alfheim.common.integration.travellersgear.TravellersGearAlfheimConfig
import alfheim.common.integration.waila.WAILAAlfheimConfig
import alfheim.common.network.*
import cpw.mods.fml.common.*
import cpw.mods.fml.common.Mod.*
import cpw.mods.fml.common.event.*
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.relauncher.Side
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import vazkii.botania.common.Botania
import java.io.File

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = "$MODID.client.gui.GUIFactory", dependencies = "required-after:Botania;before:elvenstory;after:MineTweaker3;after:Thaumcraft")
class AlfheimCore {
	
	@EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		AlfheimConfig.readModes()
		MineTweakerLoaded = Loader.isModLoaded("MineTweaker3")
		NEILoaded = Loader.isModLoaded("NotEnoughItems")
		TravellersGearLoaded = Loader.isModLoaded("TravellersGear")
		WAILALoaded = Loader.isModLoaded("Waila")
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID)
		AlfheimConfig.loadConfig(File(e.modConfigurationDirectory.toString() + "/Alfheim", "$NAME.cfg"))
		
		if (AlfheimConfig.info) InfoLoader.start()
		
		registerPackets()
		
		proxy!!.initializeAndRegisterHandlers()
		proxy!!.preInit()
		if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.preInit()
	}
	
	@EventHandler
	fun init(e: FMLInitializationEvent) {
		proxy!!.init()
	}
	
	@EventHandler
	fun postInit(e: FMLPostInitializationEvent) {
		proxy!!.registerKeyBinds()
		proxy!!.registerRenderThings()
		proxy!!.postInit()
		AlfheimRegistry.loadAllPinkStuff()
		if (MineTweakerLoaded) MinetweakerAlfheimConfig.loadConfig()
		if (Botania.thaumcraftLoaded) {
			ThaumcraftAlfheimConfig.loadConfig()
			ThaumcraftAlfheimModule.postInit()
		}
		if (TravellersGearLoaded) TravellersGearAlfheimConfig.loadConfig()
		if (WAILALoaded) WAILAAlfheimConfig.loadConfig()
	}
	
	@EventHandler
	fun starting(e: FMLServerStartingEvent) {
		save = e.server.entityWorld.saveHandler.worldDirectory.absolutePath
		if (enableElvenStory) AlfheimConfig.initWorldCoordsForElvenStory(save)
		CardinalSystem.load(save)
		e.registerServerCommand(CommandAlfheim())
		CommandDimTP.register(e)
		e.registerServerCommand(CommandRace())
	}
	
	@EventHandler
	fun stopping(e: FMLServerStoppingEvent) {
		CardinalSystem.save(save)
	}
	
	companion object {
		
		@Instance(MODID)
		lateinit var instance: AlfheimCore
		
		@SidedProxy(clientSide = "$MODID.client.core.proxy.ClientProxy", serverSide = "$MODID.common.core.proxy.CommonProxy")
		var proxy: CommonProxy? = null
		
		lateinit var network: SimpleNetworkWrapper
		var nextPacketID = 0
		
		var save = ""
		
		var enableElvenStory = true
		var enableMMO = true
		var MineTweakerLoaded = false
		var NEILoaded = false
		var TravellersGearLoaded = false
		var WAILALoaded = false
		
		fun registerPackets() {
			AlfheimCore.network.registerMessage(Message0dHandler::class.java, Message0d::class.java, nextPacketID++, Side.SERVER)
			AlfheimCore.network.registerMessage(MessageHotSpellSHandler::class.java, MessageHotSpellS::class.java, nextPacketID++, Side.SERVER)
			AlfheimCore.network.registerMessage(MessageKeyBindHandler::class.java, MessageKeyBind::class.java, nextPacketID++, Side.SERVER)
			
			AlfheimCore.network.registerMessage(Message1dHandler::class.java, Message1d::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(Message2dHandler::class.java, Message2d::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(Message3dHandler::class.java, Message3d::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessageEffectHandler::class.java, MessageEffect::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessageHotSpellCHandler::class.java, MessageHotSpellC::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessageParticlesHandler::class.java, MessageParticles::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessagePartyHandler::class.java, MessageParty::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessageTileItemHandler::class.java, MessageTileItem::class.java, nextPacketID++, Side.CLIENT)
			AlfheimCore.network.registerMessage(MessageTimeStopHandler::class.java, MessageTimeStop::class.java, nextPacketID++, Side.CLIENT)
		}
		
		val alfheimTab = object: CreativeTabs("Alfheim") {
			override fun getTabIconItem(): Item {
				return Item.getItemFromBlock(AlfheimBlocks.alfheimPortal)
			}
		}.setNoTitle().setBackgroundImageName("Alfheim.png")
	}
}
