package alfheim.client.render.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.TileItemContainer
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.block.ModelSimpleAnyavil
import alfheim.common.block.tile.TileAnyavil
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*

object RenderTileAnyavil: TileEntitySpecialRenderer() {
	
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Anyavil.obj"))
	val modelSimple = ModelSimpleAnyavil()
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileAnyavil) return
		
		glPushMatrix()
		glTranslated(x + 0.5, y, z + 0.5)
		glRotated((90 * (tile.blockMetadata + 1)).D, 0.0, 1.0, 0.0)
		
		if (model == null) {
			glPushMatrix()
			glTranslated(0.0, 1.5, 0.0)
			glRotated(180.0, 1.0, 0.0, 0.0)
			mc.renderEngine.bindTexture(LibResourceLocations.anyavil)
			modelSimple.renderAll()
			glPopMatrix()
			glTranslated(0.0, 0.425, 0.0)
		} else {
			glTranslated(0.0, 0.425, 0.0)
			mc.renderEngine.bindTexture(LibResourceLocations.elementiumBlock)
			model.renderAll()
		}
		
		glRotated(90.0, 1.0, 0.0, 0.0)
		glRotated(135.0, 0.0, 0.0, 1.0)
		glTranslated(0.0, 0.07, -0.6)
		glScaled(1.5)
		
		TileItemContainer.renderItem(tile)
		glPopMatrix()
	}
}