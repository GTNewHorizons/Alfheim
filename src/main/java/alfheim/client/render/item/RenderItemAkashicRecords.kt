package alfheim.client.render.item

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJShaderHelper
import alfheim.api.ModInfo
import alfheim.api.lib.*
import alfheim.common.item.relic.ItemAkashicRecords
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import kotlin.math.*

object RenderItemAkashicRecords: IItemRenderer {
	
	val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/AkashicRecords.obj"))
	
	override fun renderItem(type: IItemRenderer.ItemRenderType, stack: ItemStack, vararg data: Any?) {
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glAlphaFunc(GL_GREATER, 0f)
		glPushMatrix()
		
		val frame = if (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
			val f = ItemNBTHelper.getInt(stack, ItemAkashicRecords.TAG_FRAME, 0)
			if (f == 0) 0f else f + (mc.timer.renderPartialTicks * ItemNBTHelper.getInt(stack, ItemAkashicRecords.TAG_MULT, -1))
		} else 0f
		
		glScalef(1f / 110 / 4)
		glTranslatef(0f, 50f, 0f)
		
		if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
			glRotated(-60.0, cos(Math.toRadians(60.0)), 0.0, -sin(Math.toRadians(60.0)))
			glTranslatef(220f, 0f, 440f)
		} else if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glTranslatef(220f, 330f, 220f)
			glRotatef(-10f, 0f, 0f, 1f)
			glRotatef(-5f, 1f, 0f, 0f)
		}
		
		val renders = arrayOf(LibResourceLocations.akashicCube to "Cylinder.0", LibResourceLocations.akashicCyl to "Cylinder.6")
		
		fun wrap(part: String, f: () -> Unit) {
			glPushMatrix()
			f.invoke()
			model.renderPart(part)
			glPopMatrix()
		}
		
		glColor3f(0.75f, 0.75f, 0.75f)
		renders.forEach { (texture, part) ->
			mc.renderEngine.bindTexture(texture)
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30
				glTranslatef(0f, y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, 1f, 0f)
			}
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30
				glTranslatef(0f, -y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, 1f, 0f)
			}
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30 * 330 / 440 + (frame - 45).coerceIn(0f, 5f) * 2
				val angle = 90f - (frame - 30).coerceIn(0f, 15f) * 6
				glRotatef(angle, 0f, 0f, 1f)
				glTranslatef(0f, y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, 1f, 0f)
			}
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30 * 330 / 440 + (frame - 45).coerceIn(0f, 5f) * 2
				val angle = 90f + (frame - 30).coerceIn(0f, 15f) * 6
				glRotatef(angle, 0f, 0f, -1f)
				glTranslatef(0f, y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, -1f, 0f)
			}
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30 * 220 / 440 + (frame - 45).coerceIn(0f, 5f) * 4
				val angle = 90f - (frame - 30).coerceIn(0f, 15f) * 6
				glRotatef(angle, 1f, 0f, 0f)
				glTranslatef(0f, y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, 1f, 0f)
			}
			
			wrap(part) {
				val y = 110f + min(frame, 30f) * 440 / 30 * 220 / 440 + (frame - 45).coerceIn(0f, 5f) * 4
				val angle = 90f + (frame - 30).coerceIn(0f, 15f) * 6
				glRotatef(angle, -1f, 0f, 0f)
				glTranslatef(0f, y, 0f)
				glRotatef(max(0f, frame - 60) % 100 * 3.6f, 0f, -1f, 0f)
			}
		}
		glColor3f(1f, 1f, 1f)
		
		mc.renderEngine.bindTexture((if (ConfigHandler.useShaders) LibResourceLocations.akashicCube else LibResourceLocations.akashicCube_))
		
		glDisable(GL_CULL_FACE)
		for (i in -110..110 step 110) {
			for (j in -110..110 step 110) {
				for (k in -110..110 step 110) {
					if (i == 0 && j == 0 && k == 0) continue
					if (i == 0 && j == 0) continue
					if (i == 0 && k == 0) continue
					if (j == 0 && k == 0) continue
					
					val f = max(0f, frame - 60) % 100 * 3.6
					val v = Vector3(i, j, k).extend((frame - 50).coerceIn(0f, 10f) * 33).rotate(f, Vector3(1).rotate(f, Vector3.oY))
					
					if (ConfigHandler.useShaders) ASJShaderHelper.useShader(LibShaderIDs.idColor3d) {
						GL20.glUniform3f(GL20.glGetUniformLocation(it, "translation"), v.x.F, v.y.F + 50, v.z.F) // FIXME use translation variable
					}
					
					glPushMatrix()
					glTranslated(v.x, v.y, v.z)
					glScalef(-1f)
					model.renderPart("core")
					glPopMatrix()
				}
			}
		}
		
		if (ConfigHandler.useShaders) ASJShaderHelper.releaseShader()
		
		glPushMatrix()
		glDisable(GL_LIGHTING)
		
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		val a = (sin(Botania.proxy.worldElapsedTicks / 4f) / 2 + 0.5f)
		val m = a * 0.2f
		glColor4f(0.7f + m, m, 1f, 1f)
		model.renderPart("core")
		for (i in 1..50) {
			glScalef((i / 100f + 1) / ((i - 1) / 100f + 1))
			glColor4d(0.7 + m, m.D, 1.0, a * 0.025 + 0.01)
			model.renderPart("core")
		}
		glColor4f(1f, 1f, 1f, 1f)
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		glEnable(GL_LIGHTING)
		glPopMatrix()
		
		glPopMatrix()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
	}
	
	override fun handleRenderType(item: ItemStack?, type: IItemRenderer.ItemRenderType?) = true
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType?, item: ItemStack?, helper: IItemRenderer.ItemRendererHelper?) = helper != IItemRenderer.ItemRendererHelper.BLOCK_3D
}