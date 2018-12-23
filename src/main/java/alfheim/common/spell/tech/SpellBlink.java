package alfheim.common.spell.tech;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import codechicken.lib.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import vazkii.botania.common.Botania;

public class SpellBlink extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result;
		Vector3 hit;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 8, true);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) hit = new Vector3(caster.getLookVec()).normalize().mul(8).add(caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ);
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

	   	if(caster.worldObj.isAirBlock(x, y, z)) {
	   		if(caster.worldObj.isAirBlock(x, y + 1, z)) {
	   			result = checkCast(caster);
	   			if (result != SpellCastResult.OK) return result;
	   			
	   			caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5F, 1.0F);
	   			caster.posX = x + 0.5;
	   			caster.posY = y + caster.yOffset;
	   			caster.posZ = z + 0.5;
	   			caster.motionX = caster.motionY = caster.motionZ = 0;
	   			if (caster instanceof EntityPlayerMP) ((EntityPlayerMP) caster).playerNetServerHandler.setPlayerLocation(x + 0.5, y + caster.yOffset, z + 0.5, caster.rotationYaw, caster.rotationPitch);
	   			else caster.setPosition(x + 0.5, y + caster.yOffset, z + 0.5);
	   			return result;
	   		} else if( caster.worldObj.isAirBlock(x, y - 1, z)) {
	   			result = checkCast(caster);
	   			if (result != SpellCastResult.OK) return result;
	   			
	   			caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5F, 1.0F);
	   			caster.posX = x + 0.5;
	   			caster.posY = y + caster.yOffset - 1.0;
	   			caster.posZ = z + 0.5;
	   			caster.motionX = caster.motionY = caster.motionZ = 0;
	   			if (caster instanceof EntityPlayerMP) ((EntityPlayerMP) caster).playerNetServerHandler.setPlayerLocation(x + 0.5, y + caster.yOffset - 1, z + 0.5, caster.rotationYaw, caster.rotationPitch);
	   			else caster.setPosition(x + 0.5, y + caster.yOffset - 1, z + 0.5);
	   			return result;
	   		}
	   	}
    	
		return SpellCastResult.OBSTRUCT;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.LEPRECHAUN;
	}

	@Override
	public String getName() {
		return "blink";
	}

	@Override
	public int getManaCost() {
		return 10000;
	}

	@Override
	public int getCooldown() {
		return 1200;
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
	public void render(EntityLivingBase caster) {
		Vector3 hit;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, 8, true);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) hit = new Vector3(caster.getLookVec()).normalize().mul(8).add(caster.posX, caster.posY, caster.posZ);
		else hit = new Vector3(mop.hitVec);
		
		double x = hit.x;
		double y = hit.y;
		double z = hit.z;
    	
    	/*if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit != -1) {
	    	switch(mop.sideHit) {
	    		case 0: --y; break;
	    		case 1: ++y; break;
	    		case 2: --z; break;
	    		case 3: ++z; break;
	    		case 4: --x; break;
	    		case 5: ++x; break;
	    	}
    	}*/
    	
    	Botania.proxy.wispFX(caster.worldObj, x, y, z, 0.5F, 0, 1, 0.15F, 0, 0.075F);
	}
}