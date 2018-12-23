package alfheim.common.spell.water;

import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.common.entity.spell.EntitySpellAquaStream;
import net.minecraft.entity.EntityLivingBase;

public class SpellAquaStream extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellAquaStream(caster.worldObj, caster));
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.UNDINE;
	}

	@Override
	public String getName() {
		return "aquastream";
	}

	@Override
	public int getManaCost() {
		return 2000;
	}

	@Override
	public int getCooldown() {
		return 100;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public int castTime() {
		return 5;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}