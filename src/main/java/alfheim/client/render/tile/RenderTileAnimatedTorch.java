package alfheim.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;

import static org.lwjgl.opengl.GL11.*;

import alfheim.common.block.tile.TileAnimatedTorch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class RenderTileAnimatedTorch extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
		render((TileAnimatedTorch) te, x, y, z, partialTicks);
	}
	
	public void render(TileAnimatedTorch tile, double x, double y, double z, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		glPushMatrix();
		glTranslated(x, y, z);

		boolean hasWorld = tile != null && tile.getWorldObj() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		
		if(wtime != 0)
			wtime += new Random(tile.xCoord ^ tile.yCoord ^ tile.zCoord).nextInt(360); // FIXME remove instanciating

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
			GL11.glDisable(GL11.GL_CULL_FACE);
			EntityItem entityitem = new EntityItem(hasWorld ? tile.getWorldObj() : Minecraft.getMinecraft().theWorld, 0.0, 0.0, 0.0, new ItemStack(Blocks.redstone_torch));
			entityitem.hoverStart = 0.0F;
			GL11.glPushMatrix();
			Tessellator.instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord));
			RenderItem.renderInFrame = true;
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0F, 0.0F);
			RenderItem.renderInFrame = false;
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();
		}
		
		glPopMatrix();
	}
}
