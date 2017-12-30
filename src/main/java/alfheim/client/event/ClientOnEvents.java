package alfheim.client.event;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.lang.reflect.Field;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import alfheim.client.registry.AflheimClientRegistry;
import alfheim.client.render.ASJShaderHelper;
import alfheim.common.utils.AlfheimConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ClientOnEvents {

	private static final IModelCustom sphere = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID, "model/sphere.obj"));
	private static final ResourceLocation skin = new ResourceLocation(Constants.MODID, "textures/model/entity/AlexSocol.png");
	
	static void onGameOver(GuiGameOver gui) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (Constants.DEV) return;
		Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
		field_146347_a.setAccessible(true);
		field_146347_a.setInt(gui, -AlfheimConfig.deathScreenAdditionalTime + 20);
	}

	static void onAuthorRendered(Post e) {
		EntityPlayer player = e.entityPlayer;
		((AbstractClientPlayer) player).func_152121_a(Type.SKIN, skin);
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glAlphaFunc(GL_GREATER, 0);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		glRotated((player.worldObj.getTotalWorldTime() / 10.0) % 360, 0, 1, 0);
		glTranslated(0, 0.5, 0);
		glScaled(1.25, 1.25, 1.25);
		
		//Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Constants.MODID, "textures/shader/fireball.png"));
		ASJShaderHelper.useShader(AflheimClientRegistry.sphere);
		sphere.renderAll();
        ASJShaderHelper.releaseShader();
        
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}