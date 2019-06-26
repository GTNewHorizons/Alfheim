package alfheim.client.render.tile

import alfheim.common.block.tile.TileAnimatedTorch
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import vazkii.botania.client.core.handler.ClientTickHandler

import java.util.Random

import org.lwjgl.opengl.GL11.*

class RenderTileAnimatedTorch: TileEntitySpecialRenderer() {
	
	internal val rand = Random()
	
	override fun renderTileEntityAt(te: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		render(te as TileAnimatedTorch, x, y, z, partialTicks)
	}
	
	fun render(tile: TileAnimatedTorch?, x: Double, y: Double, z: Double, partialTicks: Float) {
		val mc = Minecraft.getMinecraft()
		glPushMatrix()
		glTranslated(x, y, z)
		
		val hasWorld = tile != null && tile.worldObj != null
		var wtime = if (!hasWorld) 0 else ClientTickHandler.ticksInGame
		
		if (wtime != 0) {
			rand.setSeed((tile!!.xCoord xor tile.yCoord xor tile.zCoord).toLong())
			wtime += rand.nextInt(360)
		}
		
		val time = if (wtime == 0) 0 else wtime + partialTicks
		val xt = 0.5 + Math.cos(time * 0.05) * 0.025
		val yt = 0.1 + (Math.sin(time * 0.04) + 1) * 0.05
		val zt = 0.5 + Math.sin(time * 0.05) * 0.025
		glTranslated(xt, yt, zt)
		
		glScaled(2.0, 2.0, 2.0)
		glRotated(90.0, 1.0, 0.0, 0.0)
		
		var rotation = 0f
		if (tile != null) {
			rotation = tile.rotation.toFloat()
			if (tile.rotating)
				rotation += (tile.anglePerTick * partialTicks).toFloat()
		}
		
		glRotated(rotation.toDouble(), 0.0, 0.0, 1.0)
		glTranslated(0.0, 0.15, 0.0)
		
		run {
			glDisable(GL_CULL_FACE)
			val entityitem = EntityItem(if (hasWorld) tile!!.worldObj else Minecraft.getMinecraft().theWorld, 0.0, 0.0, 0.0, ItemStack(Blocks.redstone_torch))
			entityitem.hoverStart = 0.0f
			glPushMatrix()
			if (tile != null) Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord))
			RenderItem.renderInFrame = true
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0f, 0.0f)
			RenderItem.renderInFrame = false
			glEnable(GL_CULL_FACE)
			glPopMatrix()
		}
		
		glPopMatrix()
	}
}
