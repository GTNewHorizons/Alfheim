package alfheim.common.spell.tech;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.entity.spell.EntitySpellDriftingMine;
import alfheim.common.entity.spell.EntitySpellFireball;
import net.minecraft.entity.EntityLivingBase;

public class SpellDriftingMine extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellDriftingMine(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.LEPRECHAUN;
	}

	@Override
	public String getName() {
		return "driftingmine";
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
		return 15;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}