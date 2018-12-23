package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GUIDeathTimer extends GuiScreen {

	public int timer;
	
	@Override
	public void initGui() {
		super.initGui();
		timer = AlfheimConfig.deathScreenAddTime;
	}
	
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        
        FontRenderer font = mc.fontRenderer;
		ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		glScaled(2, 2, 1);
		{
			glPushMatrix();
			glColor4d(1, 1, 1, 1);
			glTranslated(resolution.getScaledWidth() / 4 - 32, resolution.getScaledHeight() / 4 - 32, 0);
			Tessellator tes = Tessellator.instance;
			mc.renderEngine.bindTexture(LibResourceLocations.deathTimerBG);
			tes.startDrawingQuads();
			tes.addVertexWithUV(0, 0, 0, 0, 0);
			tes.addVertexWithUV(0, 64, 0, 0, 1);
			tes.addVertexWithUV(64, 64, 0, 1, 1);
			tes.addVertexWithUV(64, 0, 0, 1, 0);
			tes.draw();
			mc.renderEngine.bindTexture(LibResourceLocations.deathTimer);
			glTranslated(32, 32, 0);
			glRotated(-(timer % 20) * 18, 0, 0, 1);
			glTranslated(-32, -32, 0);
			tes.startDrawingQuads();
			tes.addVertexWithUV(0, 0, 0, 0, 0);
			tes.addVertexWithUV(0, 64, 0, 0, 1);
			tes.addVertexWithUV(64, 64, 0, 1, 1);
			tes.addVertexWithUV(64, 0, 0, 1, 0);
			tes.draw();
			glPopMatrix();
		}

		{
			glPushMatrix();
			double sc = 1.5;
			glScaled(sc, sc, 1);
			String s = "" + Math.max((timer / 20), 0);
			font.drawString(s, (int) (resolution.getScaledWidth() / (4 * sc) - font.getStringWidth(s) / 2), (int) ((resolution.getScaledHeight()  / (4 * sc)) - (4)), 0xFFFFFF, true);
			glPopMatrix();
		}

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
	
    protected void keyTyped(char c, int i) {}

    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
    	super.updateScreen();
    	--timer;
    	
    	if (!mc.thePlayer.isPotionActive(AlfheimRegistry.leftFlame)) {
    		mc.displayGuiScreen((GuiScreen)null);
            mc.setIngameFocus();
    	}
    }
}
