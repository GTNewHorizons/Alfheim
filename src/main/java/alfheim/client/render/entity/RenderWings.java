package alfheim.client.render.entity;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.Flight;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.common.Botania;

import static org.lwjgl.opengl.GL11.*;

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
		EnumRace.getRace(player).glColorA(Flight.get(player) / Flight.get(player) < 0.05 ? (Math.min(0.75 + (float) Math.cos((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd * 0.3) * 0.2, 1)) : 1);
		
		Helper.rotateIfSneaking(player);
		glTranslated(0, -0.15, 0);
		
		// Icon
		glPushMatrix();
		glTranslated(-0.25, 0.25, 0.15);
		double si = 0.5;
		glScaled(si, si, si);
		drawRect(getPlayerIconTexture(player), 0);
		glPopMatrix();
		
		glTranslated(0, 0.1, 0);
		
		player.sendPlayerAbilities();
		boolean flying = player.capabilities.isFlying;
		float ry = 20F + (float) ((Math.sin((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * spd * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
		
		// Wing left
		glPushMatrix();
		glTranslated(0.15, 0.1, 0.15);
		double swr = 1.5;
		glScaled(swr, swr, swr);
		//glRotated(10, 0, 0, 1);
		glRotated(-ry, 0, 1, 0);
		drawRect(getPlayerWingTexture(player), -1);
		glPopMatrix();
		
		// Wing right
		glPushMatrix();
		glTranslated(-0.15, 0.1, 0.15);
		double swl = 1.5;
		glScaled(-swl, swl, swl);
		//glRotated(10, 0, 0, 1);
		glRotated(-ry, 0, 1, 0);
		drawRect(getPlayerWingTexture(player), -1);
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
	
	private static void drawRect(ResourceLocation texture, int i) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(0, i, 0, 1, 0);
		Tessellator.instance.addVertexWithUV(0, 1, 0, 1, 1);
		Tessellator.instance.addVertexWithUV(1, 1, 0, 0, 1);
		Tessellator.instance.addVertexWithUV(1, i, 0, 0, 0);
		Tessellator.instance.draw();
	}
	
	public static ResourceLocation getPlayerWingTexture(EntityPlayer player) {
		return LibResourceLocations.wings[EnumRace.getRaceID(player)];
	}
	
	public static ResourceLocation getPlayerIconTexture(EntityPlayer player) {
		return LibResourceLocations.icons[EnumRace.getRaceID(player)];
	}
}