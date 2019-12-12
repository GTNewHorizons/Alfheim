package alfheim.client.render.tile

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelBipedNew
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer
import net.minecraft.tileentity.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL

class RenderTileHeadFlugel: TileEntitySkullRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		if (tile is TileEntitySkull) renderTileEntityAt(tile, x, y, z, ticks)
	}
	
	override fun renderTileEntityAt(skull: TileEntitySkull, x: Double, y: Double, z: Double, ticks: Float) {
		render(skull, x, y, z, skull.getBlockMetadata() and 7, skull.func_145906_b() * 360 / 16.0f, skull.func_145904_a())
	}
	
	fun render(skull: TileEntitySkull, x: Double, y: Double, z: Double, meta: Int, rotation: Float, type: Int) {
		var rotat = rotation
		bindTexture(LibResourceLocations.jibril)
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		if (meta != 1) {
			when (meta) {
				2    -> glTranslated(x + 0.5, y + 0.25, z + 0.74)
				
				3    -> {
					glTranslated(x + 0.5, y + 0.25, z + 0.26)
					rotat = 180f
				}
				
				4    -> {
					glTranslated(x + 0.74, y + 0.25, z + 0.5)
					rotat = 270f
				}
				
				5    -> {
					glTranslated(x + 0.26, y + 0.25, z + 0.5)
					rotat = 90f
				}
				
				else -> {
					glTranslated(x + 0.26, y + 0.25, z + 0.5)
					rotat = 90f
				}
			}
		} else
			glTranslated(x + 0.5, y, z + 0.5)
		
		glEnable(GL_RESCALE_NORMAL)
		glScaled(-1.0, -1.0, 1.0)
		glEnable(GL_ALPHA_TEST)
		glRotated(rotat.toDouble(), 0.0, 1.0, 0.0)
		ModelBipedNew.model.head.render(0.0625f)
		
		glPopMatrix()
	}
}