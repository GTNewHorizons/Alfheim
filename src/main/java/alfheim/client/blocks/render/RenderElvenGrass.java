package alfheim.client.blocks.render;

import org.lwjgl.opengl.GL11;

import alfheim.common.blocks.ElvenGrass;
import alfheim.common.registry.AlfheimBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderElvenGrass implements ISimpleBlockRenderingHandler {

	public static final int elvenGrassRendererID = RenderingRegistry.getNextAvailableRenderId();
	
	static {
		RenderingRegistry.registerBlockHandler(elvenGrassRendererID, new RenderElvenGrass());
	}
	
	@Override
	public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
		// TODO try renderer.renderBlockAsItem(block, 1, 0.0F);
		
		GL11.glPushMatrix();
		Tessellator tes = Tessellator.instance;
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBounds(0F, 0F, 0F, 1F, 1F, 1F);
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
		tes.setNormal(0.0F, 0.8F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
		tes.setNormal(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
		tes.setNormal(-0.5F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
		tes.draw();
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		/*Tessellator tes = Tessellator.instance;
		tes.draw();
		tes.addTranslation(x, y, z);
		tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		tes.startDrawingQuads();
				
		if (block.shouldSideBeRendered(world, x, y - 1, z, 0)) renderer.renderFaceYNeg(block, x, y, z, block.getIcon(world, x, y, z, 0));
		if (block.shouldSideBeRendered(world, x + 1, y, z, 2)) renderer.renderFaceXPos(block, x, y, z, block.getIcon(world, x, y, z, 2));
		if (block.shouldSideBeRendered(world, x - 1, y, z, 3)) renderer.renderFaceXNeg(block, x, y, z, block.getIcon(world, x, y, z, 3));
		if (block.shouldSideBeRendered(world, x, y, z - 1, 4)) renderer.renderFaceZNeg(block, x, y, z, block.getIcon(world, x, y, z, 4));
		if (block.shouldSideBeRendered(world, x, y, z + 1, 5)) renderer.renderFaceZPos(block, x, y, z, block.getIcon(world, x, y, z, 5));
		
		// Colored
		tes.setColorOpaque_I(block.colorMultiplier(world, x, y, z));
		if (block.shouldSideBeRendered(world, x, y + 1, z, 1)) renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(world, x, y, z, 1));
		IIcon overlay = world.getBlock(x, y + 1, z).getMaterial() != Material.snow && world.getBlock(x, y + 1, z).getMaterial() != Material.craftedSnow ? ElvenGrass.getIconSideOverlay() : null;
		if (overlay != null) {
			if (block.shouldSideBeRendered(world, x + 1, y, z, 2)) renderer.renderFaceXPos(block, x, y, z, overlay);
			if (block.shouldSideBeRendered(world, x - 1, y, z, 3)) renderer.renderFaceXNeg(block, x, y, z, overlay);
			if (block.shouldSideBeRendered(world, x, y, z - 1, 4)) renderer.renderFaceZNeg(block, x, y, z, overlay);
			if (block.shouldSideBeRendered(world, x, y, z + 1, 5)) renderer.renderFaceZPos(block, x, y, z, overlay);
		}
		
		tes.draw();
		tes.startDrawingQuads();
		return false;*/
		
		renderer.renderStandardBlock(block, x, y, z);
		renderer.renderStandardBlock(ElvenGrass.grassCrutch, x, y, z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return elvenGrassRendererID;
	}
}