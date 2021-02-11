package alfheim.api.item

import alexsocol.asjlib.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import java.awt.Color

interface IDoubleBoundItem {
	
	@SideOnly(Side.CLIENT)
	fun getFirstPosition(stack: ItemStack): ChunkCoordinates?
	
	@SideOnly(Side.CLIENT)
	fun getSecondPosition(stack: ItemStack): ChunkCoordinates?
}

interface IRotationDisplay {
	
	/**
	 * @return horizontal rotation [0..30]. -1 means no rotation render
	 */
	@SideOnly(Side.CLIENT)
	fun getRotation(stack: ItemStack): Int
}

object DoubleBoundItemRender {
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun renderWorldLast(e: RenderWorldLastEvent) {
		val player = mc.thePlayer
		val stack = player.currentEquippedItem ?: return
		val item = stack.item
		
		glPushMatrix()
		glPushAttrib(GL_LIGHTING)
		glDisable(GL_CULL_FACE)
		glDisable(GL_DEPTH_TEST)
		glDisable(GL_TEXTURE_2D)
		glDisable(GL_LIGHTING)
		glEnable(GL_BLEND)
		
		Tessellator.renderingWorldRenderer = false
		
		if (item is IDoubleBoundItem) {
			val color1 = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 300 / 300f, 0.6f, 1f)
			val color2 = Color.HSBtoRGB((ClientTickHandler.ticksInGame + 100) % 300 / 300f, 0.6f, 1f)
			
			fun draw(it: ChunkCoordinates, n: String, color: Int) {
				glPushMatrix()
				glEnable(GL_TEXTURE_2D)
				glTranslated(it.posX - RenderManager.renderPosX, it.posY - RenderManager.renderPosY, it.posZ - RenderManager.renderPosZ)
				alexsocol.asjlib.glTranslated(0.5)
				alexsocol.asjlib.glScalef(1 / 16f)
				glTranslatef(mc.fontRenderer.getStringWidth(n) / 2f, mc.fontRenderer.FONT_HEIGHT / 2f, 0f)
				glRotatef(180f, 0f, 0f, 1f)
				mc.fontRenderer.drawString(n, 0, 0, color)
				glDisable(GL_TEXTURE_2D)
				glPopMatrix()
				
				renderBlockOutlineAt(it, color, 1f)
			}
			
			item.getFirstPosition(stack)?.also { draw(it, "1", color1) }
			item.getSecondPosition(stack)?.also { draw(it, "2", color2) }
		}
		
		if (item is IRotationDisplay) {
			val rotation = item.getRotation(stack)
			val mop = mc.objectMouseOver
			
			if (mop != null && rotation != -1) {
				val color = Color.HSBtoRGB((ClientTickHandler.ticksInGame + 200) % 300 / 300f, 0.6f, 1f)
				val side = ForgeDirection.getOrientation(mop.sideHit)
				val tes = Tessellator.instance
				
				val x = mop.blockX + side.offsetX
				val y = mop.blockY + side.offsetY
				val z = mop.blockZ + side.offsetZ
				
				glPushMatrix()
				glScalef(1f, 1f, 1f)
				glTranslated(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ)
				glRotatef(rotation * 90f, 0f, 1f, 0f)
				
				val colorRGB = Color(color)
				glColor4ub(colorRGB.red.toByte(), colorRGB.green.toByte(), colorRGB.blue.toByte(), 255.toByte())
				
				glLineWidth(5f)
				
				when (rotation) {
					1 -> glTranslated(-1.0, 0.0, 0.0)
					2 -> glTranslated(-1.0, 0.0, -1.0)
					3 -> glTranslated(0.0, 0.0, -1.0)
				}
				
				tes.startDrawing(GL_LINES)
				tes.addVertex(0.0, 0.5, 0.0)
				tes.addVertex(1.0, 0.5, 1.0)
				tes.addVertex(1.0, 0.5, 0.75)
				tes.addVertex(1.0, 0.5, 1.0)
				tes.addVertex(0.75, 0.5, 1.0)
				tes.addVertex(1.0, 0.5, 1.0)
				tes.addVertex(1.0, 0.5, 0.75)
				tes.addVertex(0.75, 0.5, 1.0)
				tes.draw()
				
				tes.startDrawing(GL_POLYGON)
				tes.addVertex(1.0, 0.5, 1.0)
				tes.addVertex(1.0, 0.5, 0.75)
				tes.addVertex(0.75, 0.5, 1.0)
				tes.draw()
				
				glPopMatrix()
				
				renderBlockOutlineAt(ChunkCoordinates(x, y, z), color, 1f)
			}
		}
		
