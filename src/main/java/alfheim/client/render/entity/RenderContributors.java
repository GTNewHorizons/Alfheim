package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;

public class RenderContributors {

    private static final ResourceLocation book = new ResourceLocation(LibResources.MODEL_LEXICA);
    private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON); 
	
	public static void render(RenderWorldLastEvent e) {
		EntityPlayer thePlayer = Minecraft.getMinecraft().thePlayer;
		EntityPlayer author = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("AlexSocol");
		EntityPlayer lore = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("DmitryWS");
	
		if (author != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(babylon);
			glPushMatrix();
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
			
			if (!thePlayer.equals(author)) {
				ASJUtilities.interpolatedTranslation(author, e.partialTicks);
				ASJUtilities.interpolatedTranslationReverse(thePlayer, e.partialTicks);
				glTranslated(0, 1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight, 0);
			}
			
			glRotated(-90, 1, 0, 0);
			
			if (author.equals(thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0 && !(author.isRiding() && author.ridingEntity instanceof EntityHorse)) {
				glRotated(-ASJUtilities.interpolate(author.prevRenderYawOffset, author.renderYawOffset, e.partialTicks), 0, 0, 1);
			} else {
				glRotated(-ASJUtilities.interpolate(author.prevRotationYaw, author.rotationYaw, e.partialTicks), 0, 0, 1);
			}
			glTranslated(0, 0.5, -0.4);
			glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + e.partialTicks, 0, 1, 0);
			glScaled(2, 2, 2);
	
			ShaderHelper.useShader(ShaderHelper.halo);
			Tessellator tes = Tessellator.instance;
			tes.startDrawingQuads();
			tes.addVertexWithUV(-1, 0, -1, 0, 0);
			tes.addVertexWithUV(-1, 0, 1, 0, 1);
			tes.addVertexWithUV(1, 0, 1, 1, 1);
			tes.addVertexWithUV(1, 0, -1, 1, 0);
			tes.draw();
			ShaderHelper.releaseShader();
	
			glShadeModel(GL_FLAT);
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glPopMatrix();
		}
		
		if (lore != null && !(thePlayer.equals(lore) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
			glPushMatrix();
			glEnable(GL_CULL_FACE);
			float t = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + e.partialTicks;
			
			if (!thePlayer.equals(lore)) {
				ASJUtilities.interpolatedTranslation(lore, e.partialTicks);
				ASJUtilities.interpolatedTranslationReverse(thePlayer, e.partialTicks);
				glTranslated(0, 1.75 + Minecraft.getMinecraft().thePlayer.eyeHeight, 0);
			}
			
			glTranslated(0, 1.5 -(0.9 + Math.sin(t / 20) * 0.025), 0);
			glRotated(-ASJUtilities.interpolate(lore.prevRotationYaw, lore.rotationYaw, e.partialTicks), 0, 1, 0);
			glRotated(-90, 0, 1, 0);
			glRotated(60, 0, 0, 1);
			Minecraft.getMinecraft().renderEngine.bindTexture(book);
			ModelBook model = new ModelBook();
			model.render((Entity)null, 0, 0, 0.95F, 1, 0.0F, 0.0625F);
			glPopMatrix();
		}
	}
}
