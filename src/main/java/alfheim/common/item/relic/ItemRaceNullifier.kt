package alfheim.common.item.relic

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.util.hasAchievement
import alfheim.common.item.ItemMod
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemRaceNullifier: ItemMod("RaceNullifier") {
	
	init {
		setFull3D()
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player !is EntityPlayerMP) return stack
		
		if (AlfheimCore.enableElvenStory && player.race != EnumRace.HUMAN && player.race != EnumRace.ALV) {
			if (player.hasAchievement(AlfheimAchievements.newChance))
				player.race = EnumRace.HUMAN
			else
				ASJUtilities.say(player, "alfheimmisc.noflugelach")
			
			--stack.stackSize
		}
		
		return stack
	}
}
