package alfheim.common.potion;

import alfheim.AlfheimCore;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

public class PotionQuadDamage extends PotionAlfheim {

	public PotionQuadDamage() {
		super(AlfheimConfig.potionIDQuadDamage, "quadDamage", false, 0x22FFFF);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl);
		if (AlfheimCore.enableMMO) SpellEffectHandler.sendPacket(Spells.QUAD, target);
	}
}
