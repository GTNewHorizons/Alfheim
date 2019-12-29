package alfheim.common.entity.boss.ai.flugel.constant

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.common.core.util.*
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.AIConstantExecutable
import baubles.common.lib.PlayerHandler
import net.minecraft.util.ChunkCoordinates
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems

class AIRequireWings(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		val source = flugel.source
		
		flugel.playersAround.forEach { player ->
			val baubles = PlayerHandler.getPlayerBaubles(player)
			val tiara = baubles.getStackInSlot(0)
			if (tiara != null && tiara.item == ModItems.flightTiara && tiara.itemDamage == 1)
				ItemNBTHelper.setInt(tiara, EntityFlugel.TAG_TIME_LEFT, 1200)
			else {
				if (!flugel.worldObj.isRemote) {
					ASJUtilities.say(player, "alfheimmisc.notallowed")
					
					fun isTooNear(bed: ChunkCoordinates?) =
						if (bed == null) true
						else Vector3.pointDistanceSpace(bed.posX, bed.posY, bed.posZ, source.posX, source.posY, source.posZ) <= EntityFlugel.RANGE + 3
					
					if (isTooNear(player.getBedLocation(player.dimension))) {
						if (isTooNear(player.worldObj.spawnPoint)) {
							val v = Vector3(Math.random() * 100 + EntityFlugel.RANGE, 0.0, 0.0).rotate(Math.random() * 360, Vector3.oY)
							val newPosY = ASJUtilities.getTopLevel(flugel.worldObj, v.x.mfloor(), v.z.mfloor())
							player.setPositionAndUpdate(v.x, newPosY.D, v.z)
						} else {
							val bed = player.worldObj.spawnPoint
							player.setPositionAndUpdate(bed.posX.D, bed.posY.D, bed.posZ.D)
						}
					} else {
						val bed = player.getBedLocation(player.dimension)
						player.setPositionAndUpdate(bed.posX.D, bed.posY.D, bed.posZ.D)
					}
				}
			}
		}
	}
}