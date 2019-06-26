package alfheim.common.core.command

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.EnumRace
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*

class CommandRace: CommandBase() {
	
	override fun getRequiredPermissionLevel(): Int {
		return 0
	}
	
	override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
		return AlfheimCore.enableElvenStory && sender is EntityPlayer && (AlfheimConfig.enableWingsNonAlfheim || sender.dimension == AlfheimConfig.dimensionIDAlfheim && EnumRace.getRace(sender) == EnumRace.HUMAN)
	}
	
	override fun getCommandName(): String {
		return "race"
	}
	
	override fun getCommandUsage(sender: ICommandSender): String {
		return "elvenstory.commands.race.usage"
	}
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (!AlfheimCore.enableElvenStory) throw CommandNotFoundException("elvenstory.commands.race.unavailable")
		if (args.size == 1 && sender is EntityPlayer) {
			val r = EnumRace.valueOf(EnumRace.unlocalize(args[0]))
			if (r == EnumRace.ALV || r == EnumRace.HUMAN) throw WrongUsageException(getCommandUsage(sender))
			if (EnumRace.getRace(sender) == EnumRace.HUMAN) {
				sender.getEntityAttribute(AlfheimAPI.RACE).baseValue = r.ordinal.toDouble()
				sender.capabilities.allowFlying = true
				sender.sendPlayerAbilities()
				sender.setSpawnChunk(ChunkCoordinates(MathHelper.floor_double(AlfheimConfig.zones[r.ordinal].xCoord), MathHelper.floor_double(AlfheimConfig.zones[r.ordinal].yCoord), MathHelper.floor_double(AlfheimConfig.zones[r.ordinal].zCoord)), true, AlfheimConfig.dimensionIDAlfheim)
				ASJUtilities.sendToDimensionWithoutPortal(sender, AlfheimConfig.dimensionIDAlfheim, AlfheimConfig.zones[r.ordinal].xCoord, AlfheimConfig.zones[r.ordinal].yCoord, AlfheimConfig.zones[r.ordinal].zCoord)
			}
		} else {
			throw WrongUsageException(getCommandUsage(sender))
		}
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?): List<*>? {
		if (!AlfheimCore.enableElvenStory) return null
		if (sender is EntityPlayer && args!!.size == 1 && EnumRace.getRace(sender as EntityPlayer?) == EnumRace.HUMAN) {
			val ss = arrayOfNulls<String>(9)
			for (i in ss.indices) ss[i] = EnumRace.getByID((i + 1).toDouble()).localize()
			return CommandBase.getListOfStringsMatchingLastWord(args, *ss)
		}
		return null
	}
}
