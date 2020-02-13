package alfheim.client.render.particle

import alfheim.api.ModInfo
import alfheim.client.core.util.mc
import alfheim.common.core.util.*
import net.minecraft.block.BlockLiquid
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.lwjgl.opengl.GL11.*
import java.util.*

class EntityBloodFx(world: World, x: Double, y: Double, z: Double, size: Float, lifetime: Int): EntityFX(world, x, y, z) {
	
	var f0 = 0f
	var f1 = 0f
	var f2 = 0f
	var f3 = 0f
	var f4 = 0f
	var f5 = 0f
	
	init {
		motionY = 0.0
		
		particleRed = 1f
		particleGreen = 0f
		particleBlue = 0f
		
		particleAge = lifetime
		
		setSize(size, size)
		particleGravity = 0.06f
		particleMaxAge = (64.0 / (Math.random() * 0.8 + 0.2)).I
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
	
	fun postRender() {
		if (isDead) return
		Tessellator.instance.setBrightness(getBrightnessForRender(0f))
		val x = (prevPosX + (posX - prevPosX) * f0 - interpPosX).F
		val y = (prevPosY + (posY - prevPosY) * f0 - interpPosY).F
		val z = (prevPosZ + (posZ - prevPosZ) * f0 - interpPosZ).F
		glPushMatrix()
		Tessellator.instance.startDrawingQuads()
		drawBillboard(x.D - 0.90625, y.D, z.D - 0.90625, rotationPitch)
		Tessellator.instance.draw()
		glPopMatrix()
	}
	
	fun drawBillboard(x: Double, y: Double, z: Double, rotation: Float) {
		val scale = 0.05f
		glTranslatef(x.F, y.F, z.F)
		glRotatef(-RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f)
		glRotatef(RenderManager.instance.playerViewX, 1.0f, 0.0f, 0.0f)
		glRotatef(180f, 0f, 0f, 1f)
		glTranslatef(-0.01f, -0.01f, -0.01f)
		glRotatef(rotation, 0f, 0f, 1f)
		glTranslatef(0.01f, 0.01f, 0.01f)
		glScalef(scale, scale, scale)
		glScalef(particleScale, particleScale, particleScale)
		Tessellator.instance.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha * 0.85f)
		Tessellator.instance.addVertexWithUV(-1.0, -1.0, 0.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(-1.0, 1.0, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, -1.0, 0.0, 1.0, 0.0)
	}
	
	override fun onUpdate() {
		prevPosX = posX
		prevPosY = posY
		prevPosZ = posZ
		
		motionY -= particleGravity.D
		
		moveEntity(0.0, motionY, 0.0)
		
		motionY *= 0.9800000190734863
		
		if (particleMaxAge-- <= 0) setDead()
		
		val material = worldObj.getBlock(posX.mfloor(), posY.mfloor(), posZ.mfloor()).material
		
		if ((material.isLiquid || material.isSolid) && posY < (posY + 1 - BlockLiquid.getLiquidHeightPercent(worldObj.getBlockMetadata(posX.mfloor(), posY.mfloor(), posZ.mfloor()))))
			setDead()
	}
	
	companion object {
		
		val texture = ResourceLocation(ModInfo.MODID, "textures/misc/particles/blood.png")
		val textureDrop = ResourceLocation(ModInfo.MODID, "textures/misc/particles/bloodDrop.png")
		val renderQueue: Queue<EntityBloodFx> = ArrayDeque()
		
		fun renderQueue() {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			mc.renderEngine.bindTexture(texture)
			renderQueue.forEach { if (!it.onGround) it.postRender() }
			mc.renderEngine.bindTexture(textureDrop)
			renderQueue.forEach { if (it.onGround) it.postRender() }
			
			renderQueue.clear()
			glDisable(GL_BLEND)
		}
	}
}