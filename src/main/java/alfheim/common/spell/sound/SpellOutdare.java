package alfheim.common.spell.sound;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class SpellOutdare extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		List<EntityLiving> l = caster.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(32, 32, 32));
		l.remove(caster);
		if (l.isEmpty()) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		for (EntityLiving e : l) 
			if (Vector3.entityDistance(caster, e) < 32) {
				e.setAttackTarget(caster);
				e.setLastAttacker(caster);
				e.setRevengeTarget(caster);
			}
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.POOKA;
	}

	@Override
	public String getName() {
		return "outdare";
	}

	@Override
	public int getManaCost() {
		return 6000;
	}

	@Override
	public int getCooldown() {
		return 2400;
	}

	@Override
	public int castTime() {
		return 20;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}