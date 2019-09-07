package alfheim.common.spell.water

import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.network.Message3d
import alfheim.common.network.Message3d.m3d
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer

class SpellRain: SpellBase("rain", EnumRace.UNDINE, 30000, 6000, 50) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (world in MinecraftServer.getServer().worldServers) {
			val r = caster.worldObj.rand.nextInt(12000) + 3600
			val t = caster.worldObj.rand.nextInt(168000) + 12000
			world.worldInfo.isRaining = true
			world.worldInfo.rainTime = r
			world.worldInfo.isThundering = false
			world.worldInfo.thunderTime = t
			AlfheimCore.network.sendToDimension(Message3d(m3d.WAETHER, 1.0, r.toDouble(), t.toDouble()), world.provider.dimensionId)
		}
		
		return result
	}
}