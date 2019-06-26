package alexsocol.asjlib.command

import alexsocol.asjlib.ASJUtilities
import cpw.mods.fml.common.event.FMLServerStartingEvent
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World

class CommandDimTP private constructor(): CommandBase() {
	
	override fun getRequiredPermissionLevel(): Int {
		return 2
	}
	
	override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
		return sender is EntityPlayer
	}
	
	override fun getCommandName(): String {
		return "tpdim"
	}
	
	override fun getCommandUsage(sender: ICommandSender): String {
		return "alfheim.commands.tpdim.usage"
	}
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (args.size == 1 && args[0].matches("-?\\d+".toRegex()) && sender is EntityPlayer) {
			try {
				val id = Integer.valueOf(args[0])
				try {
					val w = MinecraftServer.getServer().worldServerForDimension(id)
							?: throw NullPointerException("Loaded dimension is null")
					
					var s: ChunkCoordinates? = sender.getBedLocation(id)
					// stupid minecraft returns overworld coordinates in ANY dimension
					if (s == null) s = w.spawnPoint
					ASJUtilities.sendToDimensionWithoutPortal(sender, id, s!!.posX.toDouble(), s.posY.toDouble(), s.posZ.toDouble())
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
	
	companion object {
		
		private val instance = CommandDimTP()
		var registered = false
		
		fun register(event: FMLServerStartingEvent) {
			event.registerServerCommand(instance)
			registered = true
		}
	}
}