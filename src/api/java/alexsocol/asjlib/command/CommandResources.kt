package alexsocol.asjlib.command

import alexsocol.asjlib.*
import alexsocol.patcher.asm.ASJHookLoader
import net.minecraft.command.*
import org.apache.commons.io.FileUtils
import java.io.File

// omg fucking IntelliJ can't move images go to hell bruh
object CommandResources: CommandBase() {
	
	override fun getCommandAliases() = listOf("rr")
	
	override fun getCommandName() = "resreload"
	
	override fun getCommandUsage(sender: ICommandSender) = "/$commandName"
	
	override fun processCommand(sender: ICommandSender, args: Array<out String>?) {
		if (!ASJHookLoader.OBF) {
			ASJUtilities.say(sender, "Deleting old resources...")
			FileUtils.deleteDirectory(File("../build/classes/main/assets/"))
			ASJUtilities.say(sender, "Copying resources...")
			FileUtils.copyDirectory(File("../src/main/resources/assets/"), File("../build/classes/main/assets/"))
			try_ { FileUtils.copyDirectory(File("../src/api/resources/assets/"), File("../build/classes/main/assets/")) }
			ASJUtilities.say(sender, "Success.")
		} else {
			ASJUtilities.say(sender, "Not in DEV env")
		}
	}
}
