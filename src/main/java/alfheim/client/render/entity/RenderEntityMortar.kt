package alfheim.client.render.entity

import alexsocol.asjlib.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.block.ModBlocks

object RenderEntityMortar: Render() {
	
	init {
		shadowSize = 0.5f
	}
	
	override fun getEntityTexture(entity: Entity) = TextureMap.locationBlocksTexture!!
	
	override fun doRender(e: Entity, x: Double, y: Double, z: Double, yaw: Float, ticks: Float) {
		glPushMatrix()
		glDisable(GL_LIGHTING)
		glTranslated(x, y, z)
		bindEntityTexture(e)
		renderBlocks.setRenderBoundsFromBlock(ModBlocks.livingrock)
		renderBlocks.renderBlockSandFalling(ModBlocks.livingrock, e.worldObj, e.posX.mfloor(), e.posY.mfloor(), e.posZ.mfloor(), 0)
		glEnable(GL_LIGHTING)
		glPopMatrix()
	}
}
