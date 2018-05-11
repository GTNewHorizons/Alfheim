package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import alfheim.api.ModInfo;
import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GUIDeathTimer extends Gui {

	private static final ResourceLocation rotator = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimer.png");
	private static final ResourceLocation back = new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimerBack.png");
	private Minecraft mc;

	public GUIDeathTimer(Minecraft mc) {
		super();
		this.mc = mc;
	}

	@SubscribeEvent
	public void onOverlayRendering(GuiScreenEvent.DrawScreenEvent.Post e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (e.gui instanceof GuiGameOver) {
			GuiGameOver gui = (GuiGameOver) e.gui;
			FontRenderer font = mc.fontRenderer;
			ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			java.lang.reflect.Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
			field_146347_a.setAccessible(true);
			int time = -field_146347_a.getInt(gui) + 20;
			double c = time < 0 ? 1.0 : 0.8;
			
			glTranslated(AlfheimConfig.deathTimerX, AlfheimConfig.deathTimerY, 0);
			glScaled(AlfheimConfig.deathTimerScale, AlfheimConfig.deathTimerScale, 0);
			{
				glPushMatrix();
				glColor4d(c, c, c, 1);
				glTranslated(resolution.getScaledWidth() / 2 - 32 + AlfheimConfig.deathTimerX, resolution.getScaledHeight() - 64 + AlfheimConfig.deathTimerY, 0);
				Tessellator tes = Tessellator.instance;
				mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimerBack.png"));
				tes.startDrawingQuads();
				tes.addVertexWithUV(0, 0, 0, 0, 0);
				tes.addVertexWithUV(0, 64, 0, 0, 1);
				tes.addVertexWithUV(64, 64, 0, 1, 1);
				tes.addVertexWithUV(64, 0, 0, 1, 0);
				tes.draw();
				mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/DeathTimer.png"));
				glTranslated(32, 32, 0);
				glRotated(-(time % 20) * 18, 0, 0, 1);
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
				String s = "" + Math.max((time / 20), 0);
				font.drawString(s, (int) (resolution.getScaledWidth() / (2 * sc) - font.getStringWidth(s) / 2), (int) (resolution.getScaledHeight() / sc) - 25, (int) (255 * c) << 16 | (int) (255 * c) << 8 | (int) (255 * c), true);
				glPopMatrix();
			}
		}
	}
}