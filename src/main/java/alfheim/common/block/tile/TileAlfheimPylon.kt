package alfheim.common.block.tile

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.registry.AlfheimBlocks
import net.minecraft.tileentity.TileEntity
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler

class TileAlfheimPylon: TileEntity() {
	
	internal var activated = false
	internal var centerX: Int = 0
	internal var centerY: Int = 0
	internal var centerZ: Int = 0
	internal var ticks = 0
	
	override fun updateEntity() {
		++ticks
		val meta = getBlockMetadata()
		
		if (activated && worldObj.isRemote) {
			if (worldObj.getBlock(centerX, centerY, centerZ) !== AlfheimBlocks.tradePortal || worldObj.getBlockMetadata(centerX, centerY, centerZ) == 0) {
				activated = false
				return
			}
			
			val centerBlock = Vector3(centerX + 0.5, centerY.toDouble() + 0.75 + (Math.random() - 0.5 * 0.25), centerZ + 0.5)
			
			if (ConfigHandler.elfPortalParticlesEnabled) {
				var worldTime = ticks.toDouble()
				worldTime += Math.random() * 1000
				worldTime /= 5.0
				
				val r = 0.75f + Math.random().toFloat() * 0.05f
				val x = xCoord.toDouble() + 0.5 + Math.cos(worldTime) * r
				val z = zCoord.toDouble() + 0.5 + Math.sin(worldTime) * r
				
				centerBlock.sub(0.0, 0.5, 0.0).sub(x, yCoord + 0.75, z).normalize().mul(0.2)
				
				Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75f + Math.random().toFloat() * 0.25f, 0.5f + Math.random().toFloat() * 0.25f, (if (meta == 0) 0.75f else 0) + Math.random().toFloat() * 0.25f, 0.25f + Math.random().toFloat() * 0.1f, -0.075f - Math.random().toFloat() * 0.015f)
				if (worldObj.rand.nextInt(3) == 0)
					Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75f + Math.random().toFloat() * 0.25f, 0.5f + Math.random().toFloat() * 0.25f, (if (meta == 0) 0.75f else 0) + Math.random().toFloat() * 0.25f, 0.25f + Math.random().toFloat() * 0.1f, centerBlock.x.toFloat(), centerBlock.y.toFloat(), centerBlock.z.toFloat())
			}
		}
		
		if (worldObj.rand.nextBoolean() && worldObj.isRemote)
			Botania.proxy.sparkleFX(worldObj, xCoord + Math.random(), yCoord + Math.random() * 1.5, zCoord + Math.random(), 1f, if (meta != 2) 0.5f else 0, (if (meta == 0) 1 else 0).toFloat(), Math.random().toFloat(), 2)
	}
}
