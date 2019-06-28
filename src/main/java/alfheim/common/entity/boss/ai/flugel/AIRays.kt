package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.ModItems
import java.awt.Color

class AIRays(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		flugel.aiTaskTimer = 20
		val player = ASJUtilities.getClosestVulnerablePlayerToEntity(flugel, EntityFlugel.RANGE * 2.0)
		if (player != null) flugel.setPosition(flugel.posX, player.posY, flugel.posZ)
		val more = if (flugel.isHardMode) 10 else 15
		var i = 0
		while (i < 360) {
			flugel.worldObj.spawnEntityInWorld(getBurst(flugel, i))
			i += more
		}
		flugel.worldObj.playSoundAtEntity(flugel, "botania:terraBlade", 0.4f, 1.4f)
	}
	
	fun getBurst(flugel: EntityFlugel, i: Int): EntityManaBurst {
		val burst = EntityManaBurst(flugel.worldObj)
		burst.color = Color(179, 77, 179).rgb
		burst.mana = 1
		burst.startingMana = 1
		burst.minManaLoss = 600
		burst.manaLossPerTick = 4f
		burst.gravity = 0f
		val lens = ItemStack(ModItems.terraSword, 1, 0)
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, flugel.commandSenderName)
		burst.sourceLens = lens
		burst.setBurstSourceCoords(0, -1, 0)
		burst.setLocationAndAngles(flugel.posX, flugel.posY + flugel.eyeHeight, flugel.posZ, i.toFloat(), -flugel.rotationPitch)
		burst.posX -= MathHelper.cos(i / 180.0f * Math.PI.toFloat()) / 2.0
		burst.posY -= 0.1
		burst.posZ -= MathHelper.sin(i / 180.0f * Math.PI.toFloat()) / 2.0
		burst.setPosition(burst.posX, burst.posY, burst.posZ)
		burst.yOffset = 0.0f
		val f = 0.4f
		val mx = MathHelper.sin(burst.rotationYaw / 180.0f * Math.PI.toFloat()) * f / 2.0
		val mz = -(MathHelper.cos(burst.rotationYaw / 180.0f * Math.PI.toFloat()) * f) / 2.0
		burst.setMotion(mx * 5, 0.0, mz * 5)
		return burst
	}
	
	override fun continueExecuting(): Boolean {
		return canContinue()
	}
	
	companion object {
		
		private val TAG_ATTACKER_USERNAME = "attackerUsername"
		private val MANA_PER_DAMAGE = 1
	}
}