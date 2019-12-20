package alfheim.common.spell.darkness

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer

object SpellNight: SpellBase("night", EnumRace.IMP, 30000, 6000, 50) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (world in MinecraftServer.getServer().worldServers) {
			val time = world.worldTime % 24000
			world.worldTime = world.worldTime + ((if (time < 18000L) 18000L else 42000L) - time)
		}
		
		return result
	}
}