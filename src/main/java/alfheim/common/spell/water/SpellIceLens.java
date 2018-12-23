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

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(new PotionEffect(AlfheimRegistry.icelens.id, 200, 0, true));
			
			if (caster instanceof EntityPlayerMP) AlfheimCore.network.sendTo(new MessageParticles(Spells.ICELENS.ordinal(), 0, 0, 0), (EntityPlayerMP) caster);
		}
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.UNDINE;
	}

	@Override
	public String getName() {
		return "icelens";
	}

	@Override
	public int getManaCost() {
		return 6000;
	}

	@Override
	public int getCooldown() {
		return 1200;
	}

	@Override
	public int castTime() {
		return 30;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}