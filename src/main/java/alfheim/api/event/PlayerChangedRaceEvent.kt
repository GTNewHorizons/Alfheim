package alfheim.api.event

import alfheim.api.entity.EnumRace
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerChangedRaceEvent(player: EntityPlayer, val raceFrom: EnumRace, val raceTo: EnumRace): PlayerEvent(player)