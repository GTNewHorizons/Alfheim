package alfheim.client.render.item;

import org.lwjgl.opengl.GL11;

import alfheim.client.render.block.RenderTileAnyavil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemAnyavil implements IItemRenderer {

	@Override
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
	}
}