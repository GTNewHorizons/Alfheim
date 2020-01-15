package alfmod.common.entity

import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.*

class EntitySnowSprite(world: World): EntityFlyingCreature(world) {

	init {
		setSize(0.25f, 0.25f)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 8.0
	}
	
	override fun updateEntityActionState() {
		super.updateEntityActionState()
		rotationYaw = (-Math.atan2(motionX, motionZ) * 180 / Math.PI).toFloat()
		renderYawOffset = rotationYaw
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5 + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, (Math.random() * 0.1 + 0.9).toFloat(), 1f, 1f, 1f + Math.random().toFloat() * 0.25f, 5)
	}
}