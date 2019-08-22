package alfheim.client.render.entity

import alexsocol.asjlib.render.*
import alexsocol.asjlib.render.ASJShaderHelper.ShaderCallback
import alfheim.api.ModInfo
import alfheim.api.lib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ItemElvenResource
import alfheim.common.item.relic.ItemTankMask
import baubles.common.lib.PlayerHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBook
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.model.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL20.*
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.item.IBaubleRender.Helper
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import java.awt.Color
import kotlin.math.sin

object RenderContributors {
	
	lateinit var model: IModelCustom
	
	private val callback: ShaderCallback = object: ShaderCallback() {
		override fun call(shaderID: Int) {
			val r = Minecraft.getMinecraft().renderEngine
			
			val explosionUniform = glGetUniformLocation(shaderID, "explosion")
			val gravelUniform = glGetUniformLocation(shaderID, "gravel")
			
			OpenGlHelper.setActiveTexture(GL_TEXTURE0)
			r.bindTexture(LibResourceLocations.explosion)
			glUniform1i(explosionUniform, 0)
			
			OpenGlHelper.setActiveTexture(GL_TEXTURE0 + ConfigHandler.glSecondaryTextureUnit)
			glEnable(GL_TEXTURE_2D)
			glGetInteger(GL_TEXTURE_BINDING_2D)
			r.bindTexture(LibResourceLocations.gravel)
			glUniform1i(gravelUniform, ConfigHandler.glSecondaryTextureUnit)
			
			OpenGlHelper.setActiveTexture(GL_TEXTURE0)
		}
	}
	
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
	
