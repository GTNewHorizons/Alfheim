package alfheim.common.spell.sound;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class SpellOutdare extends SpellBase {

	public SpellOutdare() {
		super("outdare", EnumRace.POOKA, 6000, 2400, 20);
	}

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
}