package alfheim.client.render.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.TileItemContainer
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.block.ModelSimpleItemHolder
import alfheim.common.block.tile.TileManaAccelerator
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.*
import vazkii.botania.common.block.tile.mana.TilePool
import java.awt.Color
import java.util.*

object RenderTileManaAccelerator: TileEntitySpecialRenderer() {
	
	val rand = Random()
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/holder.obj"))!!
	val modelSimple = ModelSimpleItemHolder()
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileManaAccelerator) return
		
		var inf = false
		var dil = false
		var fab = false
		var c = 0
		run {
			if (tile.worldObj == null) return@run
			val meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord - 1, tile.zCoord)
			inf = meta == 1
			dil = meta == 2
			fab = meta == 3
			
			val te = tile.worldObj.getTileEntity(tile.xCoord, tile.yCoord - 1, tile.zCoord)
			if (te is TilePool) c = te.color
		}
		
		glPushMatrix()
		glTranslated(x + 0.5, y - 0.5, z + 0.5)
		
		mc.renderEngine.bindTexture(if (inf) LibResourceLocations.poolPink else if (dil) LibResourceLocations.poolBlue else LibResourceLocations.livingrock)
		
		if (fab) {
			var time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks
			rand.setSeed((tile.xCoord xor tile.yCoord - 1 xor tile.zCoord).toLong())
			time += rand.nextInt(100000).F
			
			val color = Color.getHSBColor(time * 0.005f, 0.6f, 1f)
			glColor4ub(color.red.toByte(), color.green.toByte(), color.blue.toByte(), 255.toByte())
		} else {
			val acolor = EntitySheep.fleeceColorTable[c]
			glColor4f(acolor[0], acolor[1], acolor[2], if (MultiblockRenderHandler.rendering) 0.6f else 1f)
		}
		
		if (model == null) {
			glPushMatrix()
			glTranslated(0.0, 1.0, 0.0)
			glRotated(180.0, 1.0, 0.0, 0.0)
			modelSimple.renderAll()
			glPopMatrix()
		} else
			model.renderAll()
		
		glRotated(90.0, 1.0, 0.0, 0.0)
		glRotated(135.0, 0.0, 0.0, 1.0)
		glTranslated(0.0, 0.05, -0.33)
		
		TileItemContainer.renderItem(tile)
		glPopMatrix()
	}
}