package alfheim.common.entity

import net.minecraft.util.DamageSource
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy

class EntityCharge: EntityThrowableCopy {
	
	constructor(world: World): super(world) {
		setSize(0f, 0f)
	}
	
	constructor(world: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double): super(world) {
		setSize(0f, 0f)
		setPosition(x, y, z)
		setThrowableHeading(mX, mY, mZ, 2f, 0f)
		noClip = true
	}
	
	override fun onEntityUpdate() {
		super.onEntityUpdate()
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.6f)
	}
	
	public override fun onImpact(pos: MovingObjectPosition?) {
		if (pos != null && pos.typeOfHit == MovingObjectType.ENTITY) {
			pos.entityHit.attackEntityFrom(DamageSource.magic, 5.0f)
		}
	}
}