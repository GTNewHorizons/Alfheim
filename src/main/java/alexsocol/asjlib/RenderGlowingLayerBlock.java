package alexsocol.asjlib;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public class RenderGlowingLayerBlock implements ISimpleBlockRenderingHandler {

	public static final int glowBlockID = RenderingRegistry.getNextAvailableRenderId();
	
	static {
		RenderingRegistry.registerBlockHandler(glowBlockID, new RenderGlowingLayerBlock());
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		this.renderInvNormalBlock(renderer, block, metadata);
	}

	public static void renderInvNormalBlock(RenderBlocks renderer, Block block, int meta) {
		Tessellator tes = Tessellator.instance;
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBounds(0F, 0F, 0F, 1F, 1F, 1F);
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-0.5F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
		tes.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderStandardBlock(block, x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		Tessellator tes = Tessellator.instance;
		tes.draw();
		
		tes.addTranslation(x, y, z);
		GL11.glPushMatrix();
		tes.startDrawingQuads();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		tes.setColorRGBA_F(1F, 1F, 1F, 1F);
		
		// Facing X-Pos 5
		IIcon xpos = ((IGlowingLayerBlock)block).getGlowIcon(5, meta);
		if(xpos != null) {
			tes.addVertexWithUV(0, 0, 0, xpos.getMinU(), xpos.getMaxV());
			tes.addVertexWithUV(0, 0, 1, xpos.getMaxU(), xpos.getMaxV());
			tes.addVertexWithUV(0, 1, 1, xpos.getMaxU(), xpos.getMinV());
			tes.addVertexWithUV(0, 1, 0, xpos.getMinU(), xpos.getMinV());
		}	
		
		// Facing X-Neg 4
		IIcon xneg = ((IGlowingLayerBlock)block).getGlowIcon(4, meta);
		if (xneg != null) {
			tes.addVertexWithUV(1, 0, 0, xneg.getMaxU(), xneg.getMaxV());
			tes.addVertexWithUV(1, 1, 0, xneg.getMaxU(), xneg.getMinV());
			tes.addVertexWithUV(1, 1, 1, xneg.getMinU(), xneg.getMinV());
			tes.addVertexWithUV(1, 0, 1, xneg.getMinU(), xneg.getMaxV());
		}
				
		// Facing Y-Pos 1
		IIcon ypos = ((IGlowingLayerBlock)block).getGlowIcon(1, meta);
		if (ypos != null) {
			tes.addVertexWithUV(0, 1, 0, ypos.getMinU(), ypos.getMinV());
			tes.addVertexWithUV(0, 1, 1, ypos.getMinU(), ypos.getMaxV());
			tes.addVertexWithUV(1, 1, 1, ypos.getMaxU(), ypos.getMaxV());
			tes.addVertexWithUV(1, 1, 0, ypos.getMaxU(), ypos.getMinV());
		}
		
		// Facing Y-Neg 0
		IIcon yneg = ((IGlowingLayerBlock)block).getGlowIcon(0, meta);
		if (yneg != null) {
			tes.addVertexWithUV(0, 0, 0, yneg.getMinU(), yneg.getMinV());
			tes.addVertexWithUV(1, 0, 0, yneg.getMaxU(), yneg.getMinV());
			tes.addVertexWithUV(1, 0, 1, yneg.getMaxU(), yneg.getMaxV());
			tes.addVertexWithUV(0, 0, 1, yneg.getMinU(), yneg.getMaxV());
		}
				
		// Facing Z-Pos 3
		IIcon zpos = ((IGlowingLayerBlock)block).getGlowIcon(3, meta);
		if (zpos != null) {
			tes.addVertexWithUV(0, 0, 1, zpos.getMinU(), zpos.getMaxV());
			tes.addVertexWithUV(1, 0, 1, zpos.getMaxU(), zpos.getMaxV());
			tes.addVertexWithUV(1, 1, 1, zpos.getMaxU(), zpos.getMinV());
			tes.addVertexWithUV(0, 1, 1, zpos.getMinU(), zpos.getMinV());
		}
				
		// Facing Z-Neg 2
		IIcon zneg = ((IGlowingLayerBlock)block).getGlowIcon(2, meta);
		if (zneg != null) {
			tes.addVertexWithUV(0, 0, 0, zneg.getMaxU(), zneg.getMaxV());
			tes.addVertexWithUV(0, 1, 0, zneg.getMaxU(), zneg.getMinV());
			tes.addVertexWithUV(1, 1, 0, zneg.getMinU(), zneg.getMinV());
			tes.addVertexWithUV(1, 0, 0, zneg.getMinU(), zneg.getMaxV());
		}
				
		tes.draw();
        GL11.glPopMatrix();
        
		tes.startDrawingQuads();
		tes.addTranslation(-x, -y, -z);
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return glowBlockID;
	}
}