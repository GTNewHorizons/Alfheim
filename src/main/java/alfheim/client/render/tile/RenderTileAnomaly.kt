package alfheim.client.render.tile

import alexsocol.asjlib.*
import alfheim.common.block.tile.TileAnomaly
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

// Render from Thaumcraft nodes by Azanor
object RenderTileAnomaly: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
		if (tile !is TileAnomaly) return
		
		val mainSTE = tile.subTiles[tile.mainSubTile] ?: return
		
		mainSTE.bindTexture()
		
		glPushMatrix()
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_LIGHTING)
		glDepthMask(false)
		
		glTranslated(mainSTE.x() + 0.5, mainSTE.y() + 0.5, mainSTE.z() + 0.5)
		
		val frame = System.nanoTime().div(40000000L).plus(x).toLong().rem(mainSTE.frames).I
		
		renderFacingStrip(0.0, 0.0, 0.0, 0f, 1f, 1f, mainSTE.frames, mainSTE.strip, frame, partialTicks, mainSTE.color)
		
		glDepthMask(true)
		glEnable(GL_LIGHTING)
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		glPopMatrix()
		
	}
	
	private class QuadHelper(val angle: Double, val x: Double, val y: Double, val z: Double) {
		
		fun rotate(vec: Vec3) {
			val d = -x * vec.xCoord - y * vec.yCoord - z * vec.zCoord
			val d1 = angle * vec.xCoord + y * vec.zCoord - z * vec.yCoord
			val d2 = angle * vec.yCoord - x * vec.zCoord + z * vec.xCoord
			val d3 = angle * vec.zCoord + x * vec.yCoord - y * vec.xCoord
			vec.xCoord = d1 * angle - d * x - d2 * z + d3 * y
			vec.yCoord = d2 * angle - d * y + d1 * z - d3 * x
			vec.zCoord = d3 * angle - d * z - d1 * y + d2 * x
		}
		
		companion object {
			
			fun setAxis(vec: Vec3, ang: Double): QuadHelper {
				val angle = ang * 0.5
				val d4 = sin(angle)
				return QuadHelper(cos(angle), vec.xCoord * d4, vec.yCoord * d4, vec.zCoord * d4)
			}
		}
	}
	
	fun renderFacingStrip(px: Double, py: Double, pz: Double, angle: Float, scale: Float, alpha: Float, frames: Int, strip: Int, frame: Int, partialTicks: Float, color: Int) {
		if (mc.renderViewEntity is EntityPlayer) {
			val tessellator = Tessellator.instance
			val arX = ActiveRenderInfo.rotationX
			val arZ = ActiveRenderInfo.rotationZ
			val arYZ = ActiveRenderInfo.rotationYZ
			val arXY = ActiveRenderInfo.rotationXY
			val arXZ = ActiveRenderInfo.rotationXZ
			val player = mc.renderViewEntity as EntityPlayer
			val iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks.D
			val iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks.D
			val iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks.D
			glTranslated(-iPX, -iPY, -iPZ)
			tessellator.startDrawingQuads()
			tessellator.setBrightness(220)
			tessellator.setColorRGBA_I(color, (alpha * 255f).I)
			val v1 = Vec3.createVectorHelper((-arX * scale - arYZ * scale).D, (-arXZ * scale).D, (-arZ * scale - arXY * scale).D)
			val v2 = Vec3.createVectorHelper((-arX * scale + arYZ * scale).D, (arXZ * scale).D, (-arZ * scale + arXY * scale).D)
			val v3 = Vec3.createVectorHelper((arX * scale + arYZ * scale).D, (arXZ * scale).D, (arZ * scale + arXY * scale).D)
			val v4 = Vec3.createVectorHelper((arX * scale - arYZ * scale).D, (-arXZ * scale).D, (arZ * scale - arXY * scale).D)
			if (angle != 0f) {
				val f2 = Vec3.createVectorHelper(iPX, iPY, iPZ)
				val f3 = Vec3.createVectorHelper(px, py, pz)
				val f4 = f2.subtract(f3).normalize()
				QuadHelper.setAxis(f4, angle.D).rotate(v1)
				QuadHelper.setAxis(f4, angle.D).rotate(v2)
				QuadHelper.setAxis(f4, angle.D).rotate(v3)
				QuadHelper.setAxis(f4, angle.D).rotate(v4)
			}
			
			val f21 = frame.F / frames.F
			val f31 = (frame + 1).F / frames.F
			val f41 = strip.F / frames.F
			val f5 = (strip.F + 1f) / frames.F
			tessellator.setNormal(0f, 0f, -1f)
			tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, f31.D, f5.D)
			tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, f31.D, f41.D)
			tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, f21.D, f41.D)
			tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, f21.D, f5.D)
			tessellator.draw()
		}
	}
}