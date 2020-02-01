package alfheim.common.block.tile

import alexsocol.asjlib.math.Vector3
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.*
import net.minecraft.tileentity.TileEntity
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import kotlin.math.*

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
			
			val centerBlock = Vector3(centerX + 0.5, centerY.D + 0.75 + (Math.random() - 0.5 * 0.25), centerZ + 0.5)
			
			if (ConfigHandler.elfPortalParticlesEnabled) {
				var worldTime = ticks.D
				worldTime += Math.random() * 1000
				worldTime /= 5.0
				
				val r = 0.75f + Math.random().F * 0.05f
				val x = xCoord.D + 0.5 + cos(worldTime) * r
				val z = zCoord.D + 0.5 + sin(worldTime) * r
				
				centerBlock.sub(0.0, 0.5, 0.0).sub(x, yCoord + 0.75, z).normalize().mul(0.2)
				
				Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75f + Math.random().F * 0.25f, 0.5f + Math.random().F * 0.25f, (if (meta == 0) 0.75f else 0f) + Math.random().F * 0.25f, 0.25f + Math.random().F * 0.1f, -0.075f - Math.random().F * 0.015f)
				if (worldObj.rand.nextInt(3) == 0)
					Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75f + Math.random().F * 0.25f, 0.5f + Math.random().F * 0.25f, (if (meta == 0) 0.75f else 0f) + Math.random().F * 0.25f, 0.25f + Math.random().F * 0.1f, centerBlock.x.F, centerBlock.y.F, centerBlock.z.F)
			}
		}
		
		if (worldObj.rand.nextBoolean() && worldObj.isRemote)
			Botania.proxy.sparkleFX(worldObj, xCoord + Math.random(), yCoord + Math.random() * 1.5, zCoord + Math.random(), 1f, if (meta != 2) 0.5f else 0f, (if (meta == 0) 1 else 0).F, Math.random().F, 2)
	}
}
