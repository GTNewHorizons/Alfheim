package alfheim.client.render.block;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Color;

import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre;
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockCustomOreItem;

public class RenderBlockAlfheimThaumOre extends BlockRenderer implements ISimpleBlockRenderingHandler {
	
	public void renderInventoryBlock(Block block, int metad, int modelID, RenderBlocks renderer) {
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBoundsFromBlock(block);
		if (metad == 0) {
			drawFaces(renderer, block, ((BlockAlfheimThaumOre) block).icon[0], false);
		} else if (metad == 7) {
			drawFaces(renderer, block, ((BlockAlfheimThaumOre) block).icon[3], false);
		} else if (metad < 7) {
			drawFaces(renderer, block, ((BlockAlfheimThaumOre) block).icon[1], false);
			Color c = new Color(BlockCustomOreItem.colors[metad]);
			float r = (float) c.getRed() / 255.0F;
			float g = (float) c.getGreen() / 255.0F;
			float b = (float) c.getBlue() / 255.0F;
			glColor3f(r, g, b);
			block.setBlockBounds(0.005F, 0.005F, 0.005F, 0.995F, 0.995F, 0.995F);
			renderer.setRenderBoundsFromBlock(block);
			drawFaces(renderer, block, ((BlockAlfheimThaumOre) block).icon[2], false);
			glColor3f(1.0F, 1.0F, 1.0F);
		}
	}
	
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int bb = setBrightness(world, x, y, z, block);
		int metadata = world.getBlockMetadata(x, y, z);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBoundsFromBlock(block);
		renderer.renderStandardBlock(block, x, y, z);
		if (metadata != 0 && metadata < 7) {
			Tessellator t = Tessellator.instance;
			t.setColorOpaque_I(BlockCustomOreItem.colors[metadata]);
			t.setBrightness(Math.max(bb, 160));
			renderAllSides(world, x, y, z, block, renderer, ((BlockAlfheimThaumOre) block).icon[2], false);
			if (Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1) {
				block.setBlockBounds(0.005F, 0.005F, 0.005F, 0.995F, 0.995F, 0.995F);
				renderer.setRenderBoundsFromBlock(block);
				t.setBrightness(bb);
				renderAllSides(world, x, y, z, block, renderer, Blocks.stone.getIcon(0, 0), false);
			}
		}
		
		renderer.clearOverrideBlockTexture();
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.setRenderBoundsFromBlock(block);
		return true;
	}
	
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	public int getRenderId() {
		return ThaumcraftAlfheimModule.renderIDOre;
	}
}