package alfheim.client.event;

import static org.lwjgl.opengl.GL11.*;

import java.lang.reflect.Field;
import java.util.Random;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import alfheim.Constants;
import alfheim.client.render.entity.RenderContributors;
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

	static void onGameOver(GuiGameOver gui) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (Constants.DEV) return;
		Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
		field_146347_a.setAccessible(true);
		field_146347_a.setInt(gui, -Math.abs(AlfheimConfig.deathScreenAdditionalTime) + 20);
	}

	static void onContributorsRendered(Post e) {
		RenderContributors.render(e);
	}
}