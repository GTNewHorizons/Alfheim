package alfheim.common.potion;

import java.util.UUID;

import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

public class PotionBerserk extends PotionAlfheim {
	
	public PotionBerserk() {
		super(AlfheimConfig.potionIDBerserk, "berserk", false, 0xAA1111);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.applyAttributesModifiersToEntity(target, map, mod);
		AttributeModifier m = new AttributeModifier(UUID.fromString("23593E88-FFE3-1707-DE94-6BCD1756B35D"), "berserk", -0.2, 2);
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m);
		target.setHealth(Math.min(target.getHealth(), target.getMaxHealth()));
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.removeAttributesModifiersFromEntity(target, map, mod);
		AttributeModifier m = target.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString("23593E88-FFE3-1707-DE94-6BCD1756B35D"));
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m);
	}
}
