package alfheim.common.block.tile.sub

import alfheim.api.block.tile.SubTileEntity
import alfheim.common.block.tile.TileAnomaly
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import vazkii.botania.common.Botania
import kotlin.math.*

class SubTileSpeedDown: SubTileEntity() {
	
	override val targets: List<Any>
		get() {
			if (inWG() || worldObj().totalWorldTime % 2 == 0L) return EMPTY_LIST
			val l = allAroundRaw(Entity::class.java, 8.0)
			for (x in -radius..radius)
				for (z in -radius..radius)
					for (y in -radius..radius) {
						if (x == 0 && y == 0 && z == 0) continue
						val t = worldObj().getTileEntity(x(x.toDouble()), y(y.toDouble()), z(z.toDouble()))
						if (t != null && t.canUpdate() && !t.isInvalid && t !is TileAnomaly) l.add(t)
					}
			
			return l
		}
	
	override val strip: Int
		get() = 5
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.EPIC
	
	override fun update() {
		if (inWG()) return
		
		rand.setSeed((x() xor y() xor z()).toLong())
		val worldTime = (worldObj().totalWorldTime + rand.nextInt(1000)) / 10.0
		val r = 0.75f + Math.random().toFloat() * 0.05f
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, y().toDouble() + 0.5 + cos(worldTime) * r, z().toDouble() + 0.5 + sin(worldTime) * r,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj(), x().toDouble() + 0.5 + sin(worldTime) * r, y() + 0.5, z().toDouble() + 0.5 + cos(worldTime) * r,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj(), x().toDouble() + 0.5 + cos(worldTime) * r, y().toDouble() + 0.5 + sin(worldTime) * r, z() + 0.5,
							 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
	}
	
	override fun performEffect(target: Any) {
		if (target is Entity) target.canEntityUpdate = false
		if (target is TileEntity) target.canTileUpdate = false
	}
	
	override fun typeBits(): Int {
		return TIME
	}
	
	companion object {
		const val radius = 8
	}
}
