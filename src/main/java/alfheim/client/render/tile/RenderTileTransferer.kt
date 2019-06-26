package alfheim.client.render.tile

import alexsocol.asjlib.extendables.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*

import alfheim.api.lib.LibResourceLocations
import alfheim.common.block.tile.TileTransferer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.model.ModelSpreader
import kotlin.math.sin

class RenderTileTransferer: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tileentity: TileEntity, d0: Double, d1: Double, d2: Double, ticks: Float) {
		val spreader = tileentity as TileTransferer
		glPushMatrix()
		glEnable(GL_RESCALE_NORMAL)
		glColor4f(1f, 1f, 1f, 1f)
		glTranslated(d0, d1, d2)
		
		glTranslatef(0.5f, 1.5f, 0.5f)
		
		TileItemContainer.renderItem(spreader)
		glRotatef(spreader.rotationX + 90f, 0f, 1f, 0f)
		glTranslatef(0f, -1f, 0f)
		glRotatef(spreader.rotationY, 1f, 0f, 0f)
		glTranslatef(0f, 1f, 0f)
		
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.spreader)
		glScalef(1f, -1f, -1f)
		
		val time = (ClientTickHandler.ticksInGame + ticks).toDouble()
		
		model.render()
		glColor3f(1f, 1f, 1f)
		
		glPushMatrix()
		val worldTicks = if (tileentity.getWorldObj() == null) 0.0 else time
		glRotatef(worldTicks.toFloat() % 360, 0f, 1f, 0f)
		glTranslatef(0f, sin(worldTicks / 20.0).toFloat() * 0.05f, 0f)
		model.renderCube()
		glPopMatrix()
		
		glEnable(GL_RESCALE_NORMAL)
		glPopMatrix()
	}
	
	companion object {
		
		private val model = ModelSpreader()
	}
}