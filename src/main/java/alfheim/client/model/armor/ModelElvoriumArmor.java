package alfheim.client.model.armor;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.render.AdvancedArmorModel;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.client.core.helper.ShaderHelper;

public class ModelElvoriumArmor extends AdvancedArmorModel {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/ElvoriumArmor.obj"));
	private final int partType;

	/**armorType: 0 - head, 1 - body and arms, 2 - legs, 3 - feet.**/
	public ModelElvoriumArmor(int armorType) {
		partType = armorType;
	}

	@Override
	public void pre(Entity entity) {
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.elvoriumArmor);
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("GedeonGrays")) {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			if (Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals("GedeonGrays") && !AlfheimConfig.fancies) return;
			ShaderHelper.useShader(ShaderHelper.halo);
		}
	}

	@Override
	public void post(Entity entity) {
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("GedeonGrays")) {
			glDisable(GL_BLEND);
			ShaderHelper.releaseShader();
		}
	}

	@Override
	public void partHead(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 0) {
			double s = 0.01;
			glTranslatef(0F, -0.75F, 0F);
			glScaled(s, s, s);
			model.renderPart("Head");
		}
	}

	@Override
	public void partBody(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 1) {
			double s = 0.01;
			glTranslated(0, -0.75, 0);
			glScaled(s, s, s);
			model.renderPart("Body");
		} else
		if (partType == 2) {
			double s = 0.01;
			glTranslated(0, -0.73, 0);
			glScaled(s, s, s);
			model.renderPart("Belt");
		}
	}

	@Override
	public void partRightArm(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 1) {
			double s = 0.01;
			glTranslated(0.31, -0.55, 0);
			glScaled(s, s, s);
			model.renderPart("ArmO");
		}
	}

	@Override
	public void partLeftArm(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 1) {
			double s = 0.01;
			glTranslated(-0.31, -0.55, 0);
			glScaled(s, s, s);
			model.renderPart("ArmT");
		}
	}

	@Override
	public void partRightLeg(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 2) {
			double s = 0.01;
			glTranslated(0.125, 0.01, 0);
			glScaled(s, s, s);
			model.renderPart("pantsO");
		} else
		if (partType == 3) {
			double s = 0.01;
			glTranslated(0.125, 0, 0);
			glScaled(s, s, s);
			model.renderPart("BootO");
		}
	}

	@Override
	public void partLeftLeg(Entity e) {
		if (ink(e, partType)) return;
		if (partType == 2) {
			double s = 0.01;
			glTranslated(-0.125, 0.01, 0);
			glScaled(s, s, s);
			model.renderPart("PantsT");
		} else
		if (partType == 3) {
			double s = 0.01;
			glTranslated(-0.125, 0, 0);
			glScaled(s, s, s);
			model.renderPart("BootT");
		}
	}
	
	public static boolean ink(Entity e, int slot) {
		slot = 3 - slot;
		return e != null && e instanceof EntityPlayer && ((EntityPlayer) e).inventory.armorItemInSlot(slot) != null && ((EntityPlayer) e).inventory.armorItemInSlot(slot).getItem() instanceof IPhantomInkable && ((IPhantomInkable) ((EntityPlayer) e).inventory.armorItemInSlot(slot).getItem()).hasPhantomInk(((EntityPlayer) e).inventory.armorItemInSlot(slot));
	}
}