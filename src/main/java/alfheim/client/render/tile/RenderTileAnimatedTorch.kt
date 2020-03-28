package alfheim.client.render.tile

import alexsocol.asjlib.*
import alfheim.common.block.tile.TileAnimatedTorch
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import java.util.*
import kotlin.math.*

object RenderTileAnimatedTorch: TileEntitySpecialRenderer() {
	
	val rand = Random()
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileAnimatedTorch) return
		
		glPushMatrix()
		glTranslated(x, y, z)
		
		val hasWorld = tile.worldObj != null
		var wtime = if (!hasWorld) 0 else ClientTickHandler.ticksInGame
		
		if (wtime != 0) {
			rand.setSeed((tile.xCoord xor tile.yCoord xor tile.zCoord).toLong())
			wtime += rand.nextInt(360)
		}
		
		val time = if (wtime == 0) 0f else wtime + partialTicks
		val xt = 0.5 + cos(time * 0.05) * 0.025
		val yt = 0.1 + (sin(time * 0.04) + 1) * 0.05
		val zt = 0.5 + sin(time * 0.05) * 0.025
		glTranslated(xt, yt, zt)
		
		glScaled(2.0)
		glRotated(90.0, 1.0, 0.0, 0.0)
		
		var rotation = tile.rotation.F
		if (tile.rotating)
			rotation += (tile.anglePerTick * partialTicks).F
		
		glRotated(rotation.D, 0.0, 0.0, 1.0)
		glTranslated(0.0, 0.15, 0.0)
		
		run {
			glDisable(GL_CULL_FACE)
			val entityitem = EntityItem(if (hasWorld) tile.worldObj else mc.theWorld, 0.0, 0.0, 0.0, ItemStack(Blocks.redstone_torch))
			entityitem.hoverStart = 0f
			glPushMatrix()
			Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord))
			RenderItem.renderInFrame = true
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0f, 0f)
			RenderItem.renderInFrame = false
			glEnable(GL_CULL_FACE)
			glPopMatrix()
		}
		
		glPopMatrix()
	}
}
