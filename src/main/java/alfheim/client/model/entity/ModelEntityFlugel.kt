package alfheim.client.model.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.client.render.entity.RenderEntityFlugel
import alfheim.common.entity.boss.EntityFlugel
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.util.*
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import kotlin.math.sin

class ModelEntityFlugel: ModelBipedNew() {
	
	override fun render(e: Entity?, time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float) {
		if (e?.dataWatcher?.getWatchableObjectString(10) == "Hatsune Miku") {
			val font = Minecraft.getMinecraft().fontRenderer
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glPushMatrix()
			glRotated(180.0, 1.0, 0.0, 0.0)
			glTranslated(0.0, -1.5, 0.0)
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.miku1)
			model1.renderAll()
			glPopMatrix()
			
			if (e is EntityLivingBase) {
				glPushMatrix()
				glRotated(ASJUtilities.interpolate(e.prevRenderYawOffset.toDouble(), e.renderYawOffset.toDouble()), 0.0, -1.0, 0.0)
				glRotated(ASJUtilities.interpolate(e.prevRotationYawHead.toDouble(), e.rotationYawHead.toDouble()) - 270, 0.0, 1.0, 0.0)
				glRotated(ASJUtilities.interpolate(e.prevRotationPitch.toDouble(), e.rotationPitch.toDouble()), 0.0, 0.0, 1.0)
				glRotated(-90.0, 0.0, 1.0, 0.0)
				Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.miku2)
				model2.renderAll()
				glPopMatrix()
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.miku0)
			super.render(e, time, amplitude, ticksExisted, yawHead, pitchHead, size)
			glDisable(GL_BLEND)
			
			glPushMatrix()
			glTranslated((leftarm.rotationPointX * size).toDouble(), (leftarm.rotationPointY * size).toDouble(), (leftarm.rotationPointZ * size).toDouble())
			glRotated((leftarm.rotateAngleZ * (180f / Math.PI.toFloat())).toDouble(), 0.0, 0.0, 1.0)
			glRotated((leftarm.rotateAngleY * (180f / Math.PI.toFloat())).toDouble(), 0.0, 1.0, 0.0)
			glRotated((leftarm.rotateAngleX * (180f / Math.PI.toFloat())).toDouble(), 1.0, 0.0, 0.0)
			var s = 0.01
			glScaled(s, s, s)
			
			glPushMatrix()
			glTranslated(19.0, -5.0, font.getStringWidth("01") / -2.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			font.drawString("01", 0, 0, 0xFF0000)
			glPopMatrix()
			
			glPushMatrix()
			glTranslated(19.0, 2.5, (font.getStringWidth("Hatsune Miku.") / -2 / 5).toDouble())
			glRotated(-90.0, 0.0, 1.0, 0.0)
			
			s = 0.175
			glScaled(s, s, s)
			font.drawString("Hatsune Miku.", 0, 0, 0xFF0000)
			glPopMatrix()
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glPopMatrix()
			
			return
		}
		
		super.render(e, time, amplitude, ticksExisted, yawHead, pitchHead, size) // ItemFlightTiara
		if (e != null) {
			renderWings(e, Minecraft.getMinecraft().timer.renderPartialTicks)
			renderHalo(e, Minecraft.getMinecraft().timer.renderPartialTicks)
		}
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		super.setRotationAngles(limbSwing, limbAmpl, ticksExisted, yawHead, pitchHead, size, entity)
		val flugel = entity as EntityFlugel?
		
		if (flugel!!.isCasting) {
			val f6 = 0.0f
			val f7 = 0.0f
			rightarm.rotateAngleZ = 0.0f
			leftarm.rotateAngleZ = 0.0f
			rightarm.rotateAngleY = -(0.1f - f6 * 0.6f) + head.rotateAngleY
			leftarm.rotateAngleY = 0.1f - f6 * 0.6f + head.rotateAngleY + 0.4f
			rightarm.rotateAngleX = -(Math.PI.toFloat() / 2f) + head.rotateAngleX
			leftarm.rotateAngleX = -(Math.PI.toFloat() / 2f) + head.rotateAngleX
			rightarm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			leftarm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
			leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
			rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067f) * 0.05f
			leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067f) * 0.05f
		}
	}
	
	fun renderWings(entity: Entity, partialTicks: Float) {
		val icon = ItemFlightTiara.wingIcons[0]
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
		
		val flying = !entity.onGround
		
		val rz = 120f
		val rx = 20f + ((sin((entity.ticksExisted + partialTicks).toDouble() * if (flying) 0.4f else 0.2f) + 0.5f) * if (flying) 30f else 5f).toFloat()
		val ry = 0f
		val h = 0.4f
		val i = 0.15f
		val s = 1f
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		val f = icon.minU
		val f1 = icon.maxU
		val f2 = icon.minV
		val f3 = icon.maxV
		val sr = 1f / s
		
		if (entity.isSneaking) glRotatef(28.64789f, 1.0f, 0.0f, 0.0f)
		
		glTranslatef(0f, h, i)
		
		glRotatef(rz, 0f, 0f, 1f)
		glRotatef(rx, 1f, 0f, 0f)
		glRotatef(ry, 0f, 1f, 0f)
		glScalef(s, s, s)
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
		glScalef(sr, sr, sr)
		glRotatef(-ry, 0f, 1f, 0f)
		glRotatef(-rx, 1f, 0f, 0f)
		glRotatef(-rz, 0f, 0f, 1f)
		
		glScalef(-1f, 1f, 1f)
		glRotatef(rz, 0f, 0f, 1f)
		glRotatef(rx, 1f, 0f, 0f)
		glRotatef(ry, 0f, 1f, 0f)
		glScalef(s, s, s)
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 32f)
		glScalef(sr, sr, sr)
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
		glRotated(ASJUtilities.interpolate(e.prevRenderYawOffset.toDouble(), e.renderYawOffset.toDouble()), 0.0, -1.0, 0.0)
		glRotated(ASJUtilities.interpolate(e.prevRotationYawHead.toDouble(), e.rotationYawHead.toDouble()) - 270, 0.0, 1.0, 0.0)
		glRotated(ASJUtilities.interpolate(e.prevRotationPitch.toDouble(), e.rotationPitch.toDouble()), 0.0, 0.0, 1.0)
		glRotated(30.0, 1.0, 0.0, -1.0)
		glTranslated(0.1, -0.5, -0.1)
		glRotated((e.ticksExisted + partialTicks).toDouble(), 0.0, 1.0, 0.0)
		
		RenderEntityFlugel.so.addTranslation()
		
		glPopMatrix()
	}
	
	companion object {
		val model1 = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Miku1.obj"))!!
		val model2 = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Miku2.obj"))!!
	}
}