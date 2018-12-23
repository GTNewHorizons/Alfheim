package alfheim.common.spell.wind;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellFenrirStorm;
import net.minecraft.entity.EntityLivingBase;

public class SpellFenrirStorm extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellFenrirStorm(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SYLPH;
	}

	@Override
	public String getName() {
		return "fenrirstorm";
	}

	@Override
	public int getManaCost() {
		return 1000;
	}

	@Override
	public int getCooldown() {
		return 100;
	}

	@Override
	public int castTime() {
		return 5;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}