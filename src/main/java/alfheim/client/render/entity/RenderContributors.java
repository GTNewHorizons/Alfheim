package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;

public class RenderContributors {

	private static final ResourceLocation skin = new ResourceLocation(Constants.MODID, "textures/model/entity/AlexSocol.png");
    private static final ResourceLocation book = new ResourceLocation(LibResources.MODEL_LEXICA);
    private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON); 
	
	public static void render(Post e) {
		EntityPlayer player = e.entityPlayer;
		if (player.getCommandSenderName().equals("AlexSocol")) {
			((AbstractClientPlayer) player).func_152121_a(Type.SKIN, skin);
			
			glPushMatrix();
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(babylon);

			glRotated(-90, 1, 0, 0);
			glTranslated(0, -0.5, 0.2);
			glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2.0) + e.partialRenderTick, 0, 1, 0);
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

			glEnable(GL_LIGHTING);
			glShadeModel(GL_FLAT);
			glEnable(GL_CULL_FACE);
			glPopMatrix();
		}
		if (player.getCommandSenderName().equals("DmitryWS")) {
			glPushMatrix();
			glEnable(GL_CULL_FACE);
			float t = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + e.partialRenderTick;
			glTranslated(0, -(0.9 + Math.sin(t / 20) * 0.025), 0);
			glRotated(180, 1, 0, 0);
			glRotated(-90, 0, 1, 0);
			glRotated(60, 0, 0, 1);
			Minecraft.getMinecraft().renderEngine.bindTexture(book);
			ModelBook model = new ModelBook();
			model.render((Entity)null, 0, 0, 0.95F, 1, 0.0F, 0.0625F);
			glPopMatrix();
		}
	}
}
