package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.common.Botania;

public class RenderWings {
	
	@SideOnly(Side.CLIENT)
	public static void render(RenderPlayerEvent.Specials.Post e, EntityPlayer player) {
		if (!AlfheimConfig.enableWingsNonAlfheim && Minecraft.getMinecraft().theWorld.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return;
		if (EnumRace.getRace(player) == EnumRace.HUMAN) return;
		if (player.isInvisible() || player.isPotionActive(Potion.invisibility) || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) return;
		if (player.getCommandSenderName().equals("AlexSocol")) return;
		
		glPushMatrix();
		glDisable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		double spd = 0.5;
		EnumRace.getRace(player).glColorA(player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getBaseValue() / player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getAttribute().getDefaultValue() < 0.05 ? (Math.min(0.75 + (float) Math.cos((double) ((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd) * 0.3) * 0.2, 1)) : 1);
		
		Helper.rotateIfSneaking(player);
		glTranslated(0, -0.15, 0);
		
		// Icon
		glPushMatrix();
		glTranslated(-0.25, 0.25, 0.15);
		double si = 0.5;
		glScaled(si, si, si);
		drawRect(Tessellator.instance, getPlayerIconTexture(player), 0);
		glPopMatrix();
		
		glTranslated(0, 0.1, 0);
		
		player.sendPlayerAbilities();
		boolean flying = player.capabilities.isFlying;
		float ry = 20F + (float) ((Math.sin((double) ((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
		
		// Wing left
		glPushMatrix();
		glTranslated(0.15, 0.1, 0.15);
		double swr = 1.5;
		glScaled(swr, swr, swr);
		//glRotated(10, 0, 0, 1);
		glRotated(-ry, 0, 1, 0);
		drawRect(Tessellator.instance, getPlayerWingTexture(player), -1);
		glPopMatrix();
		
		// Wing right
		glPushMatrix();
		glTranslated(-0.15, 0.1, 0.15);
		double swl = 1.5;
		glScaled(-swl, swl, swl);
		//glRotated(10, 0, 0, 1);
		glRotated(-ry, 0, 1, 0);
		drawRect(Tessellator.instance, getPlayerWingTexture(player), -1);
		glPopMatrix();
		
		//glColor4d(1, 1, 1, 1); for some reason it cleans color
        glAlphaFunc(GL_GREATER, 0.1F);
		glDepthMask(true);
		glEnable(GL_LIGHTING);
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glPopMatrix();
		
		if (Minecraft.getMinecraft().thePlayer == player && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) return;
		
		if (Minecraft.getMinecraft().theWorld.getTotalWorldTime() % 10 == 0 && !Minecraft.getMinecraft().isGamePaused()) {
			Vector3 v = new Vector3(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().add(0, Math.random(), 0).mul(Math.random(), 1, Math.random()).mul(player.width, player.height, player.width);
			Botania.proxy.sparkleFX(player.worldObj, player.posX + v.x, player.posY + v.y - (Minecraft.getMinecraft().thePlayer == player ? 1.62 : 0), player.posZ + v.z, 1, 1, 1, 2F * (float) Math.random(), 20);
		}
	}
	
	private static void drawRect(Tessellator tes, ResourceLocation texture, int i) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		tes.startDrawingQuads();
		tes.addVertexWithUV(0, i, 0, 1, 0);
		tes.addVertexWithUV(0, 1, 0, 1, 1);
		tes.addVertexWithUV(1, 1, 0, 0, 1);
		tes.addVertexWithUV(1, i, 0, 0, 0);
		tes.draw();
	}
	
	public static ResourceLocation getPlayerWingTexture(EntityPlayer player) {
		return LibResourceLocations.wings[EnumRace.getRaceID(player)];
	}
	
	public static ResourceLocation getPlayerIconTexture(EntityPlayer player) {
		return LibResourceLocations.icons[EnumRace.getRaceID(player)];
	}
}