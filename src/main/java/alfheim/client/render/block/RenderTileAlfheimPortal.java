package alfheim.client.render.block;

import static org.lwjgl.opengl.GL11.*;

import alfheim.common.block.BlockAlfheimPortal;
import alfheim.common.block.tile.TileAlfheimPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTileAlfPortal;
import vazkii.botania.client.render.tile.RenderTilePylon;

public class RenderTileAlfheimPortal extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ticks) {
		TileAlfheimPortal portal = (TileAlfheimPortal) tileentity;
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
			glTranslated(1.25, 0, 1.75);
			glRotated(90, 0, 1, 0);
		}
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		renderIcon(0, 0, BlockAlfheimPortal.textures[2], 3, 3, 240);
		glTranslated(0, 0, 0.5);
		renderIcon(0, 0, BlockAlfheimPortal.textures[2], 3, 3, 240);

		glColor4d(1, 1, 1, 1);
		glEnable(GL_CULL_FACE);
		glEnable(GL_LIGHTING);
		glEnable(GL_ALPHA_TEST);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
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
