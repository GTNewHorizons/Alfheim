package alexsocol.asjlib.command

import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.*

object CommandHeal: CommandBase() {
	
	override fun getCommandName() = "heal"
	
	override fun getCommandUsage(sender: ICommandSender?) = "/$commandName [h][f][e[!]]"
	
	override fun processCommand(sender: ICommandSender, args: Array<String>) {
		if (sender !is EntityPlayerMP) return
		
		var heal: Boolean
		var feed: Boolean
		var effs: Boolean
		var eAll: Boolean
		
		args.getOrElse(0) { "hfe" }.toLowerCase().apply {
			heal = contains('h')
			feed = contains('f')
			effs = contains('e')
			eAll = contains("e!")
		}
		
		if (heal) sender.heal(sender.maxHealth)
		if (feed) sender.foodStats.addStats(20, 20f)
		if (effs) sender.activePotionEffects.removeAll {
			val flag = Potion.potionTypes[(it as PotionEffect).potionID].isBadEffect || eAll
			if (flag) sender.onFinishedPotionEffect(it)
			flag
		}
	}
}