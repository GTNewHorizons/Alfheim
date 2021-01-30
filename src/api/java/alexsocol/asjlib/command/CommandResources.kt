package alexsocol.asjlib.command

import alexsocol.patcher.asm.ASJHookLoader
import net.minecraft.command.*
import org.apache.commons.io.FileUtils
import java.io.File

object CommandResources: CommandBase() {
	
	override fun getCommandAliases() = listOf("rr")
	
	override fun getCommandName() = "resreload"
	
	override fun getCommandUsage(sender: ICommandSender) = "/$commandName"
	
	override fun processCommand(sender: ICommandSender, args: Array<out String>?) {
		if (!ASJHookLoader.OBF) {
			// omg fucking IntelliJ can't move images go to hell bruh
			FileUtils.deleteDirectory(File("../build/classes/main/assets/"))
			FileUtils.copyDirectory(File("../src/main/resources/assets/"), File("../build/classes/main/assets/"))
		}
	}
	
}
