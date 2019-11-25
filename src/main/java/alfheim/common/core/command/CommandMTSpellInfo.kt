package alfheim.common.core.command

import alfheim.AlfheimCore
import alfheim.common.network.Message0dC
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayerMP

class CommandMTSpellInfo: CommandBase() {
	
	override fun getRequiredPermissionLevel() = 0
	
	override fun getCommandName() = "mtspell"
	
	override fun getCommandUsage(sender: ICommandSender) = "alfheim.commands.mtspell.usage"
	
	override fun addTabCompletionOptions(sender: ICommandSender?, params: Array<out String>?) = emptyList<String>()
	
	override fun processCommand(sender: ICommandSender?, params: Array<String>?) {
		if (sender !is EntityPlayerMP) return
		
		if (!AlfheimCore.enableMMO) throw WrongUsageException("alfheim.commands.mtspell.wrong")
		
		AlfheimCore.network.sendTo(Message0dC(Message0dC.m0dc.MTSPELL), sender)
	}
}