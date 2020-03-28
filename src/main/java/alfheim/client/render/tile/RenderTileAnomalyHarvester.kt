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
		
		glTranslated(x + 0.5, y - 0.5, z + 0.5)
		
		mc.renderEngine.bindTexture(LibResourceLocations.uberSpreaderFrame)
		ModelSpreaderFrame.render()
		
		glPopMatrix()
	}
}