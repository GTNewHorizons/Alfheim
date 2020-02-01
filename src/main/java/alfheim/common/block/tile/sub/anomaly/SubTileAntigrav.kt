package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.math.Vector3
import alfheim.api.block.tile.SubTileEntity
import alfheim.common.core.util.D
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import vazkii.botania.common.Botania

class SubTileAntigrav: SubTileEntity() {
	internal val v = Vector3()
	
	override val targets: List<Any>
		get() = if (inWG()) EMPTY_LIST else worldObj.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB.getBoundingBox(x().D, y().D, z().D, x(1.0).D, y(1.0).D, z(1.0).D).expand(radius, radius * 2, radius)) as List<Any>
	
	override val strip: Int
		get() = 7
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.COMMON
	
	public override fun update() {
		if (inWG()) return
		
		for (i in 0..3) {
			v.rand().sub(0.5).set(v.x, 0.0, v.z).normalize().mul(Math.random() * radius).add(superTile!!).add(0.0, Math.random() * radius * 4.0 - radius * 2, 0.0)
			Botania.proxy.wispFX(worldObj, v.x, v.y, v.z, 0.5f, 0.9f, 1f, 0.1f, -0.1f, 10f)
		}
	}
	
	override fun performEffect(target: Any) {
		if (target !is Entity) return
		if (target is EntityPlayer && target.capabilities.disableDamage) return
		
		if (Vector3.pointDistancePlane(x() + 0.5, z() + 0.5, target.posX, target.posZ) > radius) return
		
		target.motionY += power * 0.125
	}
	
	override fun typeBits() = MOTION
	
	companion object {
		const val power = 0.7
		const val radius = 15.0
	}
}