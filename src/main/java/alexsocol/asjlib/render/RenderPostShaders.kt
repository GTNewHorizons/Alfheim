package alexsocol.asjlib.render

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import java.util.*

object RenderPostShaders {
	
	var allowShaders = true
	
	val shaders = ArrayList<ShadedObject>()
	private var renderObjectMaterialID = 0
	
	val nextAvailableRenderObjectMaterialID: Int
		get() = renderObjectMaterialID++
	
	init {
		MinecraftForge.EVENT_BUS.register(RendererPostShaders())
	}
	
	fun registerShadedObject(renobj: ShadedObject) {
		shaders.add(renobj)
	}
	
	fun dispatchObjects() {
		if (shaders.isEmpty()) return
		shaders.sort()
		
		var post = false
		var prevShader = 0
		val prevMaterial = -1
		var prevObj = shaders[0]
		if (prevObj.texture != null) Minecraft.getMinecraft().renderEngine.bindTexture(prevObj.texture)
		
		for (obj in shaders) {
			if (obj.translations.isEmpty()) continue
			if (post && obj.materialID != prevObj.materialID) {
				prevObj.postRender()
			}
			if (obj.shaderID != prevShader) {
				if (allowShaders) ASJShaderHelper.useShader(obj.shaderID)
				prevShader = obj.shaderID
			}
			if (obj.materialID != prevMaterial) obj.preRender()
			if (obj.texture != null && obj.texture !== prevObj.texture) Minecraft.getMinecraft().renderEngine.bindTexture(obj.texture)
			obj.doRender()
			obj.translations.clear()
			prevObj = obj
			post = true
		}
		if (post) prevObj.postRender()
		
		if (allowShaders) ASJShaderHelper.releaseShader()
	}
	
	class RendererPostShaders {
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		fun onWorldLastRender(e: RenderWorldLastEvent) {
			dispatchObjects()
		}
	}
}