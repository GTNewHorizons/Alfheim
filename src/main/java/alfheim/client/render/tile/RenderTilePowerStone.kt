package alfheim.client.render.tile

import alexsocol.asjlib.mc
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.block.tile.TilePowerStone
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

object RenderTilePowerStone: TileEntitySpecialRenderer() {
	
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/obelisc.obj"))!!
	
	var forceMeta = 0
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TilePowerStone) return
		
		val meta = when {
			tile.worldObj == null -> forceMeta
			tile.cooldown > 0     -> 0
			else                  -> max(0, min(tile.getBlockMetadata(), LibResourceLocations.obelisk.size - 1))
		}
		
		glPushMatrix()
		glTranslated(x + 0.5, y, z + 0.5)
		mc.renderEngine.bindTexture(LibResourceLocations.obelisk[meta])
		if (model == null) {
			// FIXME cubic model
		} else model.renderAll()
		
		glPopMatrix()
	}
}