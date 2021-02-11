package alfheim.client.render.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.render.*
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelEntityFlugel
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.ContributorsPrivacyHelper
import alfheim.common.item.material.ItemElvenResource
import net.minecraft.client.model.ModelBook
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.item.IBaubleRender.Helper
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import java.awt.Color
import kotlin.math.sin

object RenderContributors {
	
	val auraTextures: Map<String, ResourceLocation> by lazy { ContributorsPrivacyHelper.auras.map { (k, v) -> k to ResourceLocation(ModInfo.MODID, "textures/model/entity/auras/$v.png") }.toMap() }
	val book = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/mudrbook.obj"))
	
	val so: ShadedObject = object: ShadedObject(ShaderHelper.halo, RenderPostShaders.nextAvailableRenderObjectMaterialID, LibResourceLocations.babylon) {
		
		override fun preRender() {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glDisable(GL_CULL_FACE)
			glShadeModel(GL_SMOOTH)
		}
		
		override fun drawMesh() {
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
			tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
			tes.draw()
		}
		
		override fun postRender() {
			glShadeModel(GL_FLAT)
			glEnable(GL_CULL_FACE)
			glDisable(GL_BLEND)
		}
	}
	
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		if (player.isInvisible || player.isInvisibleToPlayer(mc.thePlayer) || player.isPotionActive(Potion.invisibility)) return
		if (!AlfheimConfigHandler.fancies) return
		
		glColor4f(1f, 1f, 1f, 1f)
		
		if (ContributorsPrivacyHelper.isCorrect(player, "AlexSocol")) {
			//run {
			//	// jojo's mask
			//	if (PlayerHandler.getPlayerBaubles(player)?.get(0)?.item !== AlfheimItems.mask) {
			//		val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick
			//		val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick
			//		val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick
			//
			//		glPushMatrix()
			//		glRotatef(yawOffset, 0f, -1f, 0f)
			//		glRotatef(yaw - 270, 0f, 1f, 0f)
			//		glRotatef(pitch, 0f, 0f, 1f)
			//		glColor4f(0.375f, 0f, 0f, 1f)
			//		val mask = ItemStack(AlfheimItems.mask)
			//		mask.setStackDisplayName("kono dio da")
			//		(AlfheimItems.mask as ItemTankMask).onPlayerBaubleRender(mask, e, IBaubleRender.RenderType.HEAD)
			//
			//		glPopMatrix()
			//	}
			//}
			
			run {
				// devil wings
				glDisable(GL_CULL_FACE)
				(ModItems.flightTiara as ItemFlightTiara).onPlayerBaubleRender(ItemStack(ModItems.flightTiara, 1, 6), e, IBaubleRender.RenderType.BODY)
			}
			
			run {
				// babylon circle
				glPushMatrix()
				glRotated(90.0, 1.0, 0.0, 0.0)
				Helper.rotateIfSneaking(player)
				glTranslated(0.0, 0.15, -0.25)
				
				glRotated(mc.theWorld.totalWorldTime / 2.0 + mc.timer.renderPartialTicks, 0.0, 1.0, 0.0)
				glScaled(0.2)
				
				so.addTranslation()
				glPopMatrix()
			}
			
			run {
				// wings
				val icon = ItemElvenResource.wing
				mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
				
				val flying = player.capabilities.isFlying
				
				val rz = 120f
				val rx = 20f + ((sin((player.ticksExisted + mc.timer.renderPartialTicks).D * if (flying) 0.4f else 0.2f) + 0.5f) * if (flying) 30f else 5f).F
				val ry = 0f
				val h = 0.2f
				val i = 0.15f
				
				glPushMatrix()
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				
				val lastX = OpenGlHelper.lastBrightnessX
				val lastY = OpenGlHelper.lastBrightnessY
				
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
				
				val f = icon.minU
				val f1 = icon.maxU
				val f2 = icon.minV
				val f3 = icon.maxV
				
				Helper.rotateIfSneaking(player)
				
				glTranslatef(0f, h, i)
				
				glRotatef(rz, 0f, 0f, 1f)
				glRotatef(rx, 1f, 0f, 0f)
				glRotatef(ry, 0f, 1f, 0f)
				ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
				glRotatef(-ry, 0f, 1f, 0f)
				glRotatef(-rx, 1f, 0f, 0f)
				glRotatef(-rz, 0f, 0f, 1f)
				
				glScalef(-1f, 1f, 1f)
				glRotatef(rz, 0f, 0f, 1f)
				glRotatef(rx, 1f, 0f, 0f)
				glRotatef(ry, 0f, 1f, 0f)
				ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
				glRotatef(-ry, 1f, 0f, 0f)
				glRotatef(-rx, 1f, 0f, 0f)
				glRotatef(-rz, 0f, 0f, 1f)
				
				glDisable(GL_BLEND)
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				glPopMatrix()
			}
		}
		
		if (ContributorsPrivacyHelper.isCorrect(player, "DmitryWS")) run dws@{
			if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return@dws
			
			glPushMatrix()
			glEnable(GL_CULL_FACE)
			val t = sin((mc.theWorld.totalWorldTime + mc.timer.renderPartialTicks) / 10.0)
			
			glTranslated(0.0, -(0.9 + t * 0.05), 0.0)
			glRotated(180.0, 1.0, 0.0, 0.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			glRotated(60.0, 0.0, 0.0, 1.0)
			mc.renderEngine.bindTexture(LibResourceLocations.lexica)
			val model = ModelBook()
			model.render(null, 0f, 0.075f + (t * 0.025).F, 0.925f - (t * 0.025).F, 1f, 0f, 0.0625f)
			glPopMatrix()
		}
		
		val match = ContributorsPrivacyHelper.auras.keys.firstOrNull { ContributorsPrivacyHelper.isCorrect(player, it) }
		
		if (match != null) run aura@{
			if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return@aura
			
			glPushMatrix()
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glDisable(GL_CULL_FACE)
			glDisable(GL_LIGHTING)
			glAlphaFunc(GL_GREATER, 1 / 255f)
			
			val lastX = OpenGlHelper.lastBrightnessX
			val lastY = OpenGlHelper.lastBrightnessY
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
			glShadeModel(GL_SMOOTH)
			
			glTranslated(0.0, 1.35, 0.0)
			glRotated(mc.theWorld.totalWorldTime / 2.0 + mc.timer.renderPartialTicks, 0.0, 1.0, 0.0)
			glScalef(player.width * 10 / 3)
			
			ASJRenderHelper.glColor1u(Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360f, 1f, 1f))
			mc.renderEngine.bindTexture(auraTextures[match])
			
			glScaled(0.5)
			
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
			tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
			tes.draw()
			glColor4f(1f, 1f, 1f, 1f)
			
			glAlphaFunc(GL_GREATER, 0.1f)
			glShadeModel(GL_FLAT)
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
			glEnable(GL_LIGHTING)
			glEnable(GL_CULL_FACE)
			glDisable(GL_BLEND)
			glPopMatrix()
		}
		
		if (ModelEntityFlugel.model1 != null && ModelEntityFlugel.model2 != null && ContributorsPrivacyHelper.isCorrect(player, "Hyper_Miko")) {
			glPushMatrix()
			glRotatef(180f, 1f, 0f, 0f)
			glTranslatef(0f, -1.5f, 0f)
			glEnable(GL_LIGHTING)
			// OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0f, 0f)
			if (player.isSneaking) glTranslatef(0f, 0f, -0.25f)
			mc.renderEngine.bindTexture(LibResourceLocations.miko1)
			ModelEntityFlugel.model1.renderAll()
			glPopMatrix()
			
			glPushMatrix()
			
			val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick
			val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick
			val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick
			
			glRotatef(yawOffset, 0f, -1f, 0f)
			glRotatef(yaw - 270, 0f, 1f, 0f)
			glRotatef(pitch, 0f, 0f, 1f)
			
			glRotated(-90.0, 0.0, 1.0, 0.0)
			mc.renderEngine.bindTexture(LibResourceLocations.miko2)
			ModelEntityFlugel.model2.renderAll()
			glPopMatrix()
		}
		
		if (ContributorsPrivacyHelper.isCorrect(player, "MonoShiki")) {
			glPushMatrix()
			glColor4f(1f, 1f, 1f, 1f)
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			Helper.rotateIfSneaking(e.entityPlayer)
			val armor = e.entityPlayer.getCurrentArmor(2) != null
			glRotatef(180f, 1f, 0f, 0f)
			glTranslated(-0.25, -0.4, if (armor) 0.21 else 0.14)
			glScaled(0.5)
			val icon = ItemElvenResource.amulet
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1f / 32f)
			glPopMatrix()
		}
		
		if (book != null && ContributorsPrivacyHelper.isCorrect(player, "Mudresistor")) {
			glPushMatrix()
			glColor4f(1f, 1f, 1f, 1f)
			
			glRotatef(180f, 1f, 0f, 0f)
			glTranslated(-0.5, 1.0, 0.5)
			glRotatef(mc.theWorld.totalWorldTime % 360f, 0f, 1f, 0f)
			// glTranslatef(0f, sin(mc.theWorld.totalWorldTime / 16f % 360f) / 10, 0f)
			glTranslated(-0.45, -0.55, 0.4)
			
			glScaled(0.1)
			mc.renderEngine.bindTexture(LibResourceLocations.palette)
			book.renderAll()
			glPopMatrix()
		}
		
		glColor4f(1f, 1f, 1f, 1f)
	}
}