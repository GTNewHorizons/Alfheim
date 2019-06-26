package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;

public class RenderEntityMortar extends Render {

	final RenderBlocks render = new RenderBlocks();
	
	public RenderEntityMortar() {
		shadowSize = 0.5F;
	}

	public ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}

	@Override
	public void doRender(Entity e, double x, double y, double z, float yaw, float ticks) {
		glPushMatrix();
		glDisable(GL_LIGHTING);
		glTranslated(x, y, z);
		bindEntityTexture(e);
		render.setRenderBoundsFromBlock(ModBlocks.livingrock);
		render.renderBlockSandFalling(ModBlocks.livingrock, e.worldObj, MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posY), MathHelper.floor_double(e.posZ), 0);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}
}
