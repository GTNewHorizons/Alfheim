package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderEntityHarp extends Render {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/Arfa.obj"));
	
	public RenderEntityHarp() {
		super();
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return LibResourceLocations.harp;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		glPushMatrix();
		glTranslated(x, y + 0.2 + Math.sin((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + entity.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) / 50.0) / 10.0, z);
		glRotated(((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + entity.ticksExisted) % 360 + Minecraft.getMinecraft().timer.renderPartialTicks) * 0.5, 0, 1, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(entity));
		model.renderAll();
		glPopMatrix();
	}
}