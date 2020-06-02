package alfheim.client.render.tile

import alexsocol.asjlib.mc
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.block.ModelSpreaderFrame
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11.*

object RenderTileAnomalyHarvester: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity?, x: Double, y: Double, z: Double, ticks: Float) {
		glPushMatrix()
		
		glTranslated(x + 0.5, y + 0.5, z + 0.5)
		
		val meta = tile?.worldObj?.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) ?: 0
		
		when (meta) {
			0 -> glRotatef(90f, 1f, 0f, 0f)
			1 -> glRotatef(-90f, 1f, 0f, 0f)
			2 -> glRotatef(180f, 0f, 1f, 0f)
			3 -> Unit
			4 -> glRotatef(-90f, 0f, 1f, 0f)
			5 -> glRotatef(90f, 0f, 1f, 0f)
		}
		
		glTranslatef(0f, -1f, 0f)
		mc.renderEngine.bindTexture(LibResourceLocations.uberSpreaderFrame)
		ModelSpreaderFrame.render()
		
		glPopMatrix()
	}
}