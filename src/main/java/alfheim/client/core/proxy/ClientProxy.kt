package alfheim.client.core.proxy

import alexsocol.asjlib.*
import alexsocol.asjlib.render.*
import alfheim.AlfheimCore
import alfheim.api.item.DoubleBoundItemRender
import alfheim.api.lib.*
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.handler.EventHandlerClient
import alfheim.client.core.util.AlfheimBotaniaModifiersClient
import alfheim.client.gui.*
import alfheim.client.lib.LibResourceLocationsActual
import alfheim.client.render.block.*
import alfheim.client.render.entity.*
import alfheim.client.render.item.*
import alfheim.client.render.particle.*
import alfheim.client.render.tile.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.*
import alfheim.common.core.asm.hook.extender.FurnaceHandler
import alfheim.common.core.handler.*
import alfheim.common.core.proxy.CommonProxy
import alfheim.common.crafting.recipe.AlfheimRecipes
import alfheim.common.entity.*
import alfheim.common.entity.EntitySubspace
import alfheim.common.entity.EntitySubspaceSpear
import alfheim.common.entity.boss.*
import alfheim.common.entity.item.EntityItemImmortal
import alfheim.common.entity.spell.*
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.integration.travellersgear.TGHandlerBotaniaRenderer
import alfheim.common.item.AlfheimItems
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.client.registry.*
import net.minecraft.client.settings.KeyBinding
import net.minecraft.world.World
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
		LibRenderIDs
		
		if (ConfigHandler.useShaders) {
			LibShaderIDs.idColor3d = ASJShaderHelper.createProgram("shaders/position.vert", "shaders/color3d.frag")
			LibShaderIDs.idGravity = ASJShaderHelper.createProgram(null, "shaders/gravity.frag")
			LibShaderIDs.idNoise = ASJShaderHelper.createProgram("shaders/position.vert", "shaders/noise4d.frag")
			LibShaderIDs.idShadow = ASJShaderHelper.createProgram(null, "shaders/shadow.frag")
		}
		
		ClientRegistry.registerKeyBinding(keyLolicorn)
		
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.akashicRecords, RenderItemAkashicRecords)
		MinecraftForgeClient.registerItemRenderer(AlfheimBlocks.anomaly.toItem(), RenderItemAnomaly)
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.fenrirClaws, RenderItemFenrirClaws)
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.royalStaff, RenderItemRoyalStaff)
		
		if (!AlfheimConfigHandler.minimalGraphics)
			MinecraftForgeClient.registerItemRenderer(AlfheimItems.mjolnir, RenderItemMjolnir)
		
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idAnyavil, RenderBlockAnyavil)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idBarrel, RenderBlockBarrel)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idGrapeRedPlanted, RenderBlockGrapeRedPlanted)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idGrapeWhite, RenderBlockGrapeGreen)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idHarvester, RenderBlockAnomalyHarvester)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idManaAccelerator, RenderBlockItemHolder)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idPowerStone, RenderBlockPowerStone)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idPylon, RenderBlockAlfheimPylons)
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idShrinePanel, RenderBlockShrinePanel)
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPortal::class.java, RenderTileAlfheimPortal)
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPylon::class.java, RenderTileAlfheimPylons)
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnimatedTorch::class.java, RenderTileAnimatedTorch)
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnomaly::class.java, RenderTileAnomaly)
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnomalyHarvester::class.java, RenderTileAnomalyHarvester)
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnyavil::class.java, RenderTileAnyavil)
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel::class.java, RenderTileBarrel)
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeadFlugel::class.java, RenderTileHeadFlugel)
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeadMiku::class.java, RenderTileHeadMiku)
		ClientRegistry.bindTileEntitySpecialRenderer(TileManaAccelerator::class.java, RenderTileManaAccelerator)
		ClientRegistry.bindTileEntitySpecialRenderer(TilePowerStone::class.java, RenderTilePowerStone)
		ClientRegistry.bindTileEntitySpecialRenderer(TileRaceSelector::class.java, RenderTileRaceSelector)
		ClientRegistry.bindTileEntitySpecialRenderer(TileTradePortal::class.java, RenderTileTradePortal)
		
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie::class.java, RenderEntityAlfheimPixie)
		RenderingRegistry.registerEntityRenderingHandler(EntityButterfly::class.java, RenderEntityButterfly)
		RenderingRegistry.registerEntityRenderingHandler(EntityElf::class.java, RenderEntityElf)
		RenderingRegistry.registerEntityRenderingHandler(EntityFenrir::class.java, RenderEntityFenrir)
		RenderingRegistry.registerEntityRenderingHandler(EntityFlugel::class.java, RenderEntityFlugel)
		RenderingRegistry.registerEntityRenderingHandler(EntityGleipnir::class.java, RenderEntityGleipnir)
		RenderingRegistry.registerEntityRenderingHandler(EntityItemImmortal::class.java, RenderEntityItemImmortal)
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningMark::class.java, RenderEntityLightningMark)
		RenderingRegistry.registerEntityRenderingHandler(EntityLolicorn::class.java, RenderEntityLolicorn)
		RenderingRegistry.registerEntityRenderingHandler(EntityRook::class.java, RenderEntityRook)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellHarp::class.java, RenderEntityHarp)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellDriftingMine::class.java, RenderEntityDriftingMine)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellGravityTrap::class.java, RenderEntityGravityTrap)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellFenrirStorm::class.java, RenderEntityFenrirStorm)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellMortar::class.java, RenderEntityMortar)
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellWindBlade::class.java, RenderEntityWindBlade)
		
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.invisibleFlameLens, RenderLens())
		MinecraftForgeClient.registerItemRenderer(AlfheimItems.moonlightBow, RenderMoonBow)
		
		RenderingRegistry.registerBlockHandler(RenderBlockColoredDoubleGrass)
		RenderingRegistry.registerBlockHandler(MultipassRenderer)
		RenderingRegistry.registerBlockHandler(RenderBlockHopper)
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileItemDisplay::class.java, RenderTileItemDisplay)
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStar::class.java, RenderStar)
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMjolnir::class.java, RenderEntityMjolnir())
		
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownPotion::class.java, RenderEntityThrownPotion)
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowableItem::class.java, RenderEntityThrownItem)
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySubspace::class.java, RenderEntitySubspace)
		RenderingRegistry.registerEntityRenderingHandler(EntitySubspaceSpear::class.java, RenderEntitySubspaceSpear)
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGrieferCreeper::class.java, RenderEntityGrieferCreeper)
		RenderingRegistry.registerEntityRenderingHandler(EntityVoidCreeper::class.java, RenderEntityGrieferCreeper)
	}
	
	override fun registerKeyBinds() {
		if (AlfheimConfigHandler.enableElvenStory) addESMKeyBinds()
		if (AlfheimConfigHandler.enableMMO) addMMOKeyBinds()
	}
	
	override fun initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers()
		EventHandlerClient
		FurnaceHandler
		if (ConfigHandler.boundBlockWireframe) DoubleBoundItemRender
		if (AlfheimCore.TravellersGearLoaded) TGHandlerBotaniaRenderer
		if (AlfheimConfigHandler.enableElvenStory) enableESMGUIs()
		if (AlfheimConfigHandler.enableMMO) enableMMOGUIs()
		
		RenderPostShaders.allowShaders = !AlfheimConfigHandler.minimalGraphics
	}
	
	override fun postInit() {
		super.postInit()
		AlfheimBotaniaModifiersClient.postInit()
	}
	
	override fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, size: Float, lifetime: Float, distance: Float, must: Boolean) {
		if (mc.renderViewEntity != null && mc.effectRenderer != null) {
			val particle = EntityFeatherFx(world, x, y, z, color, size, lifetime)
			
			if (!must) {
				if (!doParticle(world)) return
				val distanceX: Double = mc.renderViewEntity.posX - particle.posX
				val distanceY: Double = mc.renderViewEntity.posY - particle.posY
				val distanceZ: Double = mc.renderViewEntity.posZ - particle.posZ
				if (distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ > distance * distance) {
					return
				}
			}
			
			mc.effectRenderer.addEffect(particle)
		}
	}
	
	override fun bloodFX(world: World, x: Double, y: Double, z: Double, lifetime: Int, size: Float, gravity: Float) {
		if (mc.renderViewEntity != null && mc.effectRenderer != null && doParticle(world)) {
			mc.effectRenderer.addEffect(EntityBloodFx(world, x, y, z, size, lifetime, gravity))
		}
	}
	
	fun doParticle(world: World): Boolean {
		return if (!world.isRemote) {
			false
		} else if (!ConfigHandler.useVanillaParticleLimiter) {
			true
		} else {
			var chance = 1f
			if (mc.gameSettings.particleSetting == 1) {
				chance = 0.6f
			} else if (mc.gameSettings.particleSetting == 2) {
				chance = 0.2f
			}
			chance == 1f || Math.random() < chance.D
		}
	}
	
	companion object {
		
		val keyLolicorn = KeyBinding("key.lolicorn.desc", Keyboard.KEY_L, "key.categories.alfheim")
		val keyESMAbility = KeyBinding("key.esmability.desc", Keyboard.KEY_M, "key.categories.alfheim")
		val keyFlight = KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.alfheim")
		val keyCast = KeyBinding("key.cast.desc", Keyboard.KEY_C, "key.categories.alfheim")
		val keyUnCast = KeyBinding("key.uncast.desc", Keyboard.KEY_X, "key.categories.alfheim")
		val keySelMob = KeyBinding("key.selmob.desc", Keyboard.KEY_R, "key.categories.alfheim")
		val keySelTeam = KeyBinding("key.selteam.desc", Keyboard.KEY_T, "key.categories.alfheim")
		
		init {
			removeKeyBinding(keyFlight)
			removeKeyBinding(keyESMAbility)
			removeKeyBinding(keyCast)
			removeKeyBinding(keyUnCast)
			removeKeyBinding(keySelMob)
			removeKeyBinding(keySelTeam)
		}
		
		private val guiIceLens = GUIIceLens()
		private val guiParty = GUIParty()
		private val guiRace = GUIRace()
		private val guiSpells = GUISpells()
		
		fun toggelModes(b: Boolean, esm: Boolean, mmo: Boolean, esmOld: Boolean, mmoOld: Boolean) {
			if (b)
				toggleESM(esm, mmo, esmOld, mmoOld)
			else
				toggleMMO(esm, mmo, esmOld, mmoOld)
		}
		
		fun enableESM() {
			if (AlfheimConfigHandler.enableElvenStory) return
			AlfheimConfigHandler.enableElvenStory = true
			AlfheimLexiconData.reEnableESM()
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.addESMRecipes()
			enableESMGUIs()
			addESMKeyBinds()
			ESMHandler.checkAddAttrs()
		}
		
		fun disableESM() {
			if (!AlfheimConfigHandler.enableElvenStory) return
			AlfheimConfigHandler.enableElvenStory = false
			AlfheimLexiconData.disableESM()
			if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.removeESMRecipes()
			disableESMGUIs()
			removeESMKeyBinds()
			disableMMO()
		}
		
		fun enableMMO() {
			if (AlfheimConfigHandler.enableMMO) return
			AlfheimConfigHandler.enableMMO = true
			AlfheimLexiconData.reEnableMMO()
			AlfheimRecipes.addMMORecipes()
			enableMMOGUIs()
			addMMOKeyBinds()
			enableESM()
		}
		
		fun disableMMO() {
			if (!AlfheimConfigHandler.enableMMO) return
			AlfheimConfigHandler.enableMMO = false
			AlfheimLexiconData.disableMMO()
			AlfheimRecipes.removeMMORecipes()
			disableMMOGUIs()
			removeMMOKeyBinds()
			TimeStopSystemClient.clear()
		}
		
		private fun toggleESM(esm: Boolean, mmo: Boolean, esmOld: Boolean, mmoOld: Boolean) {
			if (esmOld == esm) return
			AlfheimConfigHandler.enableElvenStory = esm
			
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
			AlfheimConfigHandler.enableMMO = mmo
			
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
			val id = ASJUtilities.indexOfComparableArray(mc.gameSettings.keyBindings, key)
			if (id < 0 || id > mc.gameSettings.keyBindings.size) return
			mc.gameSettings.keyBindings = ArrayUtils.remove(mc.gameSettings.keyBindings, id)
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