package alfheim.client.event;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;
import java.util.Random;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import alfheim.common.utils.AlfheimConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEnchantmentTable;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;

public class ClientOnEvents {

	private static final ResourceLocation skin = new ResourceLocation(Constants.MODID, "textures/model/entity/AlexSocol.png");
    private static final ResourceLocation book = new ResourceLocation("botania:textures/model/lexica.png");
    private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON); 
    
	static void onGameOver(GuiGameOver gui) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (Constants.DEV) return;
		Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
		field_146347_a.setAccessible(true);
		field_146347_a.setInt(gui, -Math.abs(AlfheimConfig.deathScreenAdditionalTime) + 20);
	}

	static void onContributorsRendered(Post e) {
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
			glRotated((Minecraft.getMinecraft().theWorld.getTotalWorldTime() / 2) % 360, 0, 1, 0);
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