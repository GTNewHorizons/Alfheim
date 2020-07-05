package alfheim.common.item.lens

import alexsocol.asjlib.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.common.item.lens.Lens

class LensSuperconductor: Lens() {
	
	override fun apply(stack: ItemStack, props: BurstProperties) {
		props.maxMana *= 8
		props.motionModifier *= 1.5F
		props.manaLossPerTick *= 16F
		props.ticksBeforeManaLoss = (props.ticksBeforeManaLoss * 0.8).I
	}
	
	override fun updateBurst(burst: IManaBurst, entity: EntityThrowable, stack: ItemStack) {
		if (entity.worldObj.isRemote || burst.isFake) return
		
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.5)
		val list = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as MutableList<EntityLivingBase>
		
		for (e in list)
			e.attackEntityFrom(SUPERCONDUCTOR, if (e is EntityPlayer) 25F else 8F)
	}
	
	val SUPERCONDUCTOR = DamageSource("magic").setDamageBypassesArmor().setMagicDamage().setDamageIsAbsolute().setDifficultyScaled()
}
