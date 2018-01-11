package alfheim.client.render.block;

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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class RenderBlockTradePortal extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ticks) {
		TileTradePortal portal = (TileTradePortal) tileentity;
		int meta = portal.getBlockMetadata();
		if(meta == 0)
			return;

		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_ALPHA_TEST);
		glDisable(GL_LIGHTING);
		glDisable(GL_CULL_FACE);
		glColor4d(1, 1, 1, Math.min(1, (Math.sin((ClientTickHandler.ticksInGame + ticks) / 8) + 1) / 7 + 0.6) * (Math.min(60, portal.ticksOpen) / 60) * 0.5);

		glTranslated(x - 1, y + 1, z + 0.25);
		if(meta == 2) {
			glTranslated(1.25, 0, 1.7);
			glRotated(90, 0, 1, 0);
		}
		
		// TODO fix render
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		//renderIcon(1, 1, BlockTradePortal.textures[2], 1, 1, 240);
		glTranslated(0, 0, 0.5);
		//renderIcon(1, 1, BlockTradePortal.textures[2], 1, 1, 240);

		glColor4d(1, 1, 1, 1);
		if (portal.isTradeOn()){
			glTranslated(0, 0, -0.25);
			IIcon icon;
			ItemStack out = portal.getOutput();
			if (out.getItem() instanceof ItemBlock) {
				icon = Block.getBlockFromItem(out.getItem()).getIcon(1, out.getItemDamage());
			} else {
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
				icon = out.getItem().getIconFromDamage(portal.getOutput().getItemDamage());
			}
			renderIcon(0.5, 0.5, icon, 2, 2, 240);
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
}
