package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.EnumRace
import alfheim.api.event.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.gui.ItemsRemainingRenderHandler
import alfheim.client.model.item.ModelCreatorStaff
import alfheim.client.render.entity.*
import alfheim.client.render.item.RenderItemFlugelHead
import alfheim.client.render.world.AstrolabePreviewHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.item.AlfheimItems
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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.player.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.render.world.SkyblockSkyRenderer

@Suppress("UNUSED_PARAMETER")
class EventHandlerClient {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDrawScreenPost(event: RenderGameOverlayEvent.Post) {
		if (event.type != ElementType.HOTBAR) return
		ItemsRemainingRenderHandler.render(event.resolution, event.partialTicks)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDisconnect(e: ClientDisconnectionFromServerEvent) {
		CardinalSystemClient.TimeStopSystemClient.clear()
		CardinalSystemClient.segment = null
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onTileUpdate(e: TileUpdateEvent) {
		if (!ASJUtilities.isServer && CardinalSystemClient.TimeStopSystemClient.affected(e.tile)) e.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onEntityUpdate(e: EntityUpdateEvent) {
		if (!ASJUtilities.isServer && CardinalSystemClient.TimeStopSystemClient.affected(e.entity)) e.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onEntityUpdate(e: LivingUpdateEvent) {
		if (!ASJUtilities.isServer && CardinalSystemClient.TimeStopSystemClient.affected(e.entity)) e.isCanceled = true
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onClientTick(e: ClientTickEvent) {
		val world = Minecraft.getMinecraft().theWorld
		if (world != null && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && world.provider.skyRenderer == null)
			world.provider.skyRenderer = SkyblockSkyRenderer()
		
		if (CardinalSystemClient.segment != null && Minecraft.getMinecraft().thePlayer == null) CardinalSystemClient.segment!!.target = null
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
		if (e.entityPlayer.commandSenderName == "AlexSocol") {
			(e.entityPlayer as AbstractClientPlayer).func_152121_a(Type.SKIN, LibResourceLocations.skin)
			
			if (e.entityPlayer.heldItem?.item !== AlfheimItems.royalStaff) run {
				glPushMatrix()
				var f1: Float
				
				if (e.renderer.mainModel.isChild) {
					f1 = 0.5f
					glTranslatef(0.0f, 0.625f, 0.0f)
					glRotatef(-20.0f, -1.0f, 0.0f, 0.0f)
					glScalef(f1, f1, f1)
				}
				
				e.renderer.modelBipedMain.bipedLeftArm.postRender(0.0625f)
				glTranslatef(-0.0625f, 0.4375f, 0.0625f)
				
				f1 = 0.625f
				
				glTranslatef(0.0f, 0.1875f, 0.0f)
				glScalef(f1, -f1, f1)
				glRotatef(-100.0f, 1.0f, 0.0f, 0.0f)
				glRotatef(45.0f, 0.0f, 1.0f, 0.0f)
				
				val f2: Float
				val i = 0xFFFFFF
				val f5: Float
				
				val f4 = (i shr 16 and 255).toFloat() / 255.0f
				f5 = (i shr 8 and 255).toFloat() / 255.0f
				f2 = (i and 255).toFloat() / 255.0f
				glColor4f(f4, f5, f2, 1.0f)
				
				glPushMatrix()
				glTranslated(0.1, 1.5, 0.15)
				glRotatef(180f, 1f, 0f, 0f)
				
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				ModelCreatorStaff.instance.render()
				glDisable(GL_BLEND)
				glPopMatrix()
				
				glPopMatrix()
			}
		}
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
		if (e.phase == Phase.START && e.side == Side.CLIENT && !Minecraft.getMinecraft().isGamePaused) {
			KeyBindingHandlerClient.parseKeybindings(e.player)
			CardinalSystemClient.SpellCastingSystemClient.tick()
			
			if (CardinalSystemClient.segment!!.target != null && Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
				if (!CardinalSystemClient.segment!!.target!!.isEntityAlive || Vector3.entityDistance(Minecraft.getMinecraft().thePlayer, CardinalSystemClient.segment!!.target!!) > (if (CardinalSystemClient.segment!!.target is IBossDisplayData) 128 else 32)) CardinalSystemClient.segment!!.target = null
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
//		ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
//		glTranslatef(0.5f, 5.5f, 0.5f)
//		glRotatef(180f, 1f, 0f, 0f)
//		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.male[8])
//		ModelBipedNew().render(0.0625f)
//		glPopMatrix()
	}
	
	private fun renderMMO() {
		run {
			val spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID)
			if (CardinalSystemClient.SpellCastingSystemClient.getCoolDown(spell) > 0) return@run
			
			glPushMatrix()
			ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
			spell!!.render(Minecraft.getMinecraft().thePlayer)
			glPopMatrix()
		}
		
		run {
			if (CardinalSystemClient.segment().target != null) {
				if (CardinalSystemClient.segment!!.target == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) return@run
				glPushMatrix()
				glDisable(GL_CULL_FACE)
				//glDisable(GL_ALPHA_TEST);
				glAlphaFunc(GL_GREATER, 0.003921569f)
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				if (CardinalSystemClient.segment!!.target != Minecraft.getMinecraft().thePlayer) {
					ASJUtilities.interpolatedTranslationReverse(Minecraft.getMinecraft().thePlayer)
					ASJUtilities.interpolatedTranslation(CardinalSystemClient.segment!!.target!!)
				} else {
					glTranslated(0.0, -(1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight), 0.0)
				}
				glRotated((Minecraft.getMinecraft().theWorld.totalWorldTime + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble(), 0.0, 1.0, 0.0)
				glScalef(CardinalSystemClient.segment!!.target!!.width, CardinalSystemClient.segment!!.target!!.width, CardinalSystemClient.segment!!.target!!.width)
				ASJUtilities.glColor1u(if (CardinalSystemClient.segment!!.isParty) -0xff0100 else -0x10000)
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
		
		CardinalSystemClient.TimeStopSystemClient.render()
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onClonePlayer(e: PlayerEvent.Clone) {
		if (AlfheimCore.enableElvenStory) {
			val r = EnumRace.getByID(e.original.getEntityAttribute(AlfheimAPI.RACE).attributeValue)
			e.entityPlayer.getEntityAttribute(AlfheimAPI.RACE).baseValue = r.ordinal.toDouble()
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