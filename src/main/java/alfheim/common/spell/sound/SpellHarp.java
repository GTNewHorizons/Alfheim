package alfheim.common.spell.sound;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.entity.spell.EntitySpellHarp;
import codechicken.lib.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class SpellHarp extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result;
		Vector3 hit;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 32, true);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) hit = new Vector3(caster.getLookVec()).normalize().mul(32).add(caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ);
		else hit = new Vector3(mop.blockX, mop.blockY, mop.blockZ);
		
		double x = MathHelper.floor_double(hit.x) + 0.5;
		double y = MathHelper.floor_double(hit.y) + 0.5;
		double z = MathHelper.floor_double(hit.z) + 0.5;
		
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
    	
    	result = checkCast(caster);
   		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(new EntitySpellHarp(caster.worldObj, caster, x, y, z));
   		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.POOKA;
	}

	@Override
	public String getName() {
		return "harp";
	}

	@Override
	public int getManaCost() {
		return 1500;
	}

	@Override
	public int getCooldown() {
		return 3600;
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