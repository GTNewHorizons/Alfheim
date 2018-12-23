package alfheim.common.spell.fire;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellFireball;
import net.minecraft.entity.EntityLivingBase;

public final class SpellFireball extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellFireball(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SALAMANDER;
	}

	@Override
	public String getName() {
		return "fireball";
	}

	@Override
	public int getManaCost() {
		return 1000;
	}

	@Override
	public int getCooldown() {
		return 50;
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