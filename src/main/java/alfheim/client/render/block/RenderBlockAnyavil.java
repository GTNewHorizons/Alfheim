package alfheim.client.render.block;

import org.lwjgl.opengl.GL11;

import alfheim.client.lib.LibRenderIDs;
import alfheim.common.block.tile.TileAnyavil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

public class RenderBlockAnyavil implements ISimpleBlockRenderingHandler /*IItemRenderer*/ {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glRotated(-90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.6F, -0.5F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileAnyavil(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idAnyavil;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	/*@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		GL11.glRotated(90, 0, 1, 0);
		if (type == ItemRenderType.EQUIPPED){
			GL11.glTranslated(-0.5, 0, 0.5);
		} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			GL11.glTranslated(-0.5, 0.5, 0.5);
		} else if (type == ItemRenderType.INVENTORY) {
			GL11.glTranslated(0, -0.1, 0);
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(RenderTileAnyavil.texture);
		RenderTileAnyavil.model.renderAll();
		GL11.glPopMatrix();
	}*/
}