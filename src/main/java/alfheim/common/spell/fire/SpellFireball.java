package alfheim.common.spell.fire;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellFireball;
import net.minecraft.entity.EntityLivingBase;

public final class SpellFireball extends SpellBase {
	
	public SpellFireball() {
		super("fireball", EnumRace.SALAMANDER, 1000, 50, 5);
	}
	
	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCastOver(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellFireball(caster.worldObj, caster));
		return result;
	}
}