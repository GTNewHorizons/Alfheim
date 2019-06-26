package alfheim.common.block.tile.sub

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.block.tile.SubTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.botania.common.Botania

class SubTileGravity: SubTileEntity() {
	var power = 0.65
	internal val vt = Vector3()
	internal val ve = Vector3()
	
	override val targets: List<Any>
		get() {
			if (inWG()) return SubTileEntity.EMPTY_LIST
			val radius = power * 10
			val dist = 0.0
			val str = 0.0
			return allAroundRaw(Entity::class.java, radius)
		}
	
	override val strip: Int
		get() = 0
	
	override val rarity: SubTileEntity.EnumAnomalityRarity
		get() = SubTileEntity.EnumAnomalityRarity.COMMON
	
	public override fun update() {
		if (inWG()) return
		
		if (ASJUtilities.isServer && ticks % 100 == 0) {
			power = Math.random() * 0.65 + 0.35
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(superTile!!)
		}
		
		val radius = power * 10
		val dist = 0.0
		val str = 0.0
		
		vt.rand().sub(0.5).normalize().mul(Math.random() * radius / 2).add(superTile!!).add(0.5)
		ve.set(superTile!!).add(0.5).sub(vt).mul(0.05)
		
		Botania.proxy.wispFX(worldObj(), vt.x, vt.y, vt.z, 1f, 1f, 1f, 0.5f, ve.x.toFloat(), ve.y.toFloat(), ve.z.toFloat(), 0.5f)
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
		val dist: Double
		val str = 0.0
		
		ve.set(target)
		
		if (!ASJUtilities.isServer) if (target === Minecraft.getMinecraft().thePlayer) ve.add(0.0, -1.62, 0.0)
		
		if ((dist = Math.sqrt(Math.pow(ve.x - x() + 0.5, 2.0) + Math.pow(ve.y - y() + 0.5, 2.0) + Math.pow(ve.z - z() + 0.5, 2.0))) > radius) return
		
		vt.set(superTile!!).add(0.5)
		vt.set(vt).sub(ve).normalize().mul(power * 0.5 * 1.0 / dist)
		
		target.motionX += vt.x
		target.motionY += vt.y * 1.25
		target.motionZ += vt.z
	}
	
	override fun typeBits(): Int {
		return SubTileEntity.MOTION
	}
	
	companion object {
		
		val TAG_POWER = "power"
	}
}