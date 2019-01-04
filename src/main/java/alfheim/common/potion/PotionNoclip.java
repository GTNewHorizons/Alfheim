package alfheim.common.potion;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;

public class PotionNoclip extends PotionAlfheim {

	public PotionNoclip() {
		super(AlfheimConfig.potionIDNoclip, "noclip", false, 0xAAAAAA);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		if (!AlfheimCore.enableMMO) return;
		if (target instanceof EntityPlayer) {
			((EntityPlayer) target).capabilities.allowFlying = true;
			((EntityPlayer) target).capabilities.isFlying = true;
			((EntityPlayer) target).onGround = false;
			((EntityPlayer) target).sendPlayerAbilities();
		}
		target.noClip = true;
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		if (!AlfheimCore.enableMMO) return;
		target.noClip = false;
		if (ASJUtilities.isServer()) AlfheimCore.network.sendToAll(new MessageEffect(target.getEntityId(), AlfheimRegistry.noclip.id, 0, 0));
	}
}
