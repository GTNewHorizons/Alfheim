package alfheim.client.model.armor;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.AdvancedArmorModel;
import alfheim.api.ModInfo;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.client.core.helper.ShaderHelper;

public class ModelElvoriumArmor extends AdvancedArmorModel {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/ElvoriumArmor.obj"));
	public static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/model/armor/ElvoriumArmor.png");
	private final int partType;

	/**armorType: 0 - head, 1 - body and arms, 2 - legs, 3 - feet.**/
	public ModelElvoriumArmor(int armorType) {
		partType = armorType;
	}

	@Override
	public void pre(Entity entity) {
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("GedeonGrays")) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if (Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals("GedeonGrays") && !AlfheimConfig.fancies) return;
			ShaderHelper.useShader(ShaderHelper.halo);
		}
	}

	@Override
	public void post(Entity entity) {
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("GedeonGrays")) {
			GL11.glDisable(GL11.GL_BLEND);
			ShaderHelper.releaseShader();
		}
	}

	@Override
	public void partHead(Entity entity) {
		if (partType == 0) {
			double s = 0.01;
			GL11.glTranslatef(0F, -0.75F, 0F);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("Head");
		}
	}

	@Override
	public void partBody(Entity entity) {
		if (partType == 1) {
			double s = 0.01;
			GL11.glTranslated(0, -0.75, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("Body");
		}
		if (partType == 2) {
			double s = 0.01;
			GL11.glTranslated(0, -0.73, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("Belt");
		}
	}

	@Override
	public void partRightArm(Entity entity) {
		if (partType == 1) {
			double s = 0.01;
			GL11.glTranslated(0.31, -0.55, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("ArmO");
		}
	}

	@Override
	public void partLeftArm(Entity entity) {
		if (partType == 1) {
			double s = 0.01;
			GL11.glTranslated(-0.31, -0.55, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("ArmT");
		}
	}

	@Override
	public void partRightLeg(Entity entity) {
		if (partType == 2) {
			double s = 0.01;
			GL11.glTranslated(0.125, 0.01, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("pantsO");
		}
		if (partType == 3) {
			double s = 0.01;
			GL11.glTranslated(0.125, 0, 0);
			GL11.glScaled(s, s, s);
			model.renderPart("BootO");
		}
	}

	@Override
	public void partLeftLeg(Entity entity) {
		if (partType == 2) {
			double s = 0.01;
			GL11.glTranslated(-0.125, 0.01, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("PantsT");
		}
		if (partType == 3) {
			double s = 0.01;
			GL11.glTranslated(-0.125, 0, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("BootT");
		}
	}
}