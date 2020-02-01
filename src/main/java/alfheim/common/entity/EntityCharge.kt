package alfheim.common.entity

import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.util.*
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy

class EntityCharge: EntityThrowableCopy {
	
	val flugel: EntityFlugel?
	
	constructor(world: World): super(world) {
		setSize(0f, 0f)
		flugel = null
	}
	
	constructor(flu: EntityFlugel, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double): super(flu.worldObj) {
		setSize(0f, 0f)
		setPosition(x, y, z)
		setThrowableHeading(mX, mY, mZ, 2f, 0f)
		noClip = true
		flugel = flu
	}
	
	override fun onEntityUpdate() {
		super.onEntityUpdate()
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.6f)
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.5f)
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.4f)
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.3f)
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.2f)
		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.2f, 0f, 0.8f, 0.1f)
	}
	
	public override fun onImpact(pos: MovingObjectPosition?) {
		if (pos != null && pos.typeOfHit == MovingObjectType.ENTITY) {
			if (flugel != null)
				pos.entityHit.attackEntityFrom(DamageSourceSpell.shadow(flugel), if (flugel.isUltraMode) 10f else 5f)
			else
				pos.entityHit.attackEntityFrom(DamageSource.magic, 5f)
		}
	}
}