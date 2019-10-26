package alfheim.client.core.proxy

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.render.ASJShaderHelper
import alfheim.AlfheimCore
import alfheim.api.lib.*
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.handler.EventHandlerClient
import alfheim.client.core.util.AlfheimBotaniaModifiersClient
import alfheim.client.gui.*
import alfheim.client.lib.LibResourceLocationsActual
import alfheim.client.model.entity.*
import alfheim.client.render.block.*
import alfheim.client.render.entity.*
import alfheim.client.render.item.*
import alfheim.client.render.tile.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.*
import alfheim.common.core.handler.ESMHandler
import alfheim.common.core.proxy.CommonProxy
import alfheim.common.crafting.recipe.AlfheimRecipes
import alfheim.common.entity.*
import alfheim.common.entity.EntitySubspace
import alfheim.common.entity.EntitySubspaceSpear
import alfheim.common.entity.boss.*
import alfheim.common.entity.spell.*
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.integration.travellersgear.TGHandlerBotaniaRenderer
import alfheim.common.item.AlfheimItems
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.client.registry.*
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.Item
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.MinecraftForge
import org.apache.commons.lang3.ArrayUtils
import org.lwjgl.input.Keyboard
import vazkii.botania.client.render.item.RenderLens
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler

class ClientProxy: CommonProxy() {
	
	override fun preInit() {
		super.preInit()
		LibResourceLocationsActual.init()
	}
	
