package alexsocol.asjlib.command

import alexsocol.asjlib.*
import net.minecraft.command.*

object CommandExplode: CommandBase() {
	
	override fun getRequiredPermissionLevel() = 4
	
	override fun getCommandName() = "explode"
	
	override fun getCommandUsage(sender: ICommandSender?) = "$commandName.usage"
	
	override fun processCommand(sender: ICommandSender, args: Array<out String>) {
		val (x, y, z) = sender.playerCoordinates ?: return
		
		val size = args.getOrNull(0)?.toFloat() ?: 1f
		val breakBlocks = args.getOrNull(1)?.toBoolean() ?: true
		val flame = args.getOrNull(2)?.toBoolean() ?: false
		
		sender.entityWorld.newExplosion(null, x.D, y.D, z.D, size, flame, breakBlocks)
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<out String>): List<Any?> {
		return if (args.size == 2 || args.size == 3) getListOfStringsMatchingLastWord(args, "true", "false") else emptyList()
	}
}
