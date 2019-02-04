package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.Random;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.client.model.block.ModelSimpleItemHolder;
import alfheim.common.block.tile.TileItemHolder;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.common.block.tile.mana.TilePool;

public class RenderTileItemHolder extends TileEntitySpecialRenderer {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/holder.obj"));
	public static final ModelSimpleItemHolder modelSimple = new ModelSimpleItemHolder();
	
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
		renderTE((TileItemHolder)tile, x, y, z, partialTicks);		
	}
	
	Random rand = new Random();
	
	private void renderTE(TileItemHolder tile, double x, double y, double z, float partialTicks) {
		boolean inf = false, dil = false, fab = false;
		int c = 0;
		types: {
			if (tile.getWorldObj() == null) break types;
			int meta = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord-1, tile.zCoord);
			inf = meta == 1;
			dil = meta == 2;
			fab = meta == 3;
			
			TileEntity te = tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord-1, tile.zCoord);
			if (te != null && te instanceof TilePool) c = ((TilePool) te).color;
		}
		
		glPushMatrix();
		glTranslated(x + 0.5, y - 0.5, z + 0.5);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(inf ? LibResourceLocations.poolPink : dil ? LibResourceLocations.poolBlue : LibResourceLocations.livingrock);

		if(fab) {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			if(tile != null) {
				rand.setSeed(tile.xCoord ^ tile.yCoord-1 ^ tile.zCoord);
				time += rand.nextInt(100000);
			}

			Color color = Color.getHSBColor(time * 0.005F, 0.6F, 1F);
			glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) 255);
		} else {
			float[] acolor = EntitySheep.fleeceColorTable[c];
			glColor4f(acolor[0], acolor[1], acolor[2], MultiblockRenderHandler.rendering ? 0.6F : 1F);
		}
		
		if (AlfheimConfig.minimalGraphics) {
			glPushMatrix();
			glTranslated(0, 1, 0);
			glRotated(180, 1, 0, 0);
			modelSimple.renderAll();
			glPopMatrix();
		} else model.renderAll();
		
		glRotated(90, 1, 0, 0);
		glRotated(135, 0, 0, 1);
		glTranslated(0, 0.05, -0.33);

		ItemContainingTileEntity.renderItem(tile);
		glPopMatrix();
	}
}