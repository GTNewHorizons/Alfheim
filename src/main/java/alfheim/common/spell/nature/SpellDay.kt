package alfheim.common.spell.nature

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer

object SpellDay: SpellBase("day", EnumRace.CAITSITH, 30000, 6000, 50) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (world in MinecraftServer.getServer().worldServers) {
			val time = world.worldTime % 24000
			world.worldTime = world.worldTime + ((if (time < 6000L) 6000L else 30000L) - time)
		}
		
		return result
	}
}