package alfheim.common.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityThrowableCopy;

public class EntityCharge extends EntityThrowableCopy {

	public EntityCharge(World world) {
		super(world);
		setSize(0, 0);
	}

	public EntityCharge(World world, double x, double y, double z, double mX, double mY, double mZ) {
		super(world);
		setSize(0, 0);
		setPosition(x, y, z);
		setThrowableHeading(mX, mY, mZ, 2, 0);
		noClip = true;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2F, 0, 0.8F, 0.6F);
	}
	
	@Override
	public void onImpact(MovingObjectPosition pos) {
		if (pos != null && pos.typeOfHit == MovingObjectType.ENTITY) {
			pos.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
		}
	}
}