package alfheim.common.block.tile

import alexsocol.asjlib.*
import alfheim.common.entity.FakeLightning
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import java.util.*

class TileLightningRod: TileEntity() {
	
	override fun updateEntity() { // FIXME dupes lightnings
		if (worldObj != null) {
			for (e in getBoltsWithinAABB(worldObj, boundingBox(48))) {
				worldObj.removeEntity(e)
				worldObj.addWeatherEffect(FakeLightning(worldObj, xCoord.D, (yCoord + 1).D, zCoord.D))
			}
			
			for (x in (xCoord - 2)..(xCoord + 2))
				for (y in (yCoord - 2)..(yCoord + 2))
					for (z in (zCoord - 2)..(zCoord + 2)) {
						if (worldObj.getBlock(x, y, z) === Blocks.fire) {
							worldObj.setBlockToAir(x, y, z)
						}
					}
		}
	}
	
	fun getBoltsWithinAABB(world: World, box: AxisAlignedBB): ArrayList<EntityLightningBolt> {
		val bolts = ArrayList<EntityLightningBolt>()
		
		for (effect in world.weatherEffects) {
			if (effect is EntityLightningBolt && effect !is FakeLightning) {
				if (effect.posX in box.minX..box.maxX &&
					effect.posY in box.minY..box.maxY &&
					effect.posZ in box.minZ..box.maxZ) {
					
					bolts.add(effect)
				}
			}
		}
		
		return bolts
	}
}