package alfheim.common.spell.wind;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import codechicken.lib.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class SpellThor extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result;
		Vector3 hit;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 32, true);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) hit = new Vector3(caster.getLookVec()).normalize().mul(32).add(caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ);
		else hit = new Vector3(mop.blockX, mop.blockY, mop.blockZ);
		
		int x = MathHelper.floor_double(hit.x);
    	int y = MathHelper.floor_double(hit.y);
    	int z = MathHelper.floor_double(hit.z);
    	
    	if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit != -1) {
	    	switch(mop.sideHit) {
	    		case 0: --y; break;
	    		case 1: ++y; break;
	    		case 2: --z; break;
	    		case 3: ++z; break;
	    		case 4: --x; break;
	    		case 5: ++x; break;
	    	}
    	}
    	
    	if (caster.worldObj.canBlockSeeTheSky(x, y, z) && caster.worldObj.getPrecipitationHeight(x, z) <= y) {
    		result = checkCast(caster);
   			if (result != SpellCastResult.OK) return result;
   			caster.worldObj.addWeatherEffect(new EntityLightningBolt(caster.worldObj, x, y, z));
   			return result;
    	}
    	
    	return SpellCastResult.WRONGTGT;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.SYLPH;
	}

	@Override
	public String getName() {
		return "thor";
	}

	@Override
	public int getManaCost() {
		return 60000;
	}

	@Override
	public int getCooldown() {
		return 12000;
	}

	@Override
	public int castTime() {
		return 30;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}