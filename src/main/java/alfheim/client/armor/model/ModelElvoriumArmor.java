package alfheim.client.armor.model;

import org.lwjgl.opengl.GL11;

import alfheim.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public  class ModelElvoriumArmor extends AdvancedArmorModel {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID, "model/ElvoriumArmor.obj"));
	public static final ResourceLocation texture = new ResourceLocation(Constants.MODID, "textures/model/armor/ElvoriumArmor.png");
	private final int partType;

	/**armorType: 0 - head, 1 - body and arms, 2 - legs, 3 - feet.**/
	public ModelElvoriumArmor(int armorType) {
		partType = armorType;
	}

	@Override
	public void pre() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void post() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void partHead() {
		if (partType == 0) {
			double s = 0.01;
			GL11.glTranslatef(0F, -0.75F, 0F);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("Head");
		}
	}

	@Override
	public void partBody() {
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
	public void partRightArm() {
		if (partType == 1) {
			double s = 0.01;
			GL11.glTranslated(0.31, -0.55, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("ArmO");
		}
	}

	@Override
	public void partLeftArm() {
		if (partType == 1) {
			double s = 0.01;
			GL11.glTranslated(-0.31, -0.55, 0);
			GL11.glScaled(s, s, s);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			model.renderPart("ArmT");
		}
	}

	@Override
	public void partRightLeg() {
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
	public void partLeftLeg() {
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