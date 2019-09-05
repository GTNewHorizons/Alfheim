package alfheim.common.block.tile.sub.anomaly

import alfheim.api.block.tile.SubTileEntity
import net.minecraft.entity.Entity
import vazkii.botania.common.Botania
import kotlin.math.*

class SubTileSpeedDown: SubTileEntity() {
	
	override val targets: List<Any>
		get() {
			if (inWG() || worldObj.totalWorldTime % 2 == 0L) return EMPTY_LIST
			
			return allAroundRaw(Entity::class.java, 8.0)
		}
	
	override val strip: Int
		get() = 5
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.EPIC
	
	override fun update() {
		if (inWG()) return
		
		rand.setSeed((x() xor y() xor z()).toLong())
		val worldTime = (worldObj.totalWorldTime + rand.nextInt(1000)) / 10.0
		val r = 0.75f + Math.random().toFloat() * 0.05f
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, y().toDouble() + 0.5 + cos(worldTime) * r, z().toDouble() + 0.5 + sin(worldTime) * r,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x().toDouble() + 0.5 + sin(worldTime) * r, y() + 0.5, z().toDouble() + 0.5 + cos(worldTime) * r,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x().toDouble() + 0.5 + cos(worldTime) * r, y().toDouble() + 0.5 + sin(worldTime) * r, z() + 0.5,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
	}
	
	override fun performEffect(target: Any) {
		if (target is Entity) target.canEntityUpdate = false
	}
	
	override fun typeBits() = TIME
	
	companion object {
		const val radius = 8
	}
}
