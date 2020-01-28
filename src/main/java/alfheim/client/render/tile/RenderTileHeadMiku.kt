package alfheim.client.render.tile

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelBipedNew
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer
import net.minecraft.tileentity.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL

object RenderTileHeadMiku: TileEntitySkullRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		if (tile is TileEntitySkull) renderTileEntityAt(tile, x, y, z, ticks)
	}
	
	override fun renderTileEntityAt(skull: TileEntitySkull, x: Double, y: Double, z: Double, ticks: Float) {
		val meta = skull.getBlockMetadata() and 7
		var rotation = skull.func_145906_b() * 360 / 16.0f
		bindTexture(LibResourceLocations.miku0)
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		if (meta != 1) {
			when (meta) {
				2    -> glTranslated(x + 0.5, y + 0.25, z + 0.74)
				
				3    -> {
					glTranslated(x + 0.5, y + 0.25, z + 0.26)
					rotation = 180f
				}
				
				4    -> {
					glTranslated(x + 0.74, y + 0.25, z + 0.5)
					rotation = 270f
				}
				
				5    -> {
					glTranslated(x + 0.26, y + 0.25, z + 0.5)
					rotation = 90f
				}
				
				else -> {
					glTranslated(x + 0.26, y + 0.25, z + 0.5)
					rotation = 90f
				}
			}
		} else
			glTranslated(x + 0.5, y, z + 0.5)
		
		glEnable(GL_RESCALE_NORMAL)
		glScaled(-1.0, -1.0, 1.0)
		glEnable(GL_ALPHA_TEST)
		glRotated(rotation.toDouble(), 0.0, 1.0, 0.0)
		ModelBipedNew.model.head.render(0.0625f)
		
		glPopMatrix()
	}
}