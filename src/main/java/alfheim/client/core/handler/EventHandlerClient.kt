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
import alfheim.client.core.util.mc
import alfheim.client.gui.ItemsRemainingRenderHandler
import alfheim.client.render.entity.*
import alfheim.client.render.item.RenderItemFlugelHead
import alfheim.client.render.particle.EntityFeatherFx
import alfheim.client.render.world.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.util.*
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.*
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.*
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.player.*
import org.lwjgl.opengl.GL11.*

object EventHandlerClient {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDrawScreenPre(event: RenderGameOverlayEvent.Pre) {
		if (event.type === ElementType.BOSSHEALTH && AlfheimCore.enableMMO)
			event.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDrawScreenPost(event: RenderGameOverlayEvent.Post) {
		if (event.type != ElementType.HOTBAR) return
		
		ItemsRemainingRenderHandler.render(event.resolution, event.partialTicks)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onJoined(e: EntityJoinWorldEvent) {
		if (e === mc.thePlayer)
			PlayerSegmentClient.party = Party(mc.thePlayer)
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
		val world = mc.theWorld
		if (world != null && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && world.provider.skyRenderer == null)
			world.provider.skyRenderer = AlfheimSkyRenderer
		
		if (mc.thePlayer == null) PlayerSegmentClient.target = null
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerPreRender(e: RenderPlayerEvent.Pre) {
		RenderItemFlugelHead.render(e, e.entityPlayer)
		
		if (AlfheimCore.enableMMO && e.entityPlayer.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
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
		val player = mc.thePlayer
		if (e.player !== player) return
		
		if (e.phase == Phase.START && e.side == Side.CLIENT && !mc.isGamePaused) {
			KeyBindingHandlerClient.parseKeybindings(e.player)
			SpellCastingSystemClient.tick()
			
			if (mc != null && player != null) {
				val tg = PlayerSegmentClient.target
				if (tg != null) {
					if (!tg.isEntityAlive || Vector3.entityDistance(player, tg) > (if (tg is IBossDisplayData) 128 else 32)) PlayerSegmentClient.target = null
				} else if (PlayerSegmentClient.partyIndex > 0) run {
					val mr = PlayerSegmentClient.party?.get(PlayerSegmentClient.partyIndex) ?: return@run
					if (!mr.isEntityAlive || Vector3.entityDistance(player, mr) > (if (tg is IBossDisplayData) 128 else 32)) return@run
					PlayerSegmentClient.target = mr
				}
			}
		}
		if (e.phase == Phase.END) {
			ItemsRemainingRenderHandler.tick()
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onBlockOverlay(e: RenderBlockOverlayEvent) {
		if (AlfheimCore.enableMMO && e.overlayType != OverlayType.FIRE) e.isCanceled = e.player.isPotionActive(AlfheimConfigHandler.potionIDNoclip)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onWorldLastRender(e: RenderWorldLastEvent) {
		AstrolabePreviewHandler.onWorldRenderLast(e)
		if (AlfheimCore.enableMMO) renderMMO()

//		glPushMatrix()
//		ASJRenderHelper.interpolatedTranslationReverse(mc.thePlayer)
//		glTranslatef(0.5f, 5.5f, 0.5f)
//		glRotatef(180f, 1f, 0f, 0f)
//		mc.renderEngine.bindTexture(LibResourceLocations.male[3])
//		ModelBipedNew().render(0.0625f)
//		glPopMatrix()
	}
	
	private fun renderMMO() {
		run {
			val spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID)
						?: return@run
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) return@run
			
			glPushMatrix()
			ASJRenderHelper.interpolatedTranslationReverse(mc.thePlayer)
			spell.render(mc.thePlayer)
			glPopMatrix()
		}
		
		run {
			val target = PlayerSegmentClient.target
			if (target != null) {
				if (target == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return@run
				glPushMatrix()
				glDisable(GL_CULL_FACE)
				//glDisable(GL_ALPHA_TEST);
				glAlphaFunc(GL_GREATER, 0.003921569f)
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				if (target != mc.thePlayer) {
					ASJRenderHelper.interpolatedTranslationReverse(mc.thePlayer)
					ASJRenderHelper.interpolatedTranslation(target)
				} else {
					glTranslated(0.0, -(1.5 + mc.thePlayer.eyeHeight), 0.0)
				}
				glRotated((mc.theWorld.totalWorldTime + mc.timer.renderPartialTicks).D, 0.0, 1.0, 0.0)
				glScalef(target.width, target.width, target.width)
				ASJRenderHelper.glColor1u(if (PlayerSegmentClient.isParty) -0xff0100 else -0x10000)
				mc.renderEngine.bindTexture(LibResourceLocations.cross)
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
		if (GuiScreen.isShiftKeyDown()) {
			val stack = e.itemStack
			
			if(stack.hasTagCompound() && e.showAdvancedItemTooltips) {
				e.toolTip.add("")
				e.toolTip.add("NBT Data:")
				e.toolTip.addAll(listOf(*ASJUtilities.toString(stack.tagCompound).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onFOV(e: FOVUpdateEvent) {
		if (AlfheimCore.enableMMO && e.entity.getActivePotionEffect(AlfheimConfigHandler.potionIDIceLens) != null) e.newfov = 0.1f
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
		mc.entityRenderer.enableLightmap(event.partialTicks.D)
		RenderHelper.disableStandardItemLighting()
		glColor4f(1f, 1f, 1f, 1f)
		glDepthMask(false)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glAlphaFunc(GL_GREATER, 0.003921569f)
		mc.mcProfiler.startSection("wingParticles")
		EntityFeatherFx.renderQueue()
		mc.mcProfiler.endSection()
		glDisable(GL_BLEND)
		glDepthMask(true)
		mc.entityRenderer.disableLightmap(event.partialTicks.D)
	}
}