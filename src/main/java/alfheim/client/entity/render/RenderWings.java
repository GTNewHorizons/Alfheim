package alfheim.client.entity.render;

import org.lwjgl.opengl.GL11;

import alfheim.Constants;
import alfheim.common.entity.EnumRace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender.Helper;

public class RenderWings {
	
	private static final ResourceLocation[] wings = {
		null,
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SALAMANDER_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SYLPH_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/CAITSITH_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/POOKA_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/GNOME_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/LEPRECHAUN_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SPRIGGAN_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/UNDINE_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/IMP_wing.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/ALV_wing.png"),
	};
	
	private static final ResourceLocation[] icons = {
		null,
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SALAMANDER_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SYLPH_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/CAITSITH_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/POOKA_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/GNOME_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/LEPRECHAUN_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/SPRIGGAN_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/UNDINE_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/IMP_icon.png"),
		new ResourceLocation(Constants.MODID, "textures/model/entity/wings/ALV_icon.png"),
	};
	
	public static void render(RenderPlayerEvent.Specials.Post e, EntityPlayer player) {
		if (EnumRace.fromID(player.getEntityAttribute(Constants.RACE).getAttributeValue()) == EnumRace.HUMAN) return;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GL11.glColor4d(1, 1, 1, Math.min(0.75 + (float) Math.cos((double) (player.ticksExisted + e.partialRenderTick) * 0.3) * 0.2, 1));
		Helper.rotateIfSneaking(player);
		
		// Icon
		GL11.glPushMatrix();
		GL11.glTranslated(-0.25, 0, 0.15);
		double si = 0.5;
		GL11.glScaled(si, si, si);
		drawRect(Tessellator.instance, getPlayerIconTexture(player));
		GL11.glPopMatrix();
		
		boolean flying = player.capabilities.isFlying;
		float ry = 20F + (float) ((Math.sin((double) (player.ticksExisted + e.partialRenderTick) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));

		// Wing left
		GL11.glPushMatrix();
		GL11.glTranslated(0.25, 0.1, 0.15);
		double swr = 1.5;
		GL11.glScaled(swr, swr, swr);
		GL11.glRotated(10, 0, 0, 1);
		GL11.glRotated(-ry, 0, 1, 0);
		drawRect(Tessellator.instance, getPlayerWingTexture(player));
		GL11.glPopMatrix();
		
		// Wing right
		GL11.glPushMatrix();
		GL11.glTranslated(-0.25, 0.1, 0.15);
		double swl = 1.5;
		GL11.glScaled(-swl, swl, swl);
		GL11.glRotated(10, 0, 0, 1);
		GL11.glRotated(-ry, 0, 1, 0);
		drawRect(Tessellator.instance, getPlayerWingTexture(player));
		GL11.glPopMatrix();

		GL11.glColor4d(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
		GL11.glPopMatrix();
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
		return wings[EnumRace.fromID(player.getEntityAttribute(Constants.RACE).getAttributeValue()).ordinal()];
	}
	
	public static ResourceLocation getPlayerIconTexture(EntityPlayer player) {
		return icons[EnumRace.fromID(player.getEntityAttribute(Constants.RACE).getAttributeValue()).ordinal()];
	}
}
