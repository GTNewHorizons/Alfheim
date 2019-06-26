package alfheim.common.spell.darkness

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer
import net.minecraft.world.WorldServer

class SpellNight: SpellBase("night", EnumRace.IMP, 30000, 6000, 50) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		for (world in MinecraftServer.getServer().worldServers) {
			val time = world.worldTime % 24000
			world.worldTime = world.worldTime + ((if (time < 18000L) 18000L else 42000L) - time)
		}
		
		return result
	}
}