package alfheim.client.render.entity

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.spell.EntitySpellFenrirStorm
import alfheim.common.spell.wind.SpellFenrirStorm
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import java.util.*
import kotlin.math.*

object RenderEntityFenrirStorm: Render() {
	
	internal val rand = Random()
	
	init {
		shadowSize = 0.0f
	}
	
	override fun getEntityTexture(entity: Entity) = null
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		val e = entity as EntitySpellFenrirStorm
		
		glPushMatrix()
		glTranslated(x, y, z)
		
		val parts = max(1, min(e.ticksExisted * 2, SpellFenrirStorm.radius.toInt() * 2))
		
		glDisable(GL_TEXTURE_2D)
		glDisable(GL_LIGHTING)
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		glColor4d(0.65, 1.0, 1.0, 1.0)
		
		rand.setSeed((e.entityId + e.ticksExisted / 3).toLong())
		
		for (i in 0..2) {
			glLineWidth(ASJUtilities.randInBounds(1, 3).toFloat())
			lightning(rand, parts, Vector3.zero.copy(), Vector3(ASJUtilities.getLookVec(e)).normalize(), Vector3.zero.copy())
		}
		
		glEnable(GL_LIGHTING)
		glEnable(GL_TEXTURE_2D)
		
		glPopMatrix()
	}
	
	private fun lightning(rand: Random, parts: Int, start: Vector3, dir: Vector3, end: Vector3) {
		class Fork(s: Vector3, d: Vector3, p: Int) {
			val parts = max(p, 1)
			val start = s.copy()
			val dir = d.copy()
		}
		
		val amp = 0.65
		val forks = ArrayList<Fork>()
		
		glBegin(GL_LINE_STRIP)
		for (i in 0 until parts) {
			if (rand.nextInt() % 7 == 0) forks.add(Fork(start, randVec(dir, rand), rand.nextInt(3)))
			start.glVertex().set(end.set(dir).add(rand.nextDouble() * amp * 2.0 - amp, rand.nextDouble() * amp * 2.0 - amp, rand.nextDouble() * amp * 2.0 - amp))
			dir.extend(1.0)
		}
		end.glVertex()
		
		glEnd()
		
		for (fork in forks) {
			lightning(rand, fork.parts, fork.start, fork.dir, fork.start)
		}
	}
	
	private fun randVec(dir: Vector3, rand: Random): Vector3 {
		val amp = 0.65
		return dir.copy().extend(1.0).add(rand.nextDouble() * amp * 2.0 - amp, rand.nextDouble() * amp * 2.0 - amp, rand.nextDouble() * amp * 2.0 - amp)
	}
}