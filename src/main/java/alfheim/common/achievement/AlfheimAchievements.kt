package alfheim.common.achievement

import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.AlfheimItems
import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.stats.Achievement
import net.minecraftforge.common.AchievementPage
import vazkii.botania.api.item.IRelic
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.ModItems
import java.util.*

object AlfheimAchievements {
	
	val achievements: MutableList<Achievement> = ArrayList()
	val alfheim: Achievement
	val excaliber: Achievement?
	val flugelSoul: Achievement?
	val infuser: Achievement
	val mask: Achievement?
	val moonlightBow: Achievement?
	val subspace: Achievement?
	//val mjolnir: Achievement
	val flugelKill: Achievement
	
	init {
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		flugelKill = AlfheimAchievement("flugelKill", 0, 4, ModItems.flightTiara, null)
		infuser = AlfheimAchievement("infuser", 0, -4, AlfheimBlocks.manaInfuser, null)
		
		if(ConfigHandler.relicsEnabled) {
			excaliber = AlfheimAchievement("excaliber", 2, 0, AlfheimItems.excaliber, null)
			flugelSoul = AlfheimAchievement("flugelSoul", -2, 0, AlfheimItems.flugelSoul, null)
			mask = AlfheimAchievement("mask", 0, 2, AlfheimItems.mask, null)
			//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, null)
			moonlightBow = AlfheimAchievement("moonlightBow", 3, -1, AlfheimItems.moonlightBow, null)
			subspace = AlfheimAchievement("subspace", 3, 1, AlfheimItems.subspaceSpear, null)
		} else {
			excaliber = null
			flugelSoul = null
			mask = null
			moonlightBow = null
			subspace = null
		}
		
		AchievementPage.registerAchievementPage(AchievementPage("Alfheim", *achievements.toTypedArray()))
	}
}

class AlfheimAchievement(name: String, x: Int, y: Int, icon: ItemStack, parent: Achievement?): Achievement("achievement.$name.name", name, x, y, icon, parent) {
	
	init {
		AlfheimAchievements.achievements.add(this)
		registerStat()
		
		if (icon.item is IRelic) (icon.item as IRelic).bindAchievement = this
	}
	
	constructor(name: String, x: Int, y: Int, icon: Item, parent: Achievement?): this(name, x, y, ItemStack(icon), parent)
	
	constructor(name: String, x: Int, y: Int, icon: Block, parent: Achievement?): this(name, x, y, ItemStack(icon), parent)
}