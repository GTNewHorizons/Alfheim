package alfheim.common.achievement

import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimAchievementHandler
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
	val flugelKill: Achievement
	val infuser: Achievement
	
	val wingedHussaurs: Achievement
	
	// relics
	val excaliber: Achievement?
	val flugelSoul: Achievement?
	val mask: Achievement?
	//val mjolnir: Achievement?
	val subspace: Achievement?
	val moonlightBow: Achievement?
	
	// +--------->
	// |           X
	// |
	// |
	// |
	// V
	//   Y
	
	init {
		if(ConfigHandler.relicsEnabled) {
			flugelSoul = AlfheimAchievement("flugelSoul", 2, 0, AlfheimItems.flugelSoul, null)
			mask = AlfheimAchievement("mask", 4, 0, AlfheimItems.mask, flugelSoul)
			
			excaliber = AlfheimAchievement("excaliber", 6, 0, AlfheimItems.excaliber, mask)
			//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, null)
			moonlightBow = AlfheimAchievement("moonlightBow", 5, -1, AlfheimItems.moonlightBow, mask)
			subspace = AlfheimAchievement("subspace", 5, 1, AlfheimItems.subspaceSpear, mask)
		} else {
			excaliber = null
			flugelSoul = null
			mask = null
			moonlightBow = null
			subspace = null
		}
		
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		flugelKill = AlfheimAchievement("flugelKill", 3, 1, ModItems.flightTiara, flugelSoul)
		infuser = AlfheimAchievement("infuser", 2, -2, AlfheimBlocks.manaInfuser, null)
		
		wingedHussaurs = AlfheimAchievement("wingedHussaurs", -1, -2, AlfheimItems.elvoriumHelmet, null).setSpecial()
		
		AchievementPage.registerAchievementPage(AchievementPage("Alfheim", *achievements.toTypedArray()))
		
		AlfheimAchievementHandler
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