package alfheim.common.spell.fire

import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.network.Message3d
import alfheim.common.network.Message3d.m3d
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer

object SpellSun: SpellBase("sun", EnumRace.SALAMANDER, 30000, 6000, 50) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (world in MinecraftServer.getServer().worldServers) {
			val time = caster.worldObj.rand.nextInt(168000) + 12000
			world.worldInfo.isRaining = false
			world.worldInfo.rainTime = time
			world.worldInfo.isThundering = false
			world.worldInfo.thunderTime = time
			AlfheimCore.network.sendToDimension(Message3d(m3d.WAETHER, 0.0, time.toDouble(), time.toDouble()), world.provider.dimensionId)
		}
		
		return result
	}
}