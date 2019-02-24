package alfheim.common.potion;

import java.util.UUID;

import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

public class PotionTank extends PotionAlfheim {
	
	public PotionTank() {
		super(AlfheimConfig.potionIDTank, "tank", false, 0xFFDD00);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.applyAttributesModifiersToEntity(target, map, mod);
		AttributeModifier m = new AttributeModifier(UUID.fromString("4107C0A4-DB3F-A48D-D9AA-C2701B32B143"), "tank", -0.2, 2);
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m);
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap map, int mod) {
		super.removeAttributesModifiersFromEntity(target, map, mod);
		AttributeModifier m = target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUID.fromString("4107C0A4-DB3F-A48D-D9AA-C2701B32B143"));
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m);
	}
}
