package alfheim.common.spell.wind;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellFenrirStorm;
import net.minecraft.entity.EntityLivingBase;

public class SpellFenrirStorm extends SpellBase {

	public SpellFenrirStorm() {
		super("fenrirstorm", EnumRace.SYLPH, 1000, 100, 5);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellFenrirStorm(caster.worldObj, caster));
		return result;
	}
}