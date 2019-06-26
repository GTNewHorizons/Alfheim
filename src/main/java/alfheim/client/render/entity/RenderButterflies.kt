package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.registry.AlfheimRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.*

import java.util.Random

import org.lwjgl.opengl.GL11.*

object RenderButterflies {
	
	internal val rand = Random()
	
	@SideOnly(Side.CLIENT)
	fun render(render: Render, entity: Entity, x: Double, y: Double, z: Double, partialTicks: Float) {
		var flies = (entity as EntityLivingBase).getActivePotionEffect(AlfheimRegistry.butterShield).getAmplifier() * 32
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_CULL_FACE)
		glDisable(GL_LIGHTING)
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		
		glTranslated(x, y, z)
		val s = Math.max(entity.width, entity.height) / 1.25
		glScaled(s, s, s)
		
		rand.setSeed(entity.getEntityId().toLong())
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.butterfly)
		
		while (flies > 0) {
			glPushMatrix()
			glColor4d(rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, 1.0)
			glTranslated(0.0, if (entity === Minecraft.getMinecraft().thePlayer) -(Minecraft.getMinecraft().thePlayer.defaultEyeHeight * s) else entity.height / s / 2, 0.0)
			glRotated(rand.nextDouble() * 360 + entity.ticksExisted, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1)
			glTranslated(0.0, 0.0, -1.0)
			drawRect()
			glPopMatrix()
			--flies
		}
		
		glEnable(GL_LIGHTING)
		glEnable(GL_CULL_FACE)
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	private fun drawRect() {
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(-0.2, -0.2, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(-0.2, 0.2, 0.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.2, 0.2, 0.0, 1.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.2, -0.2, 0.0, 1.0, 1.0)
		Tessellator.instance.draw()
	}
}