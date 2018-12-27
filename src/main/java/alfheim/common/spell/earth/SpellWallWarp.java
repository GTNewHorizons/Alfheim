package alfheim.common.spell.earth;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class SpellWallWarp extends SpellBase {

	public SpellWallWarp() {
		super("wallwarp", EnumRace.GNOME, 4000, 600, 5);
	}

	@Override
	// This spell is slightly changed version of item from thKaguya's mod 
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result;
		
		double dist = caster instanceof EntityPlayerMP ? ((EntityPlayerMP) caster).theItemInWorldManager.getBlockReachDistance() : 5.0;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, dist, false);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT;
		
		int px = 0;
    	int py = 0;
    	int pz = 0;
    	switch(mop.sideHit) {
    		case 0:
    			py = 1;
    			break;
    		case 1:
    			py = -1;
    			break;
    		case 2:
    			pz = 1;
    			break;
    		case 3:
    			pz = -1;
    			break;
    		case 4:
    			px = 1;
    			break;
    		default:
    			px = -1;
    			break;
    	}
    	
	    for(int i = 0; i < 3; i++) {
	    	if(caster.worldObj.isAirBlock(mop.blockX, mop.blockY, mop.blockZ)) {
	    		if( caster.worldObj.isAirBlock(mop.blockX, mop.blockY + 1, mop.blockZ)) {
	    			result = checkCast(caster);
	    			if (result != SpellCastResult.OK) return result;
	    			
	    			caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5F, 1.0F);
	    			caster.posX = mop.blockX + 0.5;
	    			caster.posY = mop.blockY + caster.yOffset;
	    			caster.posZ = mop.blockZ + 0.5;
	    			caster.motionX = caster.motionY = caster.motionZ = 0;
	    			if (caster instanceof EntityPlayerMP) ((EntityPlayerMP) caster).playerNetServerHandler.setPlayerLocation( mop.blockX + 0.5,  mop.blockY + caster.yOffset,  mop.blockZ + 0.5, caster.rotationYaw, caster.rotationPitch);
	    			else caster.setPosition(mop.blockX + 0.5, mop.blockY + caster.yOffset, mop.blockZ + 0.5);
	    			return result;
	    		} else if( caster.worldObj.isAirBlock(mop.blockX, mop.blockY - 1, mop.blockZ)) {
	    			result = checkCast(caster);
	    			if (result != SpellCastResult.OK) return result;
	    			
	    			caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5F, 1.0F);
	    			caster.posX = mop.blockX + 0.5;
	    			caster.posY = mop.blockY + caster.yOffset - 1.0;
	    			caster.posZ = mop.blockZ + 0.5;
	    			caster.motionX = caster.motionY = caster.motionZ = 0;
	    			if (caster instanceof EntityPlayerMP) ((EntityPlayerMP) caster).playerNetServerHandler.setPlayerLocation(mop.blockX + 0.5, mop.blockY + caster.yOffset - 1, mop.blockZ + 0.5, caster.rotationYaw, caster.rotationPitch);
	    			else caster.setPosition(mop.blockX + 0.5, mop.blockY + caster.yOffset - 1, mop.blockZ + 0.5);
	    			return result;
	    		}
	    	}
	    	
	    	if(caster.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Blocks.bedrock) {
	    		return SpellCastResult.OBSTRUCT;
	    	}
	    	mop.blockX += px;
	    	mop.blockY += py;
	    	mop.blockZ += pz;
	    }
		
		return SpellCastResult.OBSTRUCT;
	}
}