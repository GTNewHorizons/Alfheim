package alfheim.client.model.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.client.render.entity.RenderEntityFlugel
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.material.ItemElvenResource
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.util.*
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import kotlin.math.sin

object ModelEntityFlugel: ModelBipedNew() {
	
	val model1 = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Miku1.obj"))!!
	val model2 = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Miku2.obj"))!!
	
	override fun render(entity: Entity, time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float) {
		if (entity.dataWatcher?.getWatchableObjectString(10) == "Hatsune Miku") {
			val font = mc.fontRenderer
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			if (model1 != null) {
				glPushMatrix()
				glRotated(180.0, 1.0, 0.0, 0.0)
				glTranslated(0.0, -1.5, 0.0)
				mc.renderEngine.bindTexture(LibResourceLocations.miku1)
				model1.renderAll()
				glPopMatrix()
			}
			
			if (entity is EntityLivingBase && model2 != null) {
				glPushMatrix()
				glRotated(ASJRenderHelper.interpolate(entity.prevRenderYawOffset.D, entity.renderYawOffset.D), 0.0, -1.0, 0.0)
				glRotated(ASJRenderHelper.interpolate(entity.prevRotationYawHead.D, entity.rotationYawHead.D) - 270, 0.0, 1.0, 0.0)
				glRotated(ASJRenderHelper.interpolate(entity.prevRotationPitch.D, entity.rotationPitch.D), 0.0, 0.0, 1.0)
				glRotated(-90.0, 0.0, 1.0, 0.0)
				mc.renderEngine.bindTexture(LibResourceLocations.miku2)
				model2.renderAll()
				glPopMatrix()
			}
			
			mc.renderEngine.bindTexture(LibResourceLocations.miku0)
			super.render(entity, time, amplitude, ticksExisted, yawHead, pitchHead, size)
			glDisable(GL_BLEND)
			
			glPushMatrix()
			glTranslated((leftarm.rotationPointX * size).D, (leftarm.rotationPointY * size).D, (leftarm.rotationPointZ * size).D)
			glRotated((leftarm.rotateAngleZ * (180f / Math.PI.F)).D, 0.0, 0.0, 1.0)
			glRotated((leftarm.rotateAngleY * (180f / Math.PI.F)).D, 0.0, 1.0, 0.0)
			glRotated((leftarm.rotateAngleX * (180f / Math.PI.F)).D, 1.0, 0.0, 0.0)
			var s = 0.01
			glScaled(s)
			
			glPushMatrix()
			glTranslated(19.0, -5.0, font.getStringWidth("01") / -2.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			font.drawString("01", 0, 0, 0xFF0000)
			glPopMatrix()
			
			glPushMatrix()
			glTranslated(19.0, 2.5, (font.getStringWidth("Hatsune Miku.") / -2 / 5).D)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			
			s = 0.175
			glScaled(s)
			font.drawString("Hatsune Miku.", 0, 0, 0xFF0000)
			glPopMatrix()
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glPopMatrix()
			
			return
		}
		
		super.render(entity, time, amplitude, ticksExisted, yawHead, pitchHead, size) // ItemFlightTiara
		renderWings(entity, mc.timer.renderPartialTicks, -0x1)
		if ((entity as? EntityFlugel)?.isUltraMode == true) {
			val color = ASJRenderHelper.addAlpha(0x240935, 180)
			renderWings(entity, mc.timer.renderPartialTicks, color)
			
			if (!mc.isGamePaused)
				spawnParticales(entity, time)
		}
		
		renderHalo(entity, mc.timer.renderPartialTicks)
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		super.setRotationAngles(limbSwing, limbAmpl, ticksExisted, yawHead, pitchHead, size, entity)
		val flugel = entity as EntityFlugel?
		
		if (flugel!!.isCasting) {
			val f6 = 0f
			val f7 = 0f
			rightarm.rotateAngleZ = 0f
			leftarm.rotateAngleZ = 0f
			rightarm.rotateAngleY = -(0.1f - f6 * 0.6f) + head.rotateAngleY
			leftarm.rotateAngleY = 0.1f - f6 * 0.6f + head.rotateAngleY + 0.4f
			rightarm.rotateAngleX = -(Math.PI.F / 2f) + head.rotateAngleX
			leftarm.rotateAngleX = -(Math.PI.F / 2f) + head.rotateAngleX
			rightarm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			leftarm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
			leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
			rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067f) * 0.05f
			leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067f) * 0.05f
		}
	}
	
	fun renderWings(entity: Entity, partialTicks: Float, color: Int) {
		val icon = if (color == -0x1) ItemFlightTiara.wingIcons[0] else ItemElvenResource.flugel
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		
		val flying = !entity.onGround
		
		val rz = if (color == -0x1) 120f else 160f
		var rx = 20f + ((sin((entity.ticksExisted + partialTicks).D * if (flying) 0.4f else 0.2f) + 0.5f) * if (flying) 30f else 5f).F
		if (color != -0x1) rx = 20f + ((sin((entity.ticksExisted + partialTicks).D * 0.05f) + 0.5f) * 5f).F
		val ry = 0f
		val h = 0.4f
		val i = if (color == -0x1) 0.15f else 0.75f
		val s = if (color == -0x1) 1f else 5f
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glDisable(GL_CULL_FACE)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		val f = icon.minU
		val f1 = icon.maxU
		val f2 = icon.minV
		val f3 = icon.maxV
		val sr = 1f / s
		
		if (entity.isSneaking) glRotatef(28.64789f, 1f, 0f, 0f)
		
		ASJRenderHelper.glColor1u(color)
		
		glTranslatef(0f, h, i)
		
		glRotatef(rz, 0f, 0f, 1f)
		glRotatef(rx, 1f, 0f, 0f)
		glRotatef(ry, 0f, 1f, 0f)
		glScalef(s)
		if (color != -0x1) glScaled(1.0, 1.0, 0.5)
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
		if (color != -0x1) glScaled(1.0, 1.0, 2.0)
		glScalef(sr)
		glRotatef(-ry, 0f, 1f, 0f)
		glRotatef(-rx, 1f, 0f, 0f)
		glRotatef(-rz, 0f, 0f, 1f)
		
		glScalef(-1f, 1f, 1f)
		glRotatef(rz, 0f, 0f, 1f)
		glRotatef(rx, 1f, 0f, 0f)
		glRotatef(ry, 0f, 1f, 0f)
		glScalef(s)
		if (color != -0x1) glScaled(1.0, 1.0, 0.5)
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
		if (color != -0x1) glScaled(1.0, 1.0, 2.0)
		glScalef(sr)
		glRotatef(-ry, 1f, 0f, 0f)
		glRotatef(-rx, 1f, 0f, 0f)
		glRotatef(-rz, 0f, 0f, 1f)
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	fun renderHalo(entity: Entity, partialTicks: Float) {
		val e = entity as EntityFlugel
		
		glPushMatrix()
		
		glTranslated(0.0, -(0.12 + if (e.isSneaking) 0.0625 else 0.0), 0.0)
		glRotated(ASJRenderHelper.interpolate(e.prevRenderYawOffset.D, e.renderYawOffset.D), 0.0, -1.0, 0.0)
		glRotated(ASJRenderHelper.interpolate(e.prevRotationYawHead.D, e.rotationYawHead.D) - 270, 0.0, 1.0, 0.0)
		glRotated(ASJRenderHelper.interpolate(e.prevRotationPitch.D, e.rotationPitch.D), 0.0, 0.0, 1.0)
		glRotated(30.0, 1.0, 0.0, -1.0)
		glTranslated(0.1, -0.5, -0.1)
		glRotated((e.ticksExisted + partialTicks).D, 0.0, 1.0, 0.0)
		
		RenderEntityFlugel.so.addTranslation()
		
		glPopMatrix()
	}
	
	fun spawnParticales(flugel: EntityFlugel, partialTicks: Float) {
		val angle = 5 + ((sin((flugel.ticksExisted + partialTicks) * 0.05) + 0.5) * 5)
		
		if (flugel.worldObj.rand.nextInt(40) == 0) {
			val mod = if (flugel.worldObj.rand.nextBoolean()) -1 else 1
			val v = Vector3((Math.random() * 5 + 1) * mod, 0.5, -0.75).rotate(angle * mod - flugel.renderYawOffset, Vector3.oY).add(flugel)
			
			AlfheimCore.proxy.featherFX(flugel.worldObj, v.x, v.y, v.z, 0x240935, 5f, (Math.random() * 0.5 + 1).F, 64f)
		}
	}
}