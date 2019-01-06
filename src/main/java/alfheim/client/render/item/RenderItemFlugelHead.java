package alfheim.client.render.item;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibResourceLocations;
import alfheim.client.model.entity.ModelBipedNew;
import alfheim.client.render.tile.RenderTileFlugelHead;
import alfheim.common.item.ItemFlugelHead;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.tile.RenderTileSkullOverride;
import vazkii.botania.common.item.ItemGaiaHead;

public class RenderItemFlugelHead {

	// because!
	public static boolean notSwap = true;
	
	public static void render(RenderPlayerEvent.Pre e, EntityPlayer player) {
		if (player.getCurrentArmor(3) != null && (player.getCurrentArmor(3).getItem() instanceof ItemFlugelHead || player.getCurrentArmor(3).getItem() instanceof ItemGaiaHead))
			e.renderer.modelBipedMain.bipedHead.showModel = e.renderer.modelBipedMain.bipedHeadwear.showModel = notSwap = false;
	}

	public static void render(RenderPlayerEvent.Specials.Post e, EntityPlayer player) {
		if (!notSwap) {
			float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick;
			float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick;
			float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick;
			
			glPushMatrix();
			glRotatef(yawOffset, 0, -1, 0);
			glRotatef(yaw - 270, 0, 1, 0);
			glRotatef(pitch, 0, 0, 1);
			glRotatef(-90, 0, 1, 0);

			if (player.getCurrentArmor(3) != null) {
				if (player.getCurrentArmor(3).getItem() instanceof ItemFlugelHead) {
					Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.jibril);
					ModelBipedNew.model.head.render(0.0625F);
				} else
				if (player.getCurrentArmor(3).getItem() instanceof ItemGaiaHead) {
					Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().thePlayer.getLocationSkin());
					ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);
					RenderTileSkullOverride.modelSkull.render(null, 0, 0, 0, 0, 0, 0.0625F);
					ShaderHelper.releaseShader();
				}
			}
			
			glPopMatrix();
			
			e.renderer.modelBipedMain.bipedHead.showModel = e.renderer.modelBipedMain.bipedHeadwear.showModel = notSwap = true;
		}
	}
}
