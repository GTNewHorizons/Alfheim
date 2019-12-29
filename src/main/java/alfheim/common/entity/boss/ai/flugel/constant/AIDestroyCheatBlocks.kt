package alfheim.common.entity.boss.ai.flugel.constant

import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.AIConstantExecutable
import net.minecraft.block.Block
import net.minecraft.entity.item.EntityItem
import vazkii.botania.common.core.handler.ConfigHandler

class AIDestroyCheatBlocks(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		if (!flugel.worldObj.isRemote) {
			val src = flugel.source
			for (i in -EntityFlugel.RANGE..EntityFlugel.RANGE)
				for (j in -EntityFlugel.RANGE..EntityFlugel.RANGE)
					for (k in -EntityFlugel.RANGE..EntityFlugel.RANGE) {
						val xp = src.posX + i
						val yp = src.posY + j
						val zp = src.posZ + k
						if (Vector3.pointDistanceSpace(src.posX, src.posY, src.posZ, xp, yp, zp) <= EntityFlugel.RANGE && EntityFlugel.isCheatyBlock(flugel.worldObj, xp, yp, zp)) {
							val block = flugel.worldObj.getBlock(xp, yp, zp)
							val items = block.getDrops(flugel.worldObj, xp, yp, zp, 0, 0)
							for (stack in items) {
								if (ConfigHandler.blockBreakParticles) flugel.worldObj.playAuxSFX(2001, xp, yp, zp, Block.getIdFromBlock(block) + (flugel.worldObj.getBlockMetadata(xp, yp, zp) shl 12))
								flugel.worldObj.spawnEntityInWorld(EntityItem(flugel.worldObj, xp + 0.5, yp + 0.5, zp + 0.5, stack))
							}
							flugel.worldObj.setBlockToAir(xp, yp, zp)
						}
					}
		}
	}
}