package alfheim.common.spell.water;

import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

public class SpellIceLens extends SpellBase {

	public SpellIceLens() {
		super("icelens", EnumRace.UNDINE, 6000, 1200, 30);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(new PotionEffect(AlfheimRegistry.icelens.id, 200, 0, true));
			
			if (caster instanceof EntityPlayerMP) AlfheimCore.network.sendTo(new MessageParticles(Spells.ICELENS.ordinal(), 0, 0, 0), (EntityPlayerMP) caster);
		}
		return result;
	}
}