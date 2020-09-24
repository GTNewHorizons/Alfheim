package alexsocol.asjlib.command

import alexsocol.asjlib.*
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChunkCoordinates

object CommandDimTP: CommandBase() {
	
	override fun getRequiredPermissionLevel() = 2
	
	override fun canCommandSenderUseCommand(sender: ICommandSender) = sender is EntityPlayer
	
	override fun getCommandName() = "tpdim"
	
	override fun getCommandUsage(sender: ICommandSender) = "alfheim.commands.tpdim.usage"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (args.size == 1 && args[0].matches("-?\\d+".toRegex()) && sender is EntityPlayer) {
			try {
				val id = args[0].toInt()
				try {
					val w = MinecraftServer.getServer().worldServerForDimension(id) ?: throw NullPointerException("Loaded dimension is null")
					
					var s: ChunkCoordinates? = sender.getBedLocation(id)
					// stupid minecraft returns overworld coordinates in ANY dimension
					if (s == null) s = w.spawnPoint
					ASJUtilities.sendToDimensionWithoutPortal(sender, id, s!!.posX.D, s.posY.D, s.posZ.D)
				} catch (e: Throwable) {
					throw WrongUsageException("alfheim.commands.tpdim.worlderr")
				}
				
			} catch (nfe: NumberFormatException) {
				throw WrongUsageException("alfheim.commands.tpdim.wrongid")
			}
			
		} else {
			throw WrongUsageException("alfheim.commands.tpdim.wrong")
		}
	}
}