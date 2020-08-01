package alfheim.client.render.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.TileItemContainer
import alfheim.api.lib.LibResourceLocations
import alfheim.common.block.tile.TileTransferer
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.model.ModelSpreader
import kotlin.math.sin

object RenderTileTransferer: TileEntitySpecialRenderer() {
	
	val model = ModelSpreader()
	
	override fun renderTileEntityAt(tile: TileEntity, d0: Double, d1: Double, d2: Double, ticks: Float) {
		if (tile !is TileTransferer) return
		
		glPushMatrix()
		glEnable(GL_RESCALE_NORMAL)
		glColor4f(1f, 1f, 1f, 1f)
		glTranslated(d0, d1, d2)
		
		glTranslatef(0.5f, 1.5f, 0.5f)
		
		TileItemContainer.renderItem(tile)
		glRotatef(tile.rotationX + 90f, 0f, 1f, 0f)
		glTranslatef(0f, -1f, 0f)
		glRotatef(tile.rotationY, 1f, 0f, 0f)
		glTranslatef(0f, 1f, 0f)
		
		mc.renderEngine.bindTexture(LibResourceLocations.spreader)
		glScalef(1f, -1f, -1f)
		
		val time = ClientTickHandler.ticksInGame + ticks
		
		model.render()
		glColor3f(1f, 1f, 1f)
		
		glPushMatrix()
		val worldTicks = if (tile.worldObj == null) time else tile.worldObj.totalWorldTime.F
		glRotatef(worldTicks % 360, 0f, 1f, 0f)
		glTranslatef(0f, sin(worldTicks / 20f) * 0.05f, 0f)
		model.renderCube()
		glPopMatrix()
		
		glEnable(GL_RESCALE_NORMAL)
		glPopMatrix()
	}
}