package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gloomyfolken.hooklib.asm.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
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

	@SideOnly(Side.CLIENT)
	@Hook(injectOnExit = true)
	public static void render(ModelBiped model, Entity entity, float time, float amplitude, float ticksExisted, float yawHead, float pitchHead, float size) {
		if (!(entity instanceof EntityPlayer)) return;
		if (!AlfheimConfig.enableWingsNonAlfheim && Minecraft.getMinecraft().theWorld.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return;
		
		EntityPlayer player = (EntityPlayer) entity;
		if (EnumRace.getRace(player) == EnumRace.HUMAN) return;
		
		glPushMatrix();
		//glDisable(GL_CULL_FACE); somehow this was causing ONLY right boot of elvorium armor to be reverted... IDK
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
		glDisable(GL_ALPHA_TEST);
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		double spd = 0.05;
		glColor4d(1, 1, 1, player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getBaseValue() / player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getAttribute().getDefaultValue() < 0.05 ? (Math.min(0.75 + (float) Math.cos((double) (player.ticksExisted + (ticksExisted * spd)) * 0.3) * 0.2, 1)) : 1);
		glTranslated(0, -0.15, 0);
		glRotated(model.bipedBody.rotateAngleX * (180F / (float) Math.PI), 1, 0, 0);
		
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
		float ry = 20F + (float) ((Math.sin((double) (player.ticksExisted + (ticksExisted * spd)) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
		
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
		
		glColor4d(1, 1, 1, 1);
		glEnable(GL_ALPHA_TEST);
		glDepthMask(true);
		glEnable(GL_LIGHTING);
		glDisable(GL_BLEND);
		//glEnable(GL_CULL_FACE);
		glPopMatrix();
		
		if (Minecraft.getMinecraft().theWorld.getTotalWorldTime() % 10 == 0) {
			double x = player.posX - 0.25;
			double y = player.posY - 0.5 + (Minecraft.getMinecraft().thePlayer.equals(player) ? 0 : 1.5);
			double z = player.posZ - 0.25;
			Botania.proxy.sparkleFX(player.worldObj, x + Math.random() * player.width * 2.0, y + Math.random() * 0.8, z + Math.random() * player.width * 2.0, 1, 1, 1, 2F * (float) Math.random(), 20);
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
		return wings[EnumRace.getRaceID(player)];
	}
	
	public static ResourceLocation getPlayerIconTexture(EntityPlayer player) {
		return icons[EnumRace.getRaceID(player)];
	}
}