		glEnable(GL_DEPTH_TEST)
		glEnable(GL_TEXTURE_2D)
		glDisable(GL_BLEND)
		glEnable(GL_CULL_FACE)
		glPopAttrib()
		glPopMatrix()
	}
	
	fun renderBlockOutlineAt(pos: ChunkCoordinates, color: Int, thickness: Float) {
		glPushMatrix()
		glTranslated(pos.posX - RenderManager.renderPosX, pos.posY - RenderManager.renderPosY, pos.posZ - RenderManager.renderPosZ + 1)
		
		val colorRGB = Color(color)
		glColor4ub(colorRGB.red.toByte(), colorRGB.green.toByte(), colorRGB.blue.toByte(), 255.toByte())
		val world: World = mc.theWorld
		val block = world.getBlock(pos.posX, pos.posY, pos.posZ)
		
		run drawWireframe@{
			if (block != null) {
				val axis: AxisAlignedBB = block.getSelectedBoundingBoxFromPool(world, pos.posX, pos.posY, pos.posZ) ?: return@drawWireframe
				axis.minX -= pos.posX.D
				axis.maxX -= pos.posX.D
				axis.minY -= pos.posY.D
				axis.maxY -= pos.posY.D
				axis.minZ -= pos.posZ + 1.D
				axis.maxZ -= pos.posZ + 1.D
				glScalef(1f, 1f, 1f)
				glLineWidth(thickness)
				renderBlockOutline(axis)
				glLineWidth(thickness + 3f)
				glColor4ub(colorRGB.red.toByte(), colorRGB.green.toByte(), colorRGB.blue.toByte(), 64.toByte())
				renderBlockOutline(axis)
			}
		}
		
		glPopMatrix()
	}
	
	private fun renderBlockOutline(aabb: AxisAlignedBB) {
		val tes = Tessellator.instance
		
		val ix = aabb.minX
		val iy = aabb.minY
		val iz = aabb.minZ
		val ax = aabb.maxX
		val ay = aabb.maxY
		val az = aabb.maxZ
		
		tes.startDrawing(GL_LINES)
		
		tes.addVertex(ix, iy, iz)
		tes.addVertex(ix, ay, iz)
		
		tes.addVertex(ix, ay, iz)
		tes.addVertex(ax, ay, iz)
		
		tes.addVertex(ax, ay, iz)
		tes.addVertex(ax, iy, iz)
		
		tes.addVertex(ax, iy, iz)
		tes.addVertex(ix, iy, iz)
		
		tes.addVertex(ix, iy, az)
		tes.addVertex(ix, ay, az)
		
		tes.addVertex(ix, iy, az)
		tes.addVertex(ax, iy, az)
		
		tes.addVertex(ax, iy, az)
		tes.addVertex(ax, ay, az)
		
		tes.addVertex(ix, ay, az)
		tes.addVertex(ax, ay, az)
		
		tes.addVertex(ix, iy, iz)
		tes.addVertex(ix, iy, az)
		
		tes.addVertex(ix, ay, iz)
		tes.addVertex(ix, ay, az)
		
		tes.addVertex(ax, iy, iz)
		tes.addVertex(ax, iy, az)
		
		tes.addVertex(ax, ay, iz)
		tes.addVertex(ax, ay, az)
		
		tes.draw()
	}
	
}