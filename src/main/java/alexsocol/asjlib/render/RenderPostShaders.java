package alexsocol.asjlib.render;

import java.util.ArrayList;
import java.util.Collections;

import alexsocol.asjlib.ASJUtilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

public class RenderPostShaders {
	
	static {
		MinecraftForge.EVENT_BUS.register(new RendererPostShaders());
	}
	
	public static ArrayList<ShadedObject> shaders = new ArrayList<ShadedObject>();
	private static int renderObjectMaterialID = 0;
	
	public static void registerShadedObject(ShadedObject renobj) {
		shaders.add(renobj);
	}
	
	public static int getNextAvailableRenderObjectMaterialID() {
		return renderObjectMaterialID++;
	}
	
	public static void dispatchObjects() {
		if (shaders.isEmpty()) return;
		Collections.sort(shaders);
		
		boolean post = false;
		int prevShader = 0, prevMaterial = -1;
		ShadedObject prevObj = shaders.get(0);
		if (prevObj.texture != null) Minecraft.getMinecraft().renderEngine.bindTexture(prevObj.texture);
		
		for (ShadedObject obj : shaders) {
			if (obj.translations.isEmpty()) continue;
			if (post && obj.materialID != prevObj.materialID) {
				prevObj.postRender();
				post = false;
			}
			if (obj.shaderID != prevShader) {
				ASJShaderHelper.useShader(obj.shaderID);
				prevShader = obj.shaderID;
			}
			if (obj.materialID != prevMaterial) obj.preRender();
			if (obj.texture!= null && obj.texture != prevObj.texture) Minecraft.getMinecraft().renderEngine.bindTexture(obj.texture);
			obj.doRender();
			obj.translations.clear();
			prevObj = obj;
			post = true;
		}
		if (post) prevObj.postRender();
		
		ASJShaderHelper.releaseShader();
	}
	
	public static class RendererPostShaders {
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public void onWorldLastRender(RenderWorldLastEvent e) {
			dispatchObjects();
		}
	}
}