package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.ModInfo;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import vazkii.botania.common.Botania;

public class RenderWings {
	
	private static final ResourceLocation[] wings = {
		null,
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SALAMANDER_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SYLPH_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/CAITSITH_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/POOKA_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/GNOME_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/LEPRECHAUN_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SPRIGGAN_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/UNDINE_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/IMP_wing.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/ALV_wing.png"),
	};
	
	private static final ResourceLocation[] icons = {
		null,
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SALAMANDER_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SYLPH_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/CAITSITH_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/POOKA_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/GNOME_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/LEPRECHAUN_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/SPRIGGAN_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/UNDINE_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/IMP_icon.png"),
		new ResourceLocation(ModInfo.MODID, "textures/model/entity/wings/ALV_icon.png"),
	};
	
	public static void render(RenderWorldLastEvent e) {
		if (!AlfheimConfig.enableWingsNonAlfheim && Minecraft.getMinecraft().theWorld.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return;
		EntityPlayer thePlayer = Minecraft.getMinecraft().thePlayer;
		List<EntityPlayer> list = Minecraft.getMinecraft().theWorld.playerEntities;
		for (EntityPlayer player : list) {
			if (EnumRace.getRace(player) == EnumRace.HUMAN || (thePlayer.equals(player) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) continue;
			
			glPushMatrix();
			glDisable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_LIGHTING);
			
			glDepthMask(false);
			glDisable(GL_ALPHA_TEST);
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			glColor4d(1, 1, 1, Math.min(0.75 + (float) Math.cos((double) (player.ticksExisted + e.partialTicks) * 0.3) * 0.2, 1));
			
			if (!player.equals(thePlayer)) {
				ASJUtilities.interpolatedTranslation(player, e.partialTicks);
				ASJUtilities.interpolatedTranslationReverse(thePlayer, e.partialTicks);
				glTranslated(0, 1.5 + Minecraft.getMinecraft().thePlayer.eyeHeight, 0);
			}
			
			if (player.isRiding() && player.ridingEntity instanceof EntityHorse) {
				glRotated(-ASJUtilities.interpolate(player.prevRotationYaw, player.rotationYaw, e.partialTicks), 0, 1, 0);
			} else {
				glRotated(-ASJUtilities.interpolate(player.prevRenderYawOffset, player.renderYawOffset, e.partialTicks), 0, 1, 0);
			}
			glRotated(180, 1, 0, 0);
			if (player.isSneaking()) {
				glRotated(28.64789, 1, 0, 0);
				glTranslated(0, 0, -0.1);
			}
			
			// Icon
			glPushMatrix();
			glTranslated(-0.25, 0.25, 0.15);
			double si = 0.5;
			glScaled(si, si, si);
			drawRect(Tessellator.instance, getPlayerIconTexture(player));
			glPopMatrix();
			
			boolean flying = player.capabilities.isFlying;
			float ry = 20F + (float) ((Math.sin((double) (player.ticksExisted + e.partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
	
			// Wing left
			glPushMatrix();
			glTranslated(0.25, 0.1, 0.15);
			double swr = 1.5;
			glScaled(swr, swr, swr);
			glRotated(10, 0, 0, 1);
			glRotated(-ry, 0, 1, 0);
			drawRect(Tessellator.instance, getPlayerWingTexture(player));
			glPopMatrix();
			
			// Wing right
			glPushMatrix();
			glTranslated(-0.25, 0.1, 0.15);
			double swl = 1.5;
			glScaled(-swl, swl, swl);
			glRotated(10, 0, 0, 1);
			glRotated(-ry, 0, 1, 0);
			drawRect(Tessellator.instance, getPlayerWingTexture(player));
			glPopMatrix();
	
			glColor4d(1, 1, 1, 1);
			glEnable(GL_ALPHA_TEST);
			glDepthMask(true);

			glDisable(GL_BLEND);
			glEnable(GL_CULL_FACE);
			glPopMatrix();
			
			if (Minecraft.getMinecraft().theWorld.getTotalWorldTime() % 10 == 0) {
				double x = player.posX - 0.25;
				double y = player.posY - 0.5 + (thePlayer.equals(player) ? 0 : 1.5);
				double z = player.posZ - 0.25;
				Botania.proxy.sparkleFX(player.worldObj, x + Math.random() * player.width * 2.0, y + Math.random() * 0.8, z + Math.random() * player.width * 2.0, 1, 1, 1, 2F * (float) Math.random(), 20);
			}
		}
	}
	
	private static void drawRect(Tessellator tes, ResourceLocation texture) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		tes.startDrawingQuads();
		tes.addVertexWithUV(0, 0, 0, 1, 0);
		tes.addVertexWithUV(0, 1, 0, 1, 1);
		tes.addVertexWithUV(1, 1, 0, 0, 1);
		tes.addVertexWithUV(1, 0, 0, 0, 0);
		tes.draw();
	}
	
	public static ResourceLocation getPlayerWingTexture(EntityPlayer player) {
		return wings[EnumRace.getRaceID(player)];
	}
	
	public static ResourceLocation getPlayerIconTexture(EntityPlayer player) {
		return icons[EnumRace.getRaceID(player)];
	}
}
