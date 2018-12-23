package alfheim.common.spell.earth;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.entity.spell.EntitySpellFireball;
import alfheim.common.entity.spell.EntitySpellMortar;
import net.minecraft.entity.EntityLivingBase;

public class SpellMortar extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellMortar(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.GNOME;
	}

	@Override
	public String getName() {
		return "mortar";
	}

	@Override
	public int getManaCost() {
		return 7500;
	}

	@Override
	public int getCooldown() {
		return 200;
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