package alfheim.common.spell.earth;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellMortar;
import net.minecraft.entity.EntityLivingBase;

public class SpellMortar extends SpellBase {

	public SpellMortar() {
		super("mortar", EnumRace.GNOME, 7500, 200, 5);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellMortar(caster.worldObj, caster));
		return result;
	}
}