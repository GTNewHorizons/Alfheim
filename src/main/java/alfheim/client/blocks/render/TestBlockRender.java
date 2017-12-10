package alfheim.client.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;

public class TestBlockRender extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		float alphaMod = 1.0F;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		GL11.glRotated(90F, 1F, 0F, 0F);
		GL11.glTranslatef(0F, 0F, -3F / 16F - 0.001F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;
		if(ShaderHelper.useShaders())
			GL11.glColor4f(1F, 1F, 1F, alpha);
		else {
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			GL11.glColor4f(0.6F + (float) ((Math.cos((ClientTickHandler.ticksInGame + f) / 6D) + 1D) / 5D), 0.1F, 0.9F, alpha);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		ShaderHelper.useShader(ShaderHelper.terraPlateRune);
		renderIcon(0, 0, FluidRegistry.LAVA.getStillIcon(), 1, 1, 240);
		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
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