	override fun registerRenderThings() {
		LibRenderIDs.init()
		
		if (ConfigHandler.useShaders) {
			LibShaderIDs.idGravity = ASJShaderHelper.createProgram(null, "shaders/gravity.frag")
			LibShaderIDs.idNoise = ASJShaderHelper.createProgram("shaders/position.vert", "shaders/noise4d.frag")
			LibShaderIDs.idShadow = ASJShaderHelper.createProgram(null, "shaders/shadow.frag")
		}
		
		ClientRegistry.registerKeyBinding(keyLolicorn)
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AlfheimBlocks.anomaly), RenderItemAnomaly())
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.royalStaff, RenderItemRoyalStaff())
		
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idAnyavil, RenderBlockAnyavil())
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idManaAccelerator, RenderBlockItemHolder())
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idPylon, RenderBlockAlfheimPylons())
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idShrinePanel, RenderBlockShrinePanel())
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idTransferer, RenderBlockTransferer())
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPortal::class.java, RenderTileAlfheimPortal())
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPylon::class.java, RenderTileAlfheimPylons())
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnimatedTorch::class.java, RenderTileAnimatedTorch())
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnomaly::class.java, RenderTileAnomaly())
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnyavil::class.java, RenderTileAnyavil())
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeadFlugel::class.java, RenderTileHeadFlugel())
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeadMiku::class.java, RenderTileHeadMiku())
		ClientRegistry.bindTileEntitySpecialRenderer(TileManaAccelerator::class.java, RenderTileManaAccelerator())
		ClientRegistry.bindTileEntitySpecialRenderer(TileRaceSelector::class.java, RenderTileRaceSelector())
		ClientRegistry.bindTileEntitySpecialRenderer(TileTradePortal::class.java, RenderTileTradePortal())
		ClientRegistry.bindTileEntitySpecialRenderer(TileTransferer::class.java, RenderTileTransferer())
		
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie::class.java, RenderEntityAlfheimPixie())
		RenderingRegistry.registerEntityRenderingHandler(EntityElf::class.java, RenderEntityElf(ModelEntityElf(), 0.25f))
		RenderingRegistry.registerEntityRenderingHandler(EntityFlugel::class.java, RenderEntityFlugel(ModelEntityFlugel(), 0.25f))
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningMark::class.java, RenderEntityLightningMark())
		RenderingRegistry.registerEntityRenderingHandler(EntityLolicorn::class.java, RenderEntityLolicorn(ModelEntityLolicorn(), 0.5f))
		RenderingRegistry.registerEntityRenderingHandler(EntityRook::class.java, RenderEntityRook(ModelEntityRook(), 1.5f))
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellHarp::class.java, RenderEntityHarp())
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellDriftingMine::class.java, RenderEntityDriftingMine())
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellGravityTrap::class.java, RenderEntityGravityTrap())
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellFenrirStorm::class.java, RenderEntityFenrirStorm())
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellMortar::class.java, RenderEntityMortar())
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellWindBlade::class.java, RenderEntityWindBlade())
		
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.invisibleFlameLens, RenderLens())
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.moonlightBow, RenderMoonBow())
		
		RenderingRegistry.registerBlockHandler(RenderBlockColoredDoubleGrass())
		RenderingRegistry.registerBlockHandler(MultipassRenderer())
		RenderingRegistry.registerBlockHandler(RenderBlockHopper())
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileItemDisplay::class.java, RenderTileItemDisplay())
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStar::class.java, RenderStar())
		
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownPotion::class.java, RenderThrownPotion())
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowableItem::class.java, RenderThrownItem())
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySubspace::class.java, RenderSubspace())
		RenderingRegistry.registerEntityRenderingHandler(EntitySubspaceSpear::class.java, RenderSubspaceSpear())
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGrieferCreeper::class.java, RenderGrieferCreeper())
		RenderingRegistry.registerEntityRenderingHandler(EntityVoidCreeper::class.java, RenderGrieferCreeper())
	}
	
	override fun registerKeyBinds() {
		if (AlfheimCore.enableElvenStory) addESMKeyBinds()
		if (AlfheimCore.enableMMO) addMMOKeyBinds()
	}
	
	override fun initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers()
		MinecraftForge.EVENT_BUS.register(EventHandlerClient)
		FMLCommonHandler.instance().bus().register(EventHandlerClient)
		if (AlfheimCore.TravellersGearLoaded) MinecraftForge.EVENT_BUS.register(TGHandlerBotaniaRenderer())
		if (AlfheimCore.enableElvenStory) enableESMGUIs()
		if (AlfheimCore.enableMMO) enableMMOGUIs()
	}
	
	override fun postInit() {
		super.postInit()
		AlfheimBotaniaModifiersClient.postInit()
	}
	
	companion object {
		
		val keyLolicorn = KeyBinding("key.lolicorn.desc", Keyboard.KEY_L, "key.categories.misc")
		val keyESMAbility = KeyBinding("key.esmability.desc", Keyboard.KEY_M, "key.categories.gameplay")
		val keyFlight = KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement")
		val keyCast = KeyBinding("key.cast.desc", Keyboard.KEY_C, "key.categories.gameplay")
		val keyUnCast = KeyBinding("key.uncast.desc", Keyboard.KEY_X, "key.categories.gameplay")
		val keySelMob = KeyBinding("key.selmob.desc", Keyboard.KEY_R, "key.categories.gameplay")
		val keySelTeam = KeyBinding("key.selteam.desc", Keyboard.KEY_T, "key.categories.gameplay")
		
		init {
			removeKeyBinding(keyFlight)
			removeKeyBinding(keyESMAbility)
			removeKeyBinding(keyCast)
			removeKeyBinding(keyUnCast)
			removeKeyBinding(keySelMob)
			removeKeyBinding(keySelTeam)
		}
		
		private val guiIceLens = GUIIceLens(Minecraft.getMinecraft())
		private val guiParty = GUIParty(Minecraft.getMinecraft())
		private val guiRace = GUIRace(Minecraft.getMinecraft())
		private val guiSpells = GUISpells(Minecraft.getMinecraft())
		
		fun toggelModes(b: Boolean, esm: Boolean, mmo: Boolean, esmOld: Boolean, mmoOld: Boolean) {
			if (b)
				toggleESM(esm, mmo, esmOld, mmoOld)
			else
				toggleMMO(esm, mmo, esmOld, mmoOld)
		}
		
		fun enableESM() {
			if (AlfheimCore.enableElvenStory) return
			AlfheimCore.enableElvenStory = true
			AlfheimLexiconData.reEnableESM()
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.addESMRecipes()
			enableESMGUIs()
			addESMKeyBinds()
			ESMHandler.checkAddAttrs()
		}
		
		fun disableESM() {
			if (!AlfheimCore.enableElvenStory) return
			AlfheimCore.enableElvenStory = false
			AlfheimLexiconData.disableESM()
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.removeESMRecipes()
			disableESMGUIs()
			removeESMKeyBinds()
			disableMMO()
		}
		
		fun enableMMO() {
			if (AlfheimCore.enableMMO) return
			AlfheimCore.enableMMO = true
			AlfheimLexiconData.reEnableMMO()
			AlfheimRecipes.addMMORecipes()
			enableMMOGUIs()
			addMMOKeyBinds()
			enableESM()
		}
		
		fun disableMMO() {
			if (!AlfheimCore.enableMMO) return
			AlfheimCore.enableMMO = false
			AlfheimLexiconData.disableMMO()
			AlfheimRecipes.removeMMORecipes()
			disableMMOGUIs()
			removeMMOKeyBinds()
			TimeStopSystemClient.clear()
		}
		
		private fun toggleESM(esm: Boolean, mmo: Boolean, esmOld: Boolean, mmoOld: Boolean) {
			if (esmOld == esm) return
			AlfheimCore.enableElvenStory = esm
			
			if (esm) {
				AlfheimLexiconData.reEnableESM()
				addESMKeyBinds()
			} else {
				AlfheimLexiconData.disableESM()
				removeESMKeyBinds()
				if (mmoOld != mmo) toggleMMO(false, mmo, esmOld, mmoOld)
			}
		}
		
		private fun toggleMMO(esm: Boolean, mmo: Boolean, esmOld: Boolean, mmoOld: Boolean) {
			if (mmoOld == mmo) return
			AlfheimCore.enableMMO = mmo
			
			if (mmo) {
				AlfheimLexiconData.reEnableMMO()
				enableMMOGUIs()
				addMMOKeyBinds()
				if (mmoOld != esm) toggleESM(esm, true, esmOld, mmoOld)
			} else {
				AlfheimLexiconData.disableMMO()
				disableMMOGUIs()
				removeMMOKeyBinds()
				TimeStopSystemClient.clear()
			}
		}
		
		private fun enableESMGUIs() {
			ASJUtilities.log("Registering ESM GUIs")
			MinecraftForge.EVENT_BUS.register(guiRace)
		}
		
		private fun disableESMGUIs() {
			ASJUtilities.log("Unregistering ESM GUIs")
			MinecraftForge.EVENT_BUS.unregister(guiRace)
		}
		
		private fun enableMMOGUIs() {
			ASJUtilities.log("Registering MMO GUIs")
			MinecraftForge.EVENT_BUS.register(guiIceLens)
			MinecraftForge.EVENT_BUS.register(guiParty)
			MinecraftForge.EVENT_BUS.register(guiSpells)
			
			MinecraftForge.EVENT_BUS.unregister(guiRace)
		}
		
		private fun disableMMOGUIs() {
			ASJUtilities.log("Unregistering MMO GUIs")
			MinecraftForge.EVENT_BUS.unregister(guiIceLens)
			MinecraftForge.EVENT_BUS.unregister(guiParty)
			MinecraftForge.EVENT_BUS.unregister(guiSpells)
			
			MinecraftForge.EVENT_BUS.register(guiRace)
		}
		
		private fun addESMKeyBinds() {
			addKeyBinding(keyFlight)
			addKeyBinding(keyESMAbility)
			
			KeyBinding.resetKeyBindingArrayAndHash()
		}
		
		private fun removeESMKeyBinds() {
			unregisterKeyBinding(keyFlight)
			unregisterKeyBinding(keyESMAbility)
			
			KeyBinding.resetKeyBindingArrayAndHash()
		}
		
		private fun addMMOKeyBinds() {
			addKeyBinding(keyCast)
			addKeyBinding(keyUnCast)
			addKeyBinding(keySelMob)
			addKeyBinding(keySelTeam)
			
			KeyBinding.resetKeyBindingArrayAndHash()
		}
		
		private fun removeMMOKeyBinds() {
			unregisterKeyBinding(keyCast)
			unregisterKeyBinding(keyUnCast)
			unregisterKeyBinding(keySelMob)
			unregisterKeyBinding(keySelTeam)
			
			KeyBinding.resetKeyBindingArrayAndHash()
		}
		
		private fun unregisterKeyBinding(key: KeyBinding) {
			removeKeyBinding(key)
			val id = ASJUtilities.indexOfComparableArray(Minecraft.getMinecraft().gameSettings.keyBindings, key)
			if (id < 0 || id > Minecraft.getMinecraft().gameSettings.keyBindings.size) return
			Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.remove(Minecraft.getMinecraft().gameSettings.keyBindings, id)
		}
		
		private fun addKeyBinding(key: KeyBinding) {
			key.keyCode = key.keyCodeDefault
			ClientRegistry.registerKeyBinding(key)
		}
		
		private fun removeKeyBinding(key: KeyBinding) {
			key.keyCode = 0
		}
	}
}