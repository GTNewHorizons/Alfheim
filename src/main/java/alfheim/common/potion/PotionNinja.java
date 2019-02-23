package alfheim.common.potion;

import java.util.UUID;

import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

public class PotionNinja extends PotionAlfheim {
	
	public PotionNinja() {
		super(AlfheimConfig.potionIDNinja, "ninja", false, 0xCCCCCC);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.applyAttributesModifiersToEntity(target, map, mod);
		AttributeModifier m = new AttributeModifier(UUID.fromString("3899DCBA-B79F-92AF-727C-2190BBD8ABC5"), "ninja", 0.2, 2);
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m);
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.removeAttributesModifiersFromEntity(target, map, mod);
		AttributeModifier m = target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUID.fromString("3899DCBA-B79F-92AF-727C-2190BBD8ABC5"));
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m);
	}
}
