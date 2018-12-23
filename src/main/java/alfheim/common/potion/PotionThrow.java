package alfheim.common.potion;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.OrientedBB;
import alexsocol.asjlib.math.Vector3;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

public class PotionThrow extends PotionAlfheim {

	public PotionThrow() {
		super(AlfheimConfig.potionIDThrow, "throw", false, 0xAAFFFF);
	}

	@Override
	public boolean isReady(int time, int mod) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase target, int mod) {
		Vector3 v = new Vector3(target.getLookVec()).mul(mod+1);
		target.motionX = v.x;
		target.motionY = v.y;
		target.motionZ = v.z;
		
		Party pt = PartySystem.getMobParty(target);
		if (pt == null) pt = new Party();
		
		List<EntityLivingBase> l = target.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, target.boundingBox.copy().expand(1, 1, 1));
		l.remove(target);
		for (EntityLivingBase e : l) if (!pt.isMember(e)) e.attackEntityFrom(DamageSource.causeMobDamage(target), 5);
	}
}