package advtitles

import alfheim.client.core.util.mc
import alfheim.client.render.particle.EntityBloodFx
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.lwjgl.opengl.GL11.*
import java.util.*

class EntityCoinFx(world: World, x: Double, y: Double, z: Double, size: Float, lifetime: Int, gravity: Float): EntityBloodFx(world, x, y, z, size, lifetime, gravity) {
	
	init {
		particleGreen = 1f
		particleBlue = 1f
	}
	
	override fun renderParticle(tessellator: Tessellator, f0: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		this.f0 = f0
		this.f1 = f1
		this.f2 = f2
		this.f3 = f3
		this.f4 = f4
		this.f5 = f5
		
		renderQueue.add(this)
	}
	
	override fun onUpdate2() = Unit
	
	companion object {
		
		val texture = ResourceLocation("advtitles", "textures/misc/particles/coin.png")
		val renderQueue: Queue<EntityCoinFx> = ArrayDeque()
		
		fun renderQueue() {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			mc.renderEngine.bindTexture(texture)
			renderQueue.forEach { it.postRender() }
			renderQueue.clear()
			glDisable(GL_BLEND)
		}
	}
}