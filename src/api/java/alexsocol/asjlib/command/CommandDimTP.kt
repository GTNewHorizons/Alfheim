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
	
	override fun getCommandUsage(sender: ICommandSender) = "asjcore.commands.tpdim.usage"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		try {
			sender as EntityPlayer
			val id = args[0].toInt()
			val w = MinecraftServer.getServer().worldServerForDimension(id) ?: throw NoWorldException("Loaded dimension is null")
			var s: ChunkCoordinates? = sender.getBedLocation(id)
			// stupid minecraft returns overworld coordinates in ANY dimension
			if (s == null) s = w.spawnPoint
			ASJUtilities.sendToDimensionWithoutPortal(sender, id, s!!.posX.D, s.posY.D, s.posZ.D)
			
		} catch (e: NumberFormatException) {
			throw WrongUsageException("asjcore.commands.tpdim.wrongid", e)
		} catch (e: NoWorldException) {
			throw WrongUsageException("asjcore.commands.tpdim.worlderr", e)
		} catch (e: Throwable) {
			throw WrongUsageException("asjcore.commands.tpdim.wrong", e)
		}
	}
	
	private class NoWorldException(message: String): RuntimeException(message)
}