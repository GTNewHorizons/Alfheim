package alexsocol.asjlib.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
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
		GL11.glPushMatrix();
		Tessellator tes = Tessellator.instance;
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBounds(0F, 0F, 0F, 1F, 1F, 1F);
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getIcon(0, meta));
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getIcon(1, meta));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getIcon(2, meta));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getIcon(3, meta));
		tes.setNormal(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getIcon(4, meta));
		tes.setNormal(-0.5F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getIcon(5, meta));
		tes.draw();
		
		GL11.glDisable(GL11.GL_LIGHTING);
		tes.startDrawingQuads();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(0, meta));
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(1, meta));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(2, meta));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(3, meta));
		tes.setNormal(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(4, meta));
		tes.setNormal(-0.5F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, ((IGlowingLayerBlock)block).getGlowIcon(5, meta));
		tes.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int meta = world.getBlockMetadata(x, y, z);
		Tessellator tes = Tessellator.instance;
		renderer.renderStandardBlock(block, x, y, z);
		tes.draw();
		
		GL11.glPushMatrix();

		tes.startDrawingQuads();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		tes.setColorRGBA_F(1F, 1F, 1F, 1F);
		if (((IGlowingLayerBlock)block).getGlowIcon(0, meta) != null && block.shouldSideBeRendered(world, x, y - 1, z, 0)) renderer.renderFaceYNeg(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(0, meta));
		if (((IGlowingLayerBlock)block).getGlowIcon(1, meta) != null && block.shouldSideBeRendered(world, x, y + 1, z, 1)) renderer.renderFaceYPos(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(1, meta));
		if (((IGlowingLayerBlock)block).getGlowIcon(2, meta) != null && block.shouldSideBeRendered(world, x + 1, y, z, 2)) renderer.renderFaceXPos(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(2, meta));
		if (((IGlowingLayerBlock)block).getGlowIcon(3, meta) != null && block.shouldSideBeRendered(world, x - 1, y, z, 3)) renderer.renderFaceXNeg(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(3, meta));
		if (((IGlowingLayerBlock)block).getGlowIcon(4, meta) != null && block.shouldSideBeRendered(world, x, y, z - 1, 4)) renderer.renderFaceZNeg(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(4, meta));
		if (((IGlowingLayerBlock)block).getGlowIcon(5, meta) != null && block.shouldSideBeRendered(world, x, y, z + 1, 5)) renderer.renderFaceZPos(block, x, y, z, ((IGlowingLayerBlock)block).getGlowIcon(5, meta));
		tes.draw();
				
		GL11.glPopMatrix();
		
		tes.startDrawingQuads();
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