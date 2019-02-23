package alfheim.common.potion;

import java.util.UUID;

import alfheim.AlfheimCore;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

public class PotionSharedHP extends PotionAlfheim {

	public PotionSharedHP() {
		super(AlfheimConfig.potionIDSharedHP, "sharedHP", false, 0xFF0000);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl);
		if (!AlfheimCore.enableMMO) return;
		AttributeModifier m = new AttributeModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"), "sharedHP", ampl - target.getMaxHealth(), 0);
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl);
		if (!AlfheimCore.enableMMO) return;
		AttributeModifier m = target.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"));
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
		target.setHealth(Math.min(target.getHealth(), target.getMaxHealth()));
	}
}
