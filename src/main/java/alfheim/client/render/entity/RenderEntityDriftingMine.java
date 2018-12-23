package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderEntityDriftingMine extends Render {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/mine.obj"));
	
	public RenderEntityDriftingMine() {
		super();
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return LibResourceLocations.mine2;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		glPushMatrix();
		glTranslated(x, y+0.5, z);
		glRotated(((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + entity.ticksExisted) + Minecraft.getMinecraft().timer.renderPartialTicks) * 0.5, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine1);
		model.renderPart("insphere");
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine2);
		model.renderPart("outsphere");
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.mine3);
		model.renderPart("spikes");
		glPopMatrix();
	}
}