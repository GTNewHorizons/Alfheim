package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import alfheim.common.block.tile.TileAnimatedTorch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class RenderTileAnimatedTorch extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
		render((TileAnimatedTorch) te, x, y, z, partialTicks);
	}
	
	Random rand = new Random();
	
	public void render(TileAnimatedTorch tile, double x, double y, double z, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		glPushMatrix();
		glTranslated(x, y, z);

		boolean hasWorld = tile != null && tile.getWorldObj() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		
		if(wtime != 0) {
			rand.setSeed(tile.xCoord ^ tile.yCoord ^ tile.zCoord);
			wtime += rand.nextInt(360);
		}
			
		float time = wtime == 0 ? 0 : wtime + partialTicks;
		double xt = 0.5 + Math.cos(time * 0.05) * 0.025;
		double yt = 0.1 + (Math.sin(time * 0.04) + 1) * 0.05;
		double zt = 0.5 + Math.sin(time * 0.05) * 0.025;
		glTranslated(xt, yt, zt);

		glScaled(2, 2, 2);
		glRotated(90, 1, 0, 0);
		float rotation = (float) tile.rotation;
		if(tile.rotating)
			rotation += tile.anglePerTick * partialTicks;

		glRotated(rotation, 0F, 0F, 1F);
		glTranslated(0, 0.15, 0);
		
		{
			glDisable(GL_CULL_FACE);
			EntityItem entityitem = new EntityItem(hasWorld ? tile.getWorldObj() : Minecraft.getMinecraft().theWorld, 0.0, 0.0, 0.0, new ItemStack(Blocks.redstone_torch));
			entityitem.hoverStart = 0.0F;
			glPushMatrix();
			Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord));
			RenderItem.renderInFrame = true;
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0F, 0.0F);
			RenderItem.renderInFrame = false;
			glEnable(GL_CULL_FACE);
			glPopMatrix();
		}
		
		glPopMatrix();
	}
}
