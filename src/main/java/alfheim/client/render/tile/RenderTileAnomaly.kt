package alfheim.client.render.tile

import org.lwjgl.opengl.GL11.*

import alfheim.api.block.tile.SubTileEntity
import alfheim.common.block.tile.TileAnomaly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3

// Render from Thaumcraft nodes by Azanor
class RenderTileAnomaly: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileAnomaly) return
		
		val mainSTE = tile.subTiles[tile.mainSubTile] ?: return
		
		val pt = Minecraft.getMinecraft().timer.renderPartialTicks
		mainSTE.bindTexture()
		
		glPushMatrix()
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_LIGHTING)
		glDepthMask(false)
		
		glTranslated(mainSTE.x() + 0.5, mainSTE.y() + 0.5, mainSTE.z() + 0.5)
		
		val frame = ((System.nanoTime() / 40000000L + x) % mainSTE.frames).toInt()
		
		renderFacingStrip(0.0, 0.0, 0.0, 0f, 1f, 1f, mainSTE.frames, mainSTE.strip, frame, partialTicks, mainSTE.color)
		
		glDepthMask(true)
		glEnable(GL_LIGHTING)
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		glPopMatrix()
		
	}
	
	private class QuadHelper(val angle: Double, val x: Double, val y: Double, val z: Double) {
		
		fun rotate(vec: Vec3) {
			val d = -this.x * vec.xCoord - this.y * vec.yCoord - this.z * vec.zCoord
			val d1 = this.angle * vec.xCoord + this.y * vec.zCoord - this.z * vec.yCoord
			val d2 = this.angle * vec.yCoord - this.x * vec.zCoord + this.z * vec.xCoord
			val d3 = this.angle * vec.zCoord + this.x * vec.yCoord - this.y * vec.xCoord
			vec.xCoord = d1 * this.angle - d * this.x - d2 * this.z + d3 * this.y
			vec.yCoord = d2 * this.angle - d * this.y + d1 * this.z - d3 * this.x
			vec.zCoord = d3 * this.angle - d * this.z - d1 * this.y + d2 * this.x
		}
		
		companion object {
			
			fun setAxis(vec: Vec3, angle: Double): QuadHelper {
				var angle = angle
				angle *= 0.5
				val d4 = MathHelper.sin(angle.toFloat()).toDouble()
				return QuadHelper(MathHelper.cos(angle.toFloat()).toDouble(), vec.xCoord * d4, vec.yCoord * d4, vec.zCoord * d4)
			}
		}
	}
	
	companion object {
		
		fun renderFacingStrip(px: Double, py: Double, pz: Double, angle: Float, scale: Float, alpha: Float, frames: Int, strip: Int, frame: Int, partialTicks: Float, color: Int) {
			if (Minecraft.getMinecraft().renderViewEntity is EntityPlayer) {
				val tessellator = Tessellator.instance
				val arX = ActiveRenderInfo.rotationX
				val arZ = ActiveRenderInfo.rotationZ
				val arYZ = ActiveRenderInfo.rotationYZ
				val arXY = ActiveRenderInfo.rotationXY
				val arXZ = ActiveRenderInfo.rotationXZ
				val player = Minecraft.getMinecraft().renderViewEntity as EntityPlayer
				val iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks.toDouble()
				val iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks.toDouble()
				val iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks.toDouble()
				glTranslated(-iPX, -iPY, -iPZ)
				tessellator.startDrawingQuads()
				tessellator.setBrightness(220)
				tessellator.setColorRGBA_I(color, (alpha * 255.0f).toInt())
				val v1 = Vec3.createVectorHelper((-arX * scale - arYZ * scale).toDouble(), (-arXZ * scale).toDouble(), (-arZ * scale - arXY * scale).toDouble())
				val v2 = Vec3.createVectorHelper((-arX * scale + arYZ * scale).toDouble(), (arXZ * scale).toDouble(), (-arZ * scale + arXY * scale).toDouble())
				val v3 = Vec3.createVectorHelper((arX * scale + arYZ * scale).toDouble(), (arXZ * scale).toDouble(), (arZ * scale + arXY * scale).toDouble())
				val v4 = Vec3.createVectorHelper((arX * scale - arYZ * scale).toDouble(), (-arXZ * scale).toDouble(), (arZ * scale - arXY * scale).toDouble())
				if (angle != 0.0f) {
					val f2 = Vec3.createVectorHelper(iPX, iPY, iPZ)
					val f3 = Vec3.createVectorHelper(px, py, pz)
					val f4 = f2.subtract(f3).normalize()
					QuadHelper.setAxis(f4, angle.toDouble()).rotate(v1)
					QuadHelper.setAxis(f4, angle.toDouble()).rotate(v2)
					QuadHelper.setAxis(f4, angle.toDouble()).rotate(v3)
					QuadHelper.setAxis(f4, angle.toDouble()).rotate(v4)
				}
				
				val f21 = frame.toFloat() / frames.toFloat()
				val f31 = (frame + 1).toFloat() / frames.toFloat()
				val f41 = strip.toFloat() / frames.toFloat()
				val f5 = (strip.toFloat() + 1.0f) / frames.toFloat()
				tessellator.setNormal(0.0f, 0.0f, -1.0f)
				tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, f31.toDouble(), f5.toDouble())
				tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, f31.toDouble(), f41.toDouble())
				tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, f21.toDouble(), f41.toDouble())
				tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, f21.toDouble(), f5.toDouble())
				tessellator.draw()
			}
		}
	}
}