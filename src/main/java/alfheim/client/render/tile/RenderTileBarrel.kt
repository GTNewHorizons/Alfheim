package alfheim.client.render.tile

import alexsocol.asjlib.mc
import alfheim.api.ModInfo
import alfheim.client.model.block.ModelBarrel
import alfheim.common.block.tile.TileBarrel
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

object RenderTileBarrel: TileEntitySpecialRenderer() {
	
	val model = ModelBarrel()
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileBarrel) return
		
		val f5 = 0.0625f
		
		glPushMatrix()
		glTranslated(x, y, z)
		glRotatef(180f, 1f, 0f, 0f)
		glTranslated(0.5, -1.0, -0.5)
		glRotatef(90f, 0f, 1f, 0f)
		
		mc.renderEngine.bindTexture(ResourceLocation(ModInfo.MODID, "textures/model/block/Barrel.png"))
		model.render(f5)
		
		if (tile.closed)
			model.renderCover(f5)
		
		glTranslatef(0f, (tile.vineLevel - 2) / -16f - 0.01f, 0f)
		
		 if (tile.vineStage <= TileBarrel.VINE_STAGE_MASH) {
		 	if (tile.vineType == TileBarrel.VINE_TYPE_RED)
		 		model.redMash.render(f5)

		 	if (tile.vineType == TileBarrel.VINE_TYPE_WHITE)
		 		model.greenMash.render(f5)
		 }
		
		if (tile.vineStage >= TileBarrel.VINE_STAGE_MASH) {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glTranslatef(0f, -1/64f, 0f)
			
			val a = when (tile.vineStage) {
				TileBarrel.VINE_STAGE_MASH -> 0.5f
				TileBarrel.VINE_STAGE_LIQUID -> 1f
				TileBarrel.VINE_STAGE_READY -> 0.9f
				else -> -1f
			}
			
			glColor4f(1f, 1f, 1f, a)
			
			if (tile.vineType == TileBarrel.VINE_TYPE_RED)
				model.redVine.render(f5)
			
			if (tile.vineType == TileBarrel.VINE_TYPE_WHITE)
				model.greenVine.render(f5)
			
			glDisable(GL_BLEND)
			
			glColor4f(1f, 1f, 1f, 1f)
		}
		
		glPopMatrix()
	}
}