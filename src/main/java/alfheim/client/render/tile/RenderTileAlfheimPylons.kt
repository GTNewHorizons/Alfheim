package alfheim.client.render.tile

import alexsocol.asjlib.render.*
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.client.core.handler.*
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.model.*
import vazkii.botania.common.core.handler.ConfigHandler
import java.util.*
import kotlin.math.sin

object RenderTileAlfheimPylons: TileEntitySpecialRenderer() {
	
	var model: IPylonModel = if (ConfigHandler.oldPylonModel) ModelPylonOld() else ModelPylon()
	var orange = false
	var red = false
	var hand = false
	
	val rand = Random()
	
	val shObjRO = ShadedObjectPylon(LibResourceLocations.antiPylonOld)
	val shObjR = ShadedObjectPylon(LibResourceLocations.antiPylon)
	val shObjPO = ShadedObjectPylon(LibResourceLocations.elvenPylonOld)
	val shObjP = ShadedObjectPylon(LibResourceLocations.elvenPylon)
	val shObjOO = ShadedObjectPylon(LibResourceLocations.yordinPylonOld)
	val shObjO = ShadedObjectPylon(LibResourceLocations.yordinPylon)
	
	init {
		RenderPostShaders.registerShadedObject(shObjO)
		RenderPostShaders.registerShadedObject(shObjP)
		RenderPostShaders.registerShadedObject(shObjR)
		RenderPostShaders.registerShadedObject(shObjOO)
		RenderPostShaders.registerShadedObject(shObjPO)
		RenderPostShaders.registerShadedObject(shObjRO)
	}
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		glPushMatrix()
		glEnable(GL_RESCALE_NORMAL)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		val a = if (MultiblockRenderHandler.rendering) 0.6f else 1f
		glColor4f(1f, 1f, 1f, a)
		if (tile.worldObj != null) {
			orange = tile.getBlockMetadata() == 1
			red = tile.getBlockMetadata() == 2
		}
		
		if (ConfigHandler.oldPylonModel)
			Minecraft.getMinecraft().renderEngine.bindTexture(if (red) LibResourceLocations.antiPylonOld else if (orange) LibResourceLocations.yordinPylonOld else LibResourceLocations.elvenPylonOld)
		else
			Minecraft.getMinecraft().renderEngine.bindTexture(if (red) LibResourceLocations.antiPylon else if (orange) LibResourceLocations.yordinPylon else LibResourceLocations.elvenPylon)
		
		var worldTime = if (tile.worldObj == null) 0.0 else (ClientTickHandler.ticksInGame + ticks).toDouble()
		
		rand.setSeed((tile.xCoord xor tile.yCoord xor tile.zCoord).toLong())
		worldTime += rand.nextInt(360).toDouble()
		
		if (ConfigHandler.oldPylonModel) {
			glTranslated(x + 0.5, y + 2.2, z + 0.5)
			glScalef(1f, -1.5f, -1f)
		} else {
			glTranslated(x + 0.2 + if (orange) -0.1 else 0.0, y + 0.05, z + 0.8 + if (orange) 0.1 else 0.0)
			val scale = if (orange) 0.8f else 0.6f
			glScalef(scale, 0.6f, scale)
		}
		
		if (!orange) {
			glPushMatrix()
			if (!ConfigHandler.oldPylonModel)
				glTranslatef(0.5f, 0f, -0.5f)
			glRotatef(worldTime.toFloat() * 1.5f, 0f, 1f, 0f)
			if (!ConfigHandler.oldPylonModel)
				glTranslatef(-0.5f, 0f, 0.5f)
			
			model.renderRing()
			glTranslated(0.0, sin(worldTime / 20.0) / 20 - 0.025, 0.0)
			model.renderGems()
			glPopMatrix()
		}
		
		glPushMatrix()
		glTranslated(0.0, sin(worldTime / 20.0) / 17.5, 0.0)
		
		if (!ConfigHandler.oldPylonModel)
			glTranslatef(0.5f, 0f, -0.5f)
		
		glRotatef((-worldTime).toFloat(), 0f, 1f, 0f)
		if (!ConfigHandler.oldPylonModel)
			glTranslatef(-0.5f, 0f, 0.5f)
		
		glDisable(GL_CULL_FACE)
		model.renderCrystal()
		
		glColor4f(1f, 1f, 1f, a)
		
		if (!ShaderHelper.useShaders() || hand) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
			val alpha = ((sin((ClientTickHandler.ticksInGame + ticks) / 20.0) / 2.0 + 0.5) / if (ConfigHandler.oldPylonModel) 1.0 else 2.0).toFloat()
			glColor4f(1f, 1f, 1f, a * (alpha + 0.183f))
		}
		
		glDisable(GL_ALPHA_TEST)
		glScalef(1.1f, 1.1f, 1.1f)
		if (!ConfigHandler.oldPylonModel)
			glTranslatef(-0.05f, -0.1f, 0.05f)
		else
			glTranslatef(0f, -0.09f, 0f)
		
		if (!RenderPostShaders.allowShaders) {
			val light = 15728880
			val lightmapX = light % 65536
			val lightmapY = light / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX.toFloat(), lightmapY.toFloat())
			val alpha = ((sin(worldTime / 20.0) / 2.0 + 0.5) / if (ConfigHandler.oldPylonModel) 1.0 else 2.0).toFloat()
			glColor4f(1f, 1f, 1f, a * (alpha + 0.183f))
		}
		
		if (!hand && RenderPostShaders.allowShaders) {
			val shObj = if (ConfigHandler.oldPylonModel) if (red) shObjRO else if (orange) shObjOO else shObjPO else if (red) shObjR else if (orange) shObjO else shObjP
			shObj.addTranslation()
		} else {
			model.renderCrystal()
		}
		
		glEnable(GL_ALPHA_TEST)
		glEnable(GL_CULL_FACE)
		glPopMatrix()
		
		glDisable(GL_BLEND)
		glEnable(GL_RESCALE_NORMAL)
		glPopMatrix()
		
		hand = false
	}
	
	class ShadedObjectPylon(rl: ResourceLocation): ShadedObject(ShaderHelper.pylonGlow, matID, rl) {
		
		override fun preRender() {
			glEnable(GL_RESCALE_NORMAL)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			val a = if (MultiblockRenderHandler.rendering) 0.6f else 1f
			glColor4f(1f, 1f, 1f, a)
			glDisable(GL_CULL_FACE)
			glDisable(GL_ALPHA_TEST)
		}
		
		override fun drawMesh() {
			model.renderCrystal()
		}
		
		override fun postRender() {
			glEnable(GL_ALPHA_TEST)
			glEnable(GL_CULL_FACE)
			glDisable(GL_BLEND)
			glEnable(GL_RESCALE_NORMAL)
		}
		
		companion object {
			
			val matID = RenderPostShaders.nextAvailableRenderObjectMaterialID
		}
	}
}