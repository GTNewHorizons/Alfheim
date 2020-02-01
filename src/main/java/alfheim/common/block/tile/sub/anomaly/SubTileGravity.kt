package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.block.tile.SubTileEntity
import alfheim.client.core.util.mc
import alfheim.common.core.util.F
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.common.Botania
import kotlin.math.*

class SubTileGravity: SubTileEntity() {
	
	var power = 0.65
	internal val vt = Vector3()
	internal val ve = Vector3()
	
	override val targets: List<Any>
		get() {
			if (inWG()) return EMPTY_LIST
			val radius = power * 10
			return allAroundRaw(Entity::class.java, radius)
		}
	
	override val strip: Int
		get() = 0
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.COMMON
	
	public override fun update() {
		if (inWG()) return
		
		if (ASJUtilities.isServer && ticks % 100 == 0) {
			power = Math.random() * 0.65 + 0.35
			ASJUtilities.dispatchTEToNearbyPlayers(superTile!!)
		}
		
		val radius = power * 10
		
		vt.rand().sub(0.5).normalize().mul(Math.random() * radius / 2).add(superTile!!).add(0.5)
		ve.set(superTile!!).add(0.5).sub(vt).mul(0.05)
		
		Botania.proxy.wispFX(worldObj, vt.x, vt.y, vt.z, 1f, 1f, 1f, 0.5f, ve.x.F, ve.y.F, ve.z.F, 0.5f)
	}
	
	override fun writeCustomNBT(cmp: NBTTagCompound) {
		super.writeCustomNBT(cmp)
		cmp.setDouble(TAG_POWER, power)
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound) {
		super.readCustomNBT(cmp)
		power = cmp.getDouble(TAG_POWER)
	}
	
	override fun performEffect(target: Any) {
		if (target !is Entity) return
		if (target is EntityPlayer && target.capabilities.disableDamage) return
		
		val radius = power * 10
		
		ve.set(target)
		if (!ASJUtilities.isServer) if (target === mc.thePlayer) ve.add(0.0, -1.62, 0.0)
		
		val dist = sqrt((ve.x - x() + 0.5).pow(2.0) + (ve.y - y() + 0.5).pow(2.0) + (ve.z - z() + 0.5).pow(2.0))
		if (dist > radius) return
		
		vt.set(superTile!!).add(0.5)
		vt.set(vt).sub(ve).normalize().mul(power * 0.5 * 1.0 / dist)
		
		target.motionX += vt.x
		target.motionY += vt.y * 1.25
		target.motionZ += vt.z
	}
	
	override fun typeBits() = MOTION
	
	companion object {
		const val TAG_POWER = "power"
	}
}