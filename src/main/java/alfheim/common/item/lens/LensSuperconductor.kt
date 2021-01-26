package alfheim.common.item.lens

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.equipment.armor.elvoruim.ItemElvoriumArmor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.lens.Lens

class LensSuperconductor: Lens() {
	
	override fun apply(stack: ItemStack, props: BurstProperties) {
		props.maxMana *= 8
		props.motionModifier *= 1.5f
		props.manaLossPerTick *= 16f
		props.ticksBeforeManaLoss = (props.ticksBeforeManaLoss * 0.8).I
	}
	
	override fun updateBurst(burst: IManaBurst, entity: EntityThrowable, stack: ItemStack) {
		if (entity.worldObj.isRemote || burst.isFake) return
		
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.5)
		val list = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as MutableList<EntityLivingBase>
		
		for (e in list)
			e.attackEntityFrom(SUPERCONDUCTOR((burst as EntityManaBurst).thrower), if (e is EntityPlayer) if (ItemElvoriumArmor.hasSet(e) || !AlfheimConfigHandler.uberBlaster) 12f.also { if (AlfheimConfigHandler.uberBlaster) e.inventory.damageArmor(13f) } else 25f else 8f)
	}
	
	fun SUPERCONDUCTOR(e: EntityLivingBase) = EntityDamageSource("magic", e).setDamageBypassesArmor().setMagicDamage().setDamageIsAbsolute().setDifficultyScaled()
}
