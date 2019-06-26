package alfheim.common.block.tile.sub

import alfheim.api.block.tile.SubTileEntity
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity
import alfheim.common.block.tile.TileAnomaly
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import vazkii.botania.common.Botania

class SubTileSpeedUp: SubTileEntity() {
	
	override val targets: List<Any>
		get() {
			if (inWG()) return SubTileEntity.EMPTY_LIST
			
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
		get() = 4
	
	override val rarity: EnumAnomalityRarity
		get() = EnumAnomalityRarity.EPIC
	
	override fun update() {
		if (inWG()) return
		
		rand.setSeed((x() xor y() xor z()).toLong())
		val worldTime = (worldObj().totalWorldTime + rand.nextInt(1000)) / 10.0
		val r = 0.75f + Math.random().toFloat() * 0.05f
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, y().toDouble() + 0.5 + Math.cos(worldTime) * r, z().toDouble() + 0.5 + Math.sin(worldTime) * r,
							 Math.random().toFloat() * 0.25f, 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj(), x().toDouble() + 0.5 + Math.sin(worldTime) * r, y() + 0.5, z().toDouble() + 0.5 + Math.cos(worldTime) * r,
							 Math.random().toFloat() * 0.25f, 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
		
		Botania.proxy.wispFX(worldObj(), x().toDouble() + 0.5 + Math.cos(worldTime) * r, y().toDouble() + 0.5 + Math.sin(worldTime) * r, z() + 0.5,
							 Math.random().toFloat() * 0.25f, 0.75f + Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.25f,
							 0.1f + Math.random().toFloat() * 0.1f)
	}
	
	override fun performEffect(target: Any) {
		if (target is Entity) target.onUpdate()
		if (target is TileEntity) target.updateEntity()
	}
	
	override fun typeBits(): Int {
		return SubTileEntity.TIME
	}
	
	companion object {
		
		val radius = 8
	}
}
