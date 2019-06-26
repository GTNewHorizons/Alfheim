package alfheim.client.render.item;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.AlfheimAPI;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.item.block.ItemBlockAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

//Render from Thaumcraft nodes by Azanor
public class RenderItemAnomaly implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return item != null && item.getItem() == Item.getItemFromBlock(AlfheimBlocks.anomaly) && !ItemBlockAnomaly.getType(item).equals(ItemBlockAnomaly.TYPE_UNDEFINED);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper != ItemRendererHelper.EQUIPPED_BLOCK;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.ENTITY) {
			glScaled(2, 2, 2);
			glTranslated(-0.5, -0.25, -0.5);
		} else if (type == ItemRenderType.EQUIPPED && data[1] instanceof EntityPlayer) {
			glTranslated(0, 0, -0.5);
		} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glRotated(93.2, 1, 0, 0);
			glTranslated(0, -0.5, -1);
		}
		
		renderItemAnomaly(AlfheimAPI.getAnomalyInstance(ItemBlockAnomaly.getType(item)));
	}

	public static void renderItemAnomaly(SubTileEntity subtile) {
		glPushMatrix();
		glAlphaFunc(GL_GREATER, 0.003921569F);
		glDepthMask(false);
		glDisable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4d(1, 1, 1, 1);
		glPushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.anomalies);
		int frames = subtile.getFrames();
		int frame = (int) ((System.nanoTime() / 40000000L + 1L) % (long) frames);
		int strip = subtile.getStrip();
		int color = subtile.getColor();
		
		glTranslated(0.5, 0.5, 0.5);
		renderAnimatedQuadStrip(1.5F, 1, frames, strip, frame, color);
		glRotated(90, 0, 1, 0);
		renderAnimatedQuadStrip(1.5F, 1, frames, strip, frame, color);
		glRotatef(90, 1, 0, 0);
		renderAnimatedQuadStrip(1.5F, 1, frames, strip, frame, color);
		
		glPopMatrix();
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glDepthMask(true);
		glAlphaFunc(GL_GREATER, 0.1F);
		glPopMatrix();
	}

	public static void renderAnimatedQuadStrip(float scale, float alpha, int frames, int strip, int cframe, int color) {
		if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setBrightness(220);
			tessellator.setColorRGBA_I(color, (int) (alpha * 255.0F));
			float f2 = (float) cframe / (float) frames;
			float f3 = (float) (cframe + 1) / (float) frames;
			float f4 = (float) strip / (float) frames;
			float f5 = (float) (strip + 1) / (float) frames;
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			tessellator.addVertexWithUV(-0.5D * (double) scale, 0.5D * (double) scale, 0.0D, (double) f2, (double) f5);
			tessellator.addVertexWithUV(0.5D * (double) scale, 0.5D * (double) scale, 0.0D, (double) f3, (double) f5);
			tessellator.addVertexWithUV(0.5D * (double) scale, -0.5D * (double) scale, 0.0D, (double) f3, (double) f4);
			tessellator.addVertexWithUV(-0.5D * (double) scale, -0.5D * (double) scale, 0.0D, (double) f2, (double) f4);
			tessellator.draw();
		}
	}
}
