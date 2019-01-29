package alfheim.common.spell.tech;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellVisualizations;
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem;
import net.minecraft.entity.EntityLivingBase;

public class SpellTimeStop extends SpellBase {

	public SpellTimeStop() {
		super("timestop", EnumRace.LEPRECHAUN, 256000, 75000, 100, true);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) TimeStopSystem.stop(caster);
		return result;
	}
	
	@Override
	public void render(EntityLivingBase caster) {
		SpellVisualizations.negateSphere(caster, 0.5);
	}
}