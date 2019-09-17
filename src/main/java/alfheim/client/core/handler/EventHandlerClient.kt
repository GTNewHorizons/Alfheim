package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.raceID
import alfheim.api.event.EntityUpdateEvent
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.gui.ItemsRemainingRenderHandler
import alfheim.client.render.entity.*
import alfheim.client.render.item.RenderItemFlugelHead
import alfheim.client.render.world.AstrolabePreviewHandler
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.registry.AlfheimRegistry
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.*
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.player.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.render.world.SkyblockSkyRenderer

@Suppress("UNUSED_PARAMETER")
object EventHandlerClient {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDrawScreenPost(event: RenderGameOverlayEvent.Post) {
		if (event.type != ElementType.HOTBAR) return
		ItemsRemainingRenderHandler.render(event.resolution, event.partialTicks)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onJoined(e: EntityJoinWorldEvent) {
		if (e === Minecraft.getMinecraft().thePlayer)
			PlayerSegmentClient.party = Party(Minecraft.getMinecraft().thePlayer)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDisconnect(e: ClientDisconnectionFromServerEvent) {
		TimeStopSystemClient.clear()
		PlayerSegmentClient.party = null
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onEntityUpdate(e: EntityUpdateEvent) {
		if (!ASJUtilities.isServer && TimeStopSystemClient.affected(e.entity)) e.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onEntityUpdate(e: LivingUpdateEvent) {
		if (!ASJUtilities.isServer && TimeStopSystemClient.affected(e.entity)) e.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onClientTick(e: ClientTickEvent) {
		val world = Minecraft.getMinecraft().theWorld
		if (world != null && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && world.provider.skyRenderer == null)
			world.provider.skyRenderer = SkyblockSkyRenderer()
		
		if (Minecraft.getMinecraft().thePlayer == null) PlayerSegmentClient.target = null
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerPreRender(e: RenderPlayerEvent.Pre) {
		RenderItemFlugelHead.render(e, e.entityPlayer)
		
		if (AlfheimCore.enableMMO && e.entityPlayer.isPotionActive(AlfheimRegistry.leftFlame.id)) {
			e.isCanceled = true
			return
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerSpecialPreRender(e: RenderPlayerEvent.Specials.Pre) {
		val player = e.entityPlayer as AbstractClientPlayer
		
		val name = player.commandSenderName
		
		if (name == "AlexSocol")
			player.func_152121_a(Type.SKIN, LibResourceLocations.skin)
			
		run skin@ {
			val data = CardinalSystemClient.playerSkinsData[name] ?: return@skin
			
			if (player.raceID == 0 || player.raceID > 9) return@skin
			
			if (data.second) {
				player.func_152121_a(Type.SKIN,
									 if (data.first)
										 LibResourceLocations.oldFemale[player.raceID-1]
									 else
										 LibResourceLocations.oldMale[player.raceID-1]
									)
			}
		}
		
		
		RenderEntitysLeftHand.render(e)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerSpecialPostRender(e: RenderPlayerEvent.Specials.Post) {
		RenderItemFlugelHead.render(e, e.entityPlayer)
		RenderWings.render(e, e.entityPlayer)
		RenderContributors.render(e, e.entityPlayer)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerTick(e: PlayerTickEvent) {
		if (e.player !== Minecraft.getMinecraft().thePlayer) return
		
		if (e.phase == Phase.START && e.side == Side.CLIENT && !Minecraft.getMinecraft().isGamePaused) {
			KeyBindingHandlerClient.parseKeybindings(e.player)
			SpellCastingSystemClient.tick()
			
			if (PlayerSegmentClient.target != null && Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
				if (!PlayerSegmentClient.target!!.isEntityAlive || Vector3.entityDistance(Minecraft.getMinecraft().thePlayer, PlayerSegmentClient.target!!) > (if (PlayerSegmentClient.target is IBossDisplayData) 128 else 32)) PlayerSegmentClient.target = null
		}
		if (e.phase == Phase.END) {
			ItemsRemainingRenderHandler.tick()
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onBlockOverlay(e: RenderBlockOverlayEvent) {
		if (AlfheimCore.enableMMO && e.overlayType != OverlayType.FIRE) e.isCanceled = e.player.isPotionActive(AlfheimRegistry.noclip)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onWorldLastRender(e: RenderWorldLastEvent) {
		AstrolabePreviewHandler.onWorldRenderLast(e)
		if (AlfheimCore.enableMMO) renderMMO()

//		glPushMatrix()
//		ASJRenderHelper.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
//		glTranslatef(0.5f, 5.5f, 0.5f)
//		glRotatef(180f, 1f, 0f, 0f)
//		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.male[3])
//		ModelBipedNew().render(0.0625f)
//		glPopMatrix()
	}
	
	private fun renderMMO() {
		run {
			val spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID)
						?: return@run
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) return@run
			
			glPushMatrix()
			ASJRenderHelper.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
			spell.render(Minecraft.getMinecraft().thePlayer)
			glPopMatrix()
		}
		
		run {
			val target = PlayerSegmentClient.target
			if (target != null) {
				if (target == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) return@run
				glPushMatrix()
				glDisable(GL_CULL_FACE)
				//glDisable(GL_ALPHA_TEST);
				glAlphaFunc(GL_GREATER, 0.003921569f)
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				if (target != Minecraft.getMinecraft().thePlayer) {
					ASJRenderHelper.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
					ASJRenderHelper.interpolatedTranslation(target)
				} else {
					glTranslated(0.0, -(1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight), 0.0)
				}
				glRotated((Minecraft.getMinecraft().theWorld.totalWorldTime + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble(), 0.0, 1.0, 0.0)
				glScalef(target.width, target.width, target.width)
				ASJRenderHelper.glColor1u(if (PlayerSegmentClient.isParty) -0xff0100 else -0x10000)
				Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.cross)
				Tessellator.instance.startDrawingQuads()
				Tessellator.instance.addVertexWithUV(-1.0, 0.1, -1.0, 0.0, 0.0)
				Tessellator.instance.addVertexWithUV(-1.0, 0.1, 1.0, 0.0, 1.0)
				Tessellator.instance.addVertexWithUV(1.0, 0.1, 1.0, 1.0, 1.0)
				Tessellator.instance.addVertexWithUV(1.0, 0.1, -1.0, 1.0, 0.0)
				Tessellator.instance.draw()
				glDisable(GL_BLEND)
				glAlphaFunc(GL_GREATER, 0.1f)
				//glEnable(GL_ALPHA_TEST);
				glEnable(GL_CULL_FACE)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				glPopMatrix()
			}
		}
		
		TimeStopSystemClient.render()
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onClonePlayer(e: PlayerEvent.Clone) {
		if (AlfheimCore.enableElvenStory) {
			e.entityPlayer.raceID = e.original.raceID
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onItemTooltip(e: ItemTooltipEvent) {
		if (GuiScreen.isShiftKeyDown() && e.itemStack.hasTagCompound() && e.showAdvancedItemTooltips) {
			e.toolTip.add("")
			e.toolTip.add("NBT Data:")
			e.toolTip.addAll(listOf(*ASJUtilities.toString(e.itemStack.tagCompound).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onFOV(e: FOVUpdateEvent) {
		if (AlfheimCore.enableMMO && e.entity.getActivePotionEffect(AlfheimRegistry.icelens) != null) e.newfov = 0.1f
	}
}