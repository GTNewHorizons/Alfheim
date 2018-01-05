package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;

import alfheim.Constants;
import alfheim.client.render.entity.RenderWings;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GUIRace extends Gui {
	
	private static final ResourceLocation flightTime = new ResourceLocation(Constants.MODID, "textures/gui/FlightTime.png");
	private Minecraft mc;
	
	public GUIRace(Minecraft mc) {
		super();
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onOverlayRendering(RenderGameOverlayEvent event) {
		if (event.isCancelable() || event.type != ElementType.CROSSHAIRS || EnumRace.fromID(mc.thePlayer.getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) return;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glDisable(GL_ALPHA_TEST);
		glScaled(0.1, 0.1, 1);
		
		this.mc.getTextureManager().bindTexture(RenderWings.getPlayerIconTexture(this.mc.thePlayer));
		this.drawTexturedModalRect(event.resolution.getScaledWidth() * 5 + (182 * 5) + 32, event.resolution.getScaledHeight() * 10 - 256 - 32, 0, 0, 256, 256);

		int flightTime = MathHelper.floor_double(this.mc.thePlayer.getEntityAttribute(Constants.FLIGHT).getAttributeValue());
		this.mc.getTextureManager().bindTexture(this.flightTime);

		{
			double mod = flightTime / Constants.FLIGHT.getDefaultValue();
			glScaled(1152, 288, 1);
			glTranslated(0.02, 0.02, 0);
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        
	        // Base
	        tessellator.addVertexWithUV(0, 1, this.zLevel, 0, 0.2);
	        tessellator.addVertexWithUV(1, 1, this.zLevel, 1, 0.2);
	        tessellator.addVertexWithUV(1, 0, this.zLevel, 1, 0);
	        tessellator.addVertexWithUV(0, 0, this.zLevel, 0, 0);
	        
	        // Stamina default
	        double d = 0.265;
	        tessellator.addVertexWithUV(0, 1, this.zLevel, 0, 0.4);
	        tessellator.addVertexWithUV(d, 1, this.zLevel, d, 0.4);
	        tessellator.addVertexWithUV(d, 0, this.zLevel, d, 0.2);
	        tessellator.addVertexWithUV(0, 0, this.zLevel, 0, 0.2);
	        
	        // 17
	        tessellator.addVertexWithUV(0, 1, this.zLevel, 0, 0.6);
	        tessellator.addVertexWithUV(1, 1, this.zLevel, 1, 0.6);
	        tessellator.addVertexWithUV(1, 0, this.zLevel, 1, 0.4);
	        tessellator.addVertexWithUV(0, 0, this.zLevel, 0, 0.4);
	        
	        // Stamina shifting
	        mod *= d * d * 10;
	        tessellator.addVertexWithUV(0, 1, this.zLevel, 0, 0.4);
	        tessellator.addVertexWithUV(d + mod, 1, this.zLevel, d + mod, 0.4);
	        tessellator.addVertexWithUV(d + mod, 0, this.zLevel, d + mod, 0.2);
	        tessellator.addVertexWithUV(0, 0, this.zLevel, 0, 0.2);
	        
	        // glow
	        if (this.mc.thePlayer.capabilities.isFlying) {
	        	tessellator.addVertexWithUV(0 + mod + d - 0.085, 1, this.zLevel, 0, 0.8);
		        tessellator.addVertexWithUV(1 + mod + d - 0.085 - (mod + d), 1, this.zLevel, 1 - (mod + d), 0.8);
		        tessellator.addVertexWithUV(1 + mod + d - 0.085 - (mod + d), 0, this.zLevel, 1 - (mod + d), 0.6);
		        tessellator.addVertexWithUV(0 + mod + d - 0.085, 0, this.zLevel, 0, 0.6);
	        }
	        
	        // shifter
	        tessellator.addVertexWithUV(0 + mod + d, 1, this.zLevel, 0, 1);
	        tessellator.addVertexWithUV(1 + mod + d, 1, this.zLevel, 1, 1);
	        tessellator.addVertexWithUV(1 + mod + d, 0, this.zLevel, 1, 0.8);
	        tessellator.addVertexWithUV(0 + mod + d, 0, this.zLevel, 0, 0.8);
	        
	        tessellator.draw();
		}
		
		glEnable(GL_ALPHA_TEST);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}