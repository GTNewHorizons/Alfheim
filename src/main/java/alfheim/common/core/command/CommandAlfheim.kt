package alfheim.common.core.command

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.handler.*
import alfheim.common.core.registry.AlfheimRecipes
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.network.Message3d
import alfheim.common.network.Message3d.m3d
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import vazkii.botania.common.Botania

import java.util.*

class CommandAlfheim: CommandBase() {
	
	override fun getRequiredPermissionLevel(): Int {
		return 2
	}
	
	override fun getCommandAliases(): List<*> {
		return Arrays.asList("alf")
	}
	
	override fun getCommandName(): String {
		return "alfheim"
	}
	
	override fun getCommandUsage(sender: ICommandSender): String {
		return "alfheim.commands.alfheim.usage"
	}
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (args.size == 1) {
			val esmOld = AlfheimCore.enableElvenStory
			val mmoOld = AlfheimCore.enableMMO
			if (args[0].equals("ESM", ignoreCase = true))
				toggleESM(AlfheimCore.enableElvenStory = !AlfheimCore.enableElvenStory)
			else if (args[0].equals("MMO", ignoreCase = true))
				toggleMMO(AlfheimCore.enableMMO = !AlfheimCore.enableMMO)
			else
				throw WrongUsageException("alfheim.commands.alfheim.wrong")
			AlfheimConfig.writeModes()
			
			ASJUtilities.sayToAllOnline(String.format(StatCollector.translateToLocal("alfheim.commands.alfheim.done"),
													  sender.commandSenderName,
													  if (AlfheimCore.enableElvenStory) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED,
													  EnumChatFormatting.RESET,
													  if (AlfheimCore.enableMMO) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED,
													  EnumChatFormatting.RESET))
			
			AlfheimCore.network.sendToAll(Message3d(m3d.TOGGLER, (if (args[0].equals("ESM", ignoreCase = true)) 1 else 0).toDouble(), ((if (esmOld) 1 else 0) shl 1 or if (AlfheimCore.enableElvenStory) 1 else 0).toDouble(), ((if (mmoOld) 1 else 0) shl 1 or if (AlfheimCore.enableMMO) 1 else 0).toDouble()))
		} else
			throw WrongUsageException("alfheim.commands.alfheim.wrong")
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?): List<*> {
		return CommandBase.getListOfStringsMatchingLastWord(args, "ESM", "MMO")
	}
	
	companion object {
		
		fun toggleESM(on: Boolean) {
			if (on) {
				AlfheimConfig.initWorldCoordsForElvenStory(AlfheimCore.save)
				EventHandler.checkAddAttrs()
				if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.addESMRecipes()
			} else {
				if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.removeESMRecipes()
				toggleMMO(AlfheimCore.enableMMO = false)
			}
		}
		
		fun toggleMMO(on: Boolean) {
			if (on) {
				CardinalSystem.load(AlfheimCore.save)
				AlfheimRecipes.addMMORecipes()
				toggleESM(AlfheimCore.enableElvenStory = true)
				for (o in MinecraftServer.getServer().configurationManager.playerEntityList) CardinalSystem.transfer(o as EntityPlayerMP)
			} else {
				CardinalSystem.save(AlfheimCore.save)
				AlfheimRecipes.removeMMORecipes()
			}
		}
	}
}