package alfheim

import alexsocol.asjlib.command.CommandDimTP
import alfheim.api.ModInfo.MODID
import alfheim.common.core.asm.AlfheimModularLoader
import alfheim.common.core.command.*
import alfheim.common.core.handler.*
import alfheim.common.core.proxy.CommonProxy
import alfheim.common.core.util.*
import alfheim.common.integration.bloodmagic.BloodMagicAlfheimConfig
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig
import alfheim.common.integration.thaumcraft.*
import alfheim.common.integration.thaumcraft.thaumictinkerer.ThaumicTinkererAlfheimConfig
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig
import alfheim.common.integration.travellersgear.TravellersGearAlfheimConfig
import alfheim.common.integration.waila.WAILAAlfheimConfig
import alfheim.common.network.*
import cpw.mods.fml.common.*
import cpw.mods.fml.common.Mod.*
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.*
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.relauncher.Side
import vazkii.botania.common.Botania
import java.util.*

@Suppress("UNUSED_PARAMETER")
@Mod(modid = MODID, version = "BETA", useMetadata = true, guiFactory = "$MODID.client.gui.GUIFactory")
class AlfheimCore {
	
	companion object {
		
		@Instance(MODID)
		lateinit var instance: AlfheimCore
		
		@field:SidedProxy(clientSide = "$MODID.client.core.proxy.ClientProxy", serverSide = "$MODID.common.core.proxy.CommonProxy")
		lateinit var proxy: CommonProxy
		
		@Metadata(MODID)
		lateinit var meta: ModMetadata
		
		lateinit var network: SimpleNetworkWrapper
		var nextPacketID = 0
		
		var save = ""
		
		var MineTweakerLoaded = false
		var NEILoaded = false
		var stupidMode = false
		var TiCLoaded = false
		var TravellersGearLoaded = false
		
		val jingleTheBells: Boolean
		val winter: Boolean
		
		/** Today's month */
		val month: Int
		/** Today's day of month */
		val date: Int
		
		init {
			AlfheimTab
			
			val calendar = Calendar.getInstance()
			month = calendar[2] + 1
			date = calendar[5]
			
			jingleTheBells = (month == 12 && date >= 16 || month == 1 && date <= 8)
			winter = month in arrayOf(1, 2, 12)
		}
	}
	
	@EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		if (AlfheimModularLoader.linkSpecified && !Loader.isModLoaded("alfmod"))
			throw IllegalStateException("Alfheim Modular was not loaded, please, relaunch your game.")
		
		MineTweakerLoaded = Loader.isModLoaded("MineTweaker3")
		NEILoaded = Loader.isModLoaded("NotEnoughItems")
		TiCLoaded = Loader.isModLoaded("TConstruct")
		TravellersGearLoaded = Loader.isModLoaded("TravellersGear")
		
		stupidMode = Loader.isModLoaded("Avaritia")
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID)
		
		if (AlfheimConfigHandler.notifications) InfoLoader.start()
		
		registerPackets()
		
		proxy.preInit()
		if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.preInit()
	}
	
	@EventHandler
	fun init(e: FMLInitializationEvent) {
		proxy.init()
		proxy.initializeAndRegisterHandlers()
	}
	
	@EventHandler
	fun postInit(e: FMLPostInitializationEvent) {
		proxy.registerKeyBinds()
		proxy.registerRenderThings()
		proxy.postInit()
		if (MineTweakerLoaded) MinetweakerAlfheimConfig.loadConfig()
		if (Botania.thaumcraftLoaded) {
			ThaumcraftAlfheimConfig.loadConfig()
			ThaumcraftAlfheimModule.postInit()
		}
		if (Loader.isModLoaded("AWWayofTime")) BloodMagicAlfheimConfig
		if (Loader.isModLoaded("ThaumicTinkerer")) ThaumicTinkererAlfheimConfig
		if (TravellersGearLoaded) TravellersGearAlfheimConfig.loadConfig()
		if (TiCLoaded) TinkersConstructAlfheimConfig.loadConfig()
		if (Loader.isModLoaded("Waila")) WAILAAlfheimConfig.loadConfig()
	}
	
	@EventHandler
	fun starting(e: FMLServerStartingEvent) {
		save = e.server.entityWorld.saveHandler.worldDirectory.absolutePath
		if (AlfheimConfigHandler.enableElvenStory) AlfheimConfigHandler.initWorldCoordsForElvenStory(save)
		AlfheimConfigHandler.syncConfig()
		CardinalSystem.load(save)
		e.registerServerCommand(CommandAlfheim())
		if (MineTweakerLoaded) e.registerServerCommand(CommandMTSpellInfo())
		CommandDimTP.register(e)
	}
	
	@EventHandler
	fun stopping(e: FMLServerStoppingEvent) {
		CardinalSystem.save(save)
	}
	
	fun registerPackets() {
		network.registerMessage(Message0dSHandler::class.java, Message0dS::class.java, nextPacketID++, Side.SERVER)
		network.registerMessage(MessageHotSpellSHandler::class.java, MessageHotSpellS::class.java, nextPacketID++, Side.SERVER)
		network.registerMessage(MessageKeyBindHandler::class.java, MessageKeyBindS::class.java, nextPacketID++, Side.SERVER)
		network.registerMessage(MessagePlayerItemHandler::class.java, MessagePlayerItemS::class.java, nextPacketID++, Side.SERVER)
		network.registerMessage(MessageRaceSelectionHandler::class.java, MessageRaceSelection::class.java, nextPacketID++, Side.SERVER)
		
		network.registerMessage(MessageContributorHandler::class.java, MessageContributor::class.java, nextPacketID++, Side.SERVER)
		network.registerMessage(MessageContributorHandler::class.java, MessageContributor::class.java, nextPacketID++, Side.CLIENT)
		
		network.registerMessage(Message0dCHandler::class.java, Message0dC::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(Message1dHandler::class.java, Message1d::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(Message1lHandler::class.java, Message1l::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(Message2dHandler::class.java, Message2d::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(Message3dHandler::class.java, Message3d::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageNIHandler::class.java, MessageNI::class.java, nextPacketID++, Side.CLIENT)
		
		network.registerMessage(MessageEffectHandler::class.java, MessageEffect::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageEffectLightningHandler::class.java, MessageEffectLightning::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageHotSpellCHandler::class.java, MessageHotSpellC::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessagePartyHandler::class.java, MessageParty::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageSkinInfoHandler::class.java, MessageSkinInfo::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageSpellParamsHandler::class.java, MessageSpellParams::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageTileItemHandler::class.java, MessageTileItem::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageTimeStopHandler::class.java, MessageTimeStop::class.java, nextPacketID++, Side.CLIENT)
		network.registerMessage(MessageVisualEffectHandler::class.java, MessageVisualEffect::class.java, nextPacketID++, Side.CLIENT)
	}
}
