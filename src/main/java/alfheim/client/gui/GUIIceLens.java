package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;

import alfheim.AlfheimCore;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.registry.AlfheimRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GUIIceLens extends Gui {
	
	private Minecraft mc;
	
	public GUIIceLens(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onOverlayRendering(RenderGameOverlayEvent.Post event) {
		if (!AlfheimCore.enableMMO || event.type != ElementType.HELMET || Minecraft.getMinecraft().thePlayer.getActivePotionEffect(AlfheimRegistry.icelens) == null) return;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		//glDisable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.iceLens);
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int u = res.getScaledWidth();
        int v = res.getScaledHeight();
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.addVertexWithUV(0, 0, 0, 0, 0);
		tes.addVertexWithUV(0, v, 0, 0, 1);
		tes.addVertexWithUV(u, v, 0, 1, 1);
		tes.addVertexWithUV(u, 0, 0, 1, 0);
		tes.draw();

        glAlphaFunc(GL_GREATER, 0.1F);
		//glEnable(GL_ALPHA_TEST);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}