package alfheim.common.spell.water;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellAquaStream;
import net.minecraft.entity.EntityLivingBase;

public class SpellAquaStream extends SpellBase {

	public SpellAquaStream() {
		super("aquastream", EnumRace.UNDINE, 2000, 100, 5);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellAquaStream(caster.worldObj, caster));
		return result;
	}
}