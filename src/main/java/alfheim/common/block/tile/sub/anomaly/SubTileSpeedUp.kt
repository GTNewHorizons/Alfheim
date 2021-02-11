package alfheim.common.block.tile.sub.anomaly

import alexsocol.asjlib.*
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.common.block.tile.TileAnomaly
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import vazkii.botania.common.Botania
import kotlin.math.*

class SubTileSpeedUp: SubTileAnomalyBase() {
	
	override val targets: List<Any>
		get() {
			if (inWG()) return EMPTY_LIST
			
			val l = allAroundRaw(Entity::class.java, 8.0)
			l.removeAll { !(it as Entity).isEntityAlive }
			
			for (x in -radius..radius)
				for (z in -radius..radius)
					for (y in -radius..radius) {
						if (x == 0 && y == 0 && z == 0) continue
						val t = worldObj.getTileEntity(x(x.D), y(y.D), z(z.D))
						if (t != null && t.canUpdate() && !t.isInvalid && t !is TileAnomaly) l.add(t)
					}
			
			return l
		}
	
	override val strip: Int
		get() = 4
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.EPIC
	
	override fun update() {
		if (inWG()) return
		
		rand.setSeed((x() xor y() xor z()).toLong())
		val worldTime = (worldObj.totalWorldTime + rand.nextInt(1000)) / 10.0
		val r = 0.75f + Math.random().F * 0.05f
		
		Botania.proxy.wispFX(worldObj, x() + 0.5, y().D + 0.5 + cos(worldTime) * r, z().D + 0.5 + sin(worldTime) * r,
							 Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f, Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x().D + 0.5 + sin(worldTime) * r, y() + 0.5, z().D + 0.5 + cos(worldTime) * r,
							 Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f, Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
		
		Botania.proxy.wispFX(worldObj, x().D + 0.5 + cos(worldTime) * r, y().D + 0.5 + sin(worldTime) * r, z() + 0.5,
							 Math.random().F * 0.25f, 0.75f + Math.random().F * 0.25f, Math.random().F * 0.25f,
							 0.1f + Math.random().F * 0.1f)
	}
	
	override fun performEffect(target: Any) {
		if (target is Entity) target.onUpdate()
		if (target is TileEntity) target.updateEntity()
	}
	
	override fun typeBits() = TIME
	
	companion object {
		
		const val radius = 8
	}
}
