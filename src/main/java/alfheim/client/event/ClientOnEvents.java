package alfheim.client.event;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import alfheim.client.render.ShaderHelperAlfheim;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
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
	
	static void onGameOver(GuiGameOver gui) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (Constants.DEV) return;
		Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
		field_146347_a.setAccessible(true);
		field_146347_a.setInt(gui, -AlfheimConfig.deathScreenAdditionalTime + 20);
	}

	static void onAuthorRendered(Post e) {
		EntityPlayer player = e.entityPlayer;
		((AbstractClientPlayer) player).func_152121_a(Type.SKIN, new ResourceLocation(Constants.MODID, "textures/model/entity/AlexSocol.png"));
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glAlphaFunc(GL_GREATER, 0);
		glDisable(GL_TEXTURE_2D);
		
		ShaderHelperAlfheim.useShader(ShaderHelperAlfheim.sphere);
        sphere.renderAll();
        ShaderHelperAlfheim.releaseShader();

		glColor4d(1, 1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}