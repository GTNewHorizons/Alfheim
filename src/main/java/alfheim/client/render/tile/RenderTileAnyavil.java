package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.block.tile.TileAnyavil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderTileAnyavil extends TileEntitySpecialRenderer {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/Anyavil.obj"));
	
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
		renderTE((TileAnyavil)tile, x, y, z, partialTicks);		
	}
	
	private void renderTE(TileAnyavil tile, double x, double y, double z, float partialTicks) {
		glPushMatrix();
		glTranslated(x + 0.5, y + 0.425, z + 0.5);
		glRotated(90 * (tile.blockMetadata + 1), 0, 1, 0);

		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.elementiumBlock);
		model.renderAll();

		glRotated(90, 1, 0, 0);
		glRotated(135, 0, 0, 1);
		glTranslated(0, 0.07, -0.6);
		glScaled(1.5, 1.5, 1.5);

		ItemContainingTileEntity.renderItem(tile);
		glPopMatrix();
	}
}