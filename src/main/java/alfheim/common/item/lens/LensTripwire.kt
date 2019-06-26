package alfheim.common.item.lens

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.util.AxisAlignedBB
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.IManaSpreader
import vazkii.botania.common.block.tile.mana.TileSpreader
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.lens.Lens

class LensTripwire: Lens() {
	
	override fun allowBurstShooting(stack: ItemStack?, spreader: IManaSpreader?, redstone: Boolean): Boolean {
		if (spreader is TileSpreader) {
			val burst = runBurstSimulation((spreader as TileSpreader?)!!)
			val e = burst as Entity
			return e.entityData.getBoolean(TAG_TRIPPED)
		}
		return false
	}
	
	override fun updateBurst(burst: IManaBurst, entity: EntityThrowable?, stack: ItemStack?) {
		if (burst.isFake) {
			if (entity!!.worldObj.isRemote) return
			
			val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(0.25, 0.25, 0.25)
			val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis)
			if (!entities.isEmpty()) {
				val e = burst as Entity
				e.entityData.setBoolean(TAG_TRIPPED, true)
			}
		}
	}
	
	fun runBurstSimulation(spreader: TileSpreader): IManaBurst {
		val fakeBurst = spreader.getBurst(true)
		fakeBurst.setScanBeam()
		fakeBurst.getCollidedTile(true)
		return fakeBurst
	}
	
	companion object {
		
		private val TAG_TRIPPED = "botania:triwireLensTripped"
	}
}