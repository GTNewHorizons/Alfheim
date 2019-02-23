package alfheim.common.spell.tech;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellDriftingMine;
import net.minecraft.entity.EntityLivingBase;

public class SpellDriftingMine extends SpellBase {
	
	public SpellDriftingMine() {
		super("driftingmine", EnumRace.LEPRECHAUN, 6000, 1200, 15);
	}
	
	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCastOver(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellDriftingMine(caster.worldObj, caster));
		return result;
	}
}