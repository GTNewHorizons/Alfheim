package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.render.ASJShaderHelper;
import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibShaderIDs;
import alfheim.client.render.entity.RenderWings;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.Flight;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GUIRace extends Gui {
	
	private Minecraft mc;
	
	public GUIRace(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onOverlayRendering(RenderGameOverlayEvent.Post e) {
		if (AlfheimCore.enableMMO && AlfheimConfig.selfHealthUI) return;
		if (e.isCancelable() || e.type != ElementType.EXPERIENCE || EnumRace.getRace(mc.thePlayer) == EnumRace.HUMAN) return;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glDisable(GL_ALPHA_TEST);
		
		glTranslated(e.resolution.getScaledWidth_double() / 2 + 91, e.resolution.getScaledHeight() - 32, 0);
		
		mc.getTextureManager().bindTexture(RenderWings.getPlayerIconTexture(mc.thePlayer));
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

		EnumRace.glColorA(EnumRace.getRaceID(mc.thePlayer), 0.5);
		
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(0, 0, 0, 0, 0);
		Tessellator.instance.addVertexWithUV(0, 32, 0, 0, 1);
		Tessellator.instance.addVertexWithUV(32, 32, 0, 1, 1);
		Tessellator.instance.addVertexWithUV(32, 0, 0, 1, 0);
		Tessellator.instance.draw();
		
//		ASJShaderHelper.useShader(LibShaderIDs.idShadow);
		
		double mod = MathHelper.floor_double(Flight.get(mc.thePlayer)) / Flight.max();
		double time = Math.sin(mc.theWorld.getTotalWorldTime() / 2) * 0.5;
		EnumRace.glColorA(EnumRace.getRaceID(mc.thePlayer), (mc.thePlayer.capabilities.isFlying ? (mod > 0.1 ? time + 0.5 : time) : 1));
		
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(0, 32-(mod*32), 0, 0, 1-mod);
		Tessellator.instance.addVertexWithUV(0, 32, 0, 0, 1);
		Tessellator.instance.addVertexWithUV(32, 32, 0, 1, 1);
		Tessellator.instance.addVertexWithUV(32, 32-(mod*32), 0, 1, 1-mod);
		Tessellator.instance.draw();
		
		glEnable(GL_ALPHA_TEST);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}
