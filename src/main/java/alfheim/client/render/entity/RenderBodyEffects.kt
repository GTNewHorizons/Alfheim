package alfheim.client.render.entity

import alexsocol.asjlib.math.*
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.registry.AlfheimRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.*
import org.lwjgl.opengl.GL11.*
import java.lang.Math.toRadians
import java.util.*
import kotlin.math.*

object RenderButterflies {
	
	internal val rand = Random()
	
	@SideOnly(Side.CLIENT)
	fun render(render: Render, entity: Entity, x: Double, y: Double, z: Double, partialTicks: Float) {
		var flies = (entity as EntityLivingBase).getActivePotionEffect(AlfheimRegistry.butterShield).getAmplifier() * 32
		
		setupGlowingRender()
		
		glTranslated(x, y, z)
		val s = entity.width.coerceAtLeast(entity.height) / 1.25
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
		
		unsetGlowingRender()
	}
}

object RenderShield {
	
	private object Hexagon {
		var yaw = 0.0
		var magic: (obb: OrientedBB) -> Unit = {}
		
		val body = arrayOf(OrientedBB(0.3, 0.3*sqrt(3.0), 0.1), OrientedBB(0.3, 0.3*sqrt(3.0), 0.1), OrientedBB(0.3, 0.3*sqrt(3.0), 0.1))
		
		fun translate(x: Double, y: Double, z: Double) {
			body.forEach { it.translate(x, y, z) }
		}
		
		fun rotateOX(angle: Double) {
			body.forEach { it.rotateOX(angle) }
		}
		
		fun rotateOY(angle: Double) {
			body.forEach { it.rotateOY(angle) }
		}
		
		fun rotateOZ(angle: Double) {
			body.forEach { it.rotateOZ(angle) }
		}
		
		fun draw() {
			body.forEach { it.drawFaces() }
		}
		
		fun makeMagic(x: Double, y: Double, z: Double, oX: Double, oY: Double) {
			val (a, _, c) = Vector3(x, 0.0, z).rotate(yaw, Vector3.oY)
			
			body.forEachIndexed { id, it ->
				it.translate(a, y, c)
				it.rotateOZ(120.0 * id)
				it.rotateOX(oX)
				it.rotateOY(oY)
				it.rotateOY(yaw)
				magic.invoke(it)
				it.rotateOY(-yaw)
				it.rotateOY(-oY)
				it.rotateOX(-oX)
				it.rotateOZ(-120.0 * id)
				it.translate(-a, -y, -c)
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	fun render(render: Render, entity: Entity, x: Double, y: Double, z: Double, partialTicks: Float) {
		setupGlowingRender()
		
		val yOff = y + entity.height / 2 - (if (entity === Minecraft.getMinecraft().thePlayer) 1.62 else 0.0)
		
		glColor3f(0.4f, 0.7f, 1f)
		
		Hexagon.body[0] = OrientedBB(0.3, 0.3*sqrt(3.0), 0.1)
		Hexagon.body[1] = OrientedBB(0.3, 0.3*sqrt(3.0), 0.1)
		Hexagon.body[2] = OrientedBB(0.3, 0.3*sqrt(3.0), 0.1)
		
		Hexagon.translate(x, yOff, z)
		
		val sin60 = sin(toRadians(60.0)) * 0.8
		
		Hexagon.magic = { it.drawFaces() }
		
		for (i in 0..2) {
			Hexagon.yaw = (120.0 * i) + entity.ticksExisted * 2 % 360 + partialTicks
			
			Hexagon.makeMagic(0.0, 0.0, 1.5, 0.0, 0.0)			 // front
			
			Hexagon.makeMagic(0.0, -0.8, 1.3, 30.0, 0.0)		 // bottom
			Hexagon.makeMagic(0.0, 0.8, 1.3, -30.0, 0.0)		 // top
			
			Hexagon.makeMagic(sin60, 0.4, 1.2, -15.0, 30.0)	 	// top left
			Hexagon.makeMagic(-sin60, 0.4, 1.2, -15.0, -30.0)	 // top right
			
			Hexagon.makeMagic(sin60, -0.4, 1.2, 15.0, 30.0)	 	// bottom left
			Hexagon.makeMagic(-sin60, -0.4, 1.2, 15.0, -30.0)	 // bottom right
		}

		Hexagon.translate(-x, -yOff, -z)
		
		glColor3f(1f, 1f, 1f)
		
		unsetGlowingRender()
	}
}

private fun setupGlowingRender() {
	glPushMatrix()
	glEnable(GL_BLEND)
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
	glDisable(GL_CULL_FACE)
	glDisable(GL_LIGHTING)
	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
	glColor4d(1.0, 1.0, 1.0, 1.0)
}

private fun drawRect() {
	Tessellator.instance.startDrawingQuads()
	Tessellator.instance.addVertexWithUV(-0.2, -0.2, 0.0, 0.0, 1.0)
	Tessellator.instance.addVertexWithUV(-0.2, 0.2, 0.0, 0.0, 0.0)
	Tessellator.instance.addVertexWithUV(0.2, 0.2, 0.0, 1.0, 0.0)
	Tessellator.instance.addVertexWithUV(0.2, -0.2, 0.0, 1.0, 1.0)
	Tessellator.instance.draw()
}

private fun unsetGlowingRender() {
	glEnable(GL_LIGHTING)
	glEnable(GL_CULL_FACE)
	glDisable(GL_BLEND)
	glPopMatrix()
}