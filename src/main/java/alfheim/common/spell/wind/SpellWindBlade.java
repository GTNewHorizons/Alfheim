package alfheim.common.spell.wind;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.entity.spell.EntitySpellFireball;
import alfheim.common.entity.spell.EntitySpellWindBlade;
import net.minecraft.entity.EntityLivingBase;

public class SpellWindBlade extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellWindBlade(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SYLPH;
	}

	@Override
	public String getName() {
		return "windbalde";
	}

	@Override
	public int getManaCost() {
		return 8000;
	}

	@Override
	public int getCooldown() {
		return 120;
	}

	@Override
	public int castTime() {
		return 10;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}