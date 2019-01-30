package alfheim.common.spell.wind;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellWindBlade;
import net.minecraft.entity.EntityLivingBase;

public class SpellWindBlade extends SpellBase {

	public SpellWindBlade() {
		super("windbalde", EnumRace.SYLPH, 8000, 120, 10);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellWindBlade(caster.worldObj, caster));
		return result;
	}
}