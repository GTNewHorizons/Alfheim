package alfheim.client.render.entity

import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.block.ModBlocks

class RenderEntityMortar: Render() {
	
	internal val render = RenderBlocks()
	
	init {
		shadowSize = 0.5f
	}
	
	public override fun getEntityTexture(entity: Entity): ResourceLocation {
		return TextureMap.locationBlocksTexture
	}
	
	override fun doRender(e: Entity, x: Double, y: Double, z: Double, yaw: Float, ticks: Float) {
		glPushMatrix()
		glDisable(GL_LIGHTING)
		glTranslated(x, y, z)
		bindEntityTexture(e)
		render.setRenderBoundsFromBlock(ModBlocks.livingrock)
		render.renderBlockSandFalling(ModBlocks.livingrock, e.worldObj, MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posY), MathHelper.floor_double(e.posZ), 0)
		glEnable(GL_LIGHTING)
		glPopMatrix()
	}
}