	init {
		RenderPostShaders.registerShadedObject(so)
	}
	
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		if (player.isInvisible || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) || player.isPotionActive(Potion.invisibility)) return
		if (player == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || !AlfheimConfigHandler.fancies) return
		
		if (player.commandSenderName == "AlexSocol") {
			run { // jojo's mask
				if (PlayerHandler.getPlayerBaubles(player)?.getStackInSlot(0)?.item !== AlfheimItems.mask) {
					val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick
					val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick
					val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick
					
					glPushMatrix()
					glRotatef(yawOffset, 0f, -1f, 0f)
					glRotatef(yaw - 270, 0f, 1f, 0f)
					glRotatef(pitch, 0f, 0f, 1f)
					glColor4f(0.375f, 0f, 0f, 1f)
					val mask = ItemStack(AlfheimItems.mask)
					mask.setStackDisplayName("kono dio da")
					(AlfheimItems.mask as ItemTankMask).onPlayerBaubleRender(mask, e, IBaubleRender.RenderType.HEAD)
					
					glPopMatrix()
				}
			}
			
			run { // devil wings
				if (AlfheimConfigHandler.minimalGraphics) {
					glDisable(GL_CULL_FACE)
					(ModItems.flightTiara as ItemFlightTiara).onPlayerBaubleRender(ItemStack(ModItems.flightTiara, 1, 6), e, IBaubleRender.RenderType.BODY)
				}
			}
			
			run { // babylon circle
				glPushMatrix()
				glRotated(90.0, 1.0, 0.0, 0.0)
				Helper.rotateIfSneaking(player)
				glTranslated(0.0, 0.15, -0.25)
				
				glRotated(Minecraft.getMinecraft().theWorld.totalWorldTime / 2.0 + Minecraft.getMinecraft().timer.renderPartialTicks, 0.0, 1.0, 0.0)
				glScaled(0.2, 0.2, 0.2)
				
				so.addTranslation()
				glPopMatrix()
			}
			
			run { // wings
				if (AlfheimConfigHandler.minimalGraphics) {
					val icon = ItemElvenResource.wing
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
					
					val flying = player.capabilities.isFlying
					
					val rz = 120f
					val rx = 20f + ((sin((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble() * if (flying) 0.4f else 0.2f) + 0.5f) * if (flying) 30f else 5f).toFloat()
					val ry = 0f
					val h = 0.2f
					val i = 0.15f
					
					glPushMatrix()
					glEnable(GL_BLEND)
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
					glColor4d(1.0, 1.0, 1.0, 1.0)
					
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
					
					glColor4d(1.0, 1.0, 1.0, 1.0)
					glPopMatrix()
				} else {
					glPushMatrix()
					Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine3)
					glColor3d(0.1, 0.1, 0.1)
					
					glRotated(90.0, 1.0, 0.0, 0.0)
					Helper.rotateIfSneaking(player)
					glTranslated(0.0, 0.15, -0.25)
					
					glRotated(90.0, 1.0, 0.0, 0.0)
					val s = 2.0
					val spd = 0.5
					glScaled(s, s, s)
					
					player.sendPlayerAbilities()
					val flying = player.capabilities.isFlying
					val ry = (if (flying) 30 else 15) + (sin((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble() * spd * if (flying) 0.4 else 0.2) + 0.5) * if (flying) 25.0 else 5.0
					
					if (!::model.isInitialized) {
						model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/wing.obj"))
					}
					
					run { // bones
						glPushMatrix()
						glRotated(-ry, 0.0, 1.0, 0.0)
						glTranslated(-0.35, 0.05, 0.025)
						model.renderAllExcept("leather")
						glPopMatrix()
						
						glPushMatrix()
						glRotated(180.0, 0.0, 1.0, 0.0)
						glRotated(ry, 0.0, 1.0, 0.0)
						glTranslated(-0.35, 0.05, -0.025)
						model.renderAllExcept("leather")
						glPopMatrix()
					}
					
					glColor3d(1.0, 1.0, 1.0)
					glDisable(GL_CULL_FACE)
					if (ConfigHandler.useShaders)
						ASJShaderHelper.useShader(LibShaderIDs.idFire, callback)
					else {
						glDisable(GL_LIGHTING)
						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
						Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.explosion)
					}
					
					run { // leather
						glPushMatrix()
						glRotated(-ry, 0.0, 1.0, 0.0)
						glTranslated(-0.35, 0.05, 0.025)
						model.renderPart("leather")
						glPopMatrix()
						
						glPushMatrix()
						glRotated(180.0, 0.0, 1.0, 0.0)
						glRotated(ry, 0.0, 1.0, 0.0)
						glTranslated(-0.35, 0.05, -0.025)
						model.renderPart("leather")
						glPopMatrix()
					}
					
					if (ConfigHandler.useShaders)
						ASJShaderHelper.releaseShader()
					else
						glEnable(GL_LIGHTING)
					//glEnable(GL_CULL_FACE); for some reason this breaks tiara wings render -_-
					glColor4d(1.0, 1.0, 1.0, 1.0)
					glPopMatrix()
				}
			}
		}
		
		if (player.commandSenderName == "DmitryWS") {
			glPushMatrix()
			glEnable(GL_CULL_FACE)
			val t = sin((Minecraft.getMinecraft().theWorld.totalWorldTime + Minecraft.getMinecraft().timer.renderPartialTicks) / 10.0)
			
			glTranslated(0.0, -(0.9 + t * 0.05), 0.0)
			glRotated(180.0, 1.0, 0.0, 0.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			glRotated(60.0, 0.0, 0.0, 1.0)
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.lexica)
			val model = ModelBook()
			model.render(null, 0f, 0.075f + (t * 0.025).toFloat(), 0.925f - (t * 0.025).toFloat(), 1f, 0.0f, 0.0625f)
			glPopMatrix()
		}
		
		if (player.commandSenderName == "KAIIIAK") {
			glPushMatrix()
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glDisable(GL_CULL_FACE)
			glDisable(GL_LIGHTING)
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
			glShadeModel(GL_SMOOTH)
			
			glTranslated(0.0, 1.35, 0.0)
			glRotated(Minecraft.getMinecraft().theWorld.totalWorldTime / 2.0 + Minecraft.getMinecraft().timer.renderPartialTicks, 0.0, 1.0, 0.0)
			glScaled(2.0, 2.0, 2.0)
			
			ASJRenderHelper.glColor1u(Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360f, 1f, 1f))
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.aura)
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
			tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
			tes.draw()
			glColor4f(1f, 1f, 1f, 1f)
			
			glShadeModel(GL_FLAT)
			glEnable(GL_LIGHTING)
			glEnable(GL_CULL_FACE)
			glDisable(GL_BLEND)
			glPopMatrix()
		}
	}
}