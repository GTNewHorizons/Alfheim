package alfheim.client.event;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;

import org.lwjgl.opengl.*;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import alfheim.client.render.ShaderHelperAlfheim;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.api.internal.ShaderCallback;

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
		ShaderHelperAlfheim.useShader(ShaderHelperAlfheim.sphere);
		sphere.renderAll();
        ShaderHelperAlfheim.releaseShader();
        
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}