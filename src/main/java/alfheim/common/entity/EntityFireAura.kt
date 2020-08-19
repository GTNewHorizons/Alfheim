package alfheim.common.entity

import alexsocol.asjlib.F
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy
import kotlin.math.min

class EntityFireAura: EntityThrowableCopy {

	constructor(worldObj: World?): super(worldObj) {
		setSize(0f, 0f)
	}
	
	constructor(worldObj: World?, player: EntityPlayer?): super(worldObj, player) {
		setSize(0f, 0f)
	}
	
	override fun entityInit() {
		super.entityInit()
	}
	
	override fun getGravityVelocity() = 0f
	
	override fun onUpdate() {
		super.onUpdate()
		if (ticksExisted > 100)
			setDead()
		
		if (worldObj.isRemote)
			for (i in 0..4)
				Botania.proxy.wispFX(worldObj, posX + Math.random() * 0.4f - 0.2f, posY + Math.random() * 0.4f - 0.2f, posZ + Math.random() * 0.4f - 0.2f, 1f, (0.25f + Math.random() * 0.25).F, 0f, 0.25f)
	}
	
	override fun onImpact(pos: MovingObjectPosition) {
		if (thrower != null && thrower is EntityPlayer) {
			val player = thrower as EntityPlayer
			val attack = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).attributeValue
			
			if (pos.entityHit != null || pos.entityHit !== thrower) {
				if (pos.entityHit is EntityLivingBase) {
					val dmg = (4 + attack).F
					(pos.entityHit as EntityLivingBase).attackEntityFrom(DamageSource.causePlayerDamage(player), dmg)
					player.absorptionAmount = min(10f, player.absorptionAmount + 1f)
					setDead()
				}
			}
		}
	}
}
