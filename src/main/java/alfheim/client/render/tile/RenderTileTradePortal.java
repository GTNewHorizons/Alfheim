package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

import alfheim.common.block.BlockTradePortal;
import alfheim.common.block.tile.TileTradePortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class RenderTileTradePortal extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float ticks) {
		TileTradePortal portal = (TileTradePortal) tile;
		int meta = portal.getBlockMetadata();
		if(meta == 0)
			return;

		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_ALPHA_TEST);
		glDisable(GL_LIGHTING);
		glDisable(GL_CULL_FACE);
		glColor4d(1, 1, 1, Math.min(1, (Math.sin((ClientTickHandler.ticksInGame + ticks) / 8) + 1) / 7 + 0.6) * (Math.min(60, portal.ticksOpen) / 60) * 0.85);

		glTranslated(x - 1, y + 1, z + 0.25);
		if(meta == 2) {
			glTranslated(1.25, 0, 1.75);
			glRotated(90, 0, 1, 0);
		}
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		renderIcon(0, 0, BlockTradePortal.textures[2], 3, 3, 240);
		glTranslated(0, 0, 0.5);
		renderIcon(0, 0, BlockTradePortal.textures[2], 3, 3, 240);

		glColor4d(1, 1, 1, 1);
		if (portal.isTradeOn()){
			if (meta == 2) glTranslated(0.05, 0, 0);
			if (meta == 1) glTranslated(0.046875, 0, 0);
			glTranslated(1.453125, -0.6640625, 0.251);
			ItemStack out = portal.getOutput();
			if (out.getItem() instanceof ItemBlock) glTranslated(0, 0, -0.140625);
			else glTranslated(0, -0.04, 0);
			renderItem(tile, out);
			glRotated(180, 0, 1, 0);
			if (out.getItem() instanceof ItemBlock) glTranslated(0, 0, 0.72075);
			else glTranslated(0.01, 0, 1.002);
			renderItem(tile, out);
		}
		
		glEnable(GL_CULL_FACE);
		glEnable(GL_LIGHTING);
		glEnable(GL_ALPHA_TEST);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public void renderIcon(double par1, double par2, IIcon par3Icon, double par4, double par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}
	
	public void renderItem(TileEntity tile, ItemStack stack) {
		if (stack == null) return;
		EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0, 0.0, 0.0, stack);
		entityitem.getEntityItem().stackSize = 1;
		entityitem.hoverStart = 0.0F;
		RenderItem.renderInFrame = true;
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0, 0, 0, 0, 0);
		RenderItem.renderInFrame = false;
	}
}
