package alfheim.common.spell.tech;

import alfheim.AlfheimCore;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.render.world.SpellVisualizations;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class SpellTimeStop extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) TimeStopSystem.stop(caster);
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.LEPRECHAUN;
	}

	@Override
	public String getName() {
		return "timestop";
	}

	@Override
	public int getManaCost() {
		return 256000;
	}

	@Override
	public int getCooldown() {
		return 75000;
	}

	@Override
	public int castTime() {
		return 100;
	}

	@Override
	public boolean isHard() {
		return true;
	}

	@Override
	public void render(EntityLivingBase caster) {
		SpellVisualizations.negateSphere(caster, 0.5);
	}
}