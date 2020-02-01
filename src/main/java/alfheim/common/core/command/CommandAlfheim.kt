package alfheim.common.core.command

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.handler.*
import alfheim.common.core.util.D
import alfheim.common.crafting.recipe.AlfheimRecipes
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.network.Message3d
import alfheim.common.network.Message3d.m3d
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import vazkii.botania.common.Botania

class CommandAlfheim: CommandBase() {
	
	override fun getRequiredPermissionLevel() = 2
	
	override fun getCommandAliases() = listOf("alf")
	
	override fun getCommandName() = "alfheim"
	
	override fun getCommandUsage(sender: ICommandSender) = "alfheim.commands.alfheim.usage"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (args.size == 1) {
			val esmOld = AlfheimCore.enableElvenStory
			val mmoOld = AlfheimCore.enableMMO
			when {
				args[0].equals("ESM", ignoreCase = true)	-> {
					AlfheimCore.enableElvenStory = !AlfheimCore.enableElvenStory
					toggleESM(AlfheimCore.enableElvenStory)
				}
				args[0].equals("MMO", ignoreCase = true)	-> {
					AlfheimCore.enableMMO = !AlfheimCore.enableMMO
					toggleMMO(AlfheimCore.enableMMO)
				}
				else											-> throw WrongUsageException("alfheim.commands.alfheim.wrong")
			}
			AlfheimConfigHandler.writeModes()
			
			ASJUtilities.sayToAllOnline(String.format(StatCollector.translateToLocal("alfheim.commands.alfheim.done"),
													  sender.commandSenderName,
													  if (AlfheimCore.enableElvenStory) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED,
													  EnumChatFormatting.RESET,
													  if (AlfheimCore.enableMMO) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED,
													  EnumChatFormatting.RESET))
			
			AlfheimCore.network.sendToAll(Message3d(m3d.TOGGLER, (if (args[0].equals("ESM", ignoreCase = true)) 1 else 0).D, ((if (esmOld) 1 else 0) shl 1 or if (AlfheimCore.enableElvenStory) 1 else 0).D, ((if (mmoOld) 1 else 0) shl 1 or if (AlfheimCore.enableMMO) 1 else 0).D))
		} else
			throw WrongUsageException("alfheim.commands.alfheim.wrong")
	}
	
	override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?) = getListOfStringsMatchingLastWord(args, "ESM", "MMO")
	
	companion object {
		
		fun toggleESM(on: Boolean) {
			if (on) {
				AlfheimConfigHandler.initWorldCoordsForElvenStory(AlfheimCore.save)
				ESMHandler.checkAddAttrs()
				if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.addESMRecipes()
			} else {
				if (Botania.thaumcraftLoaded) ThaumcraftAlfheimModule.removeESMRecipes()
				AlfheimCore.enableMMO = false
				toggleMMO(AlfheimCore.enableMMO)
			}
		}
		
		fun toggleMMO(on: Boolean) {
			if (on) {
				CardinalSystem.load(AlfheimCore.save)
				AlfheimRecipes.addMMORecipes()
				AlfheimCore.enableElvenStory = true
				toggleESM(AlfheimCore.enableElvenStory)
				for (o in MinecraftServer.getServer().configurationManager.playerEntityList) CardinalSystem.transfer(o as EntityPlayerMP)
			} else {
				CardinalSystem.save(AlfheimCore.save)
				AlfheimRecipes.removeMMORecipes()
			}
		}
	}
}