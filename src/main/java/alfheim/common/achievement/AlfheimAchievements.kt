package alfheim.common.achievement

import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimAchievementHandler
import alfheim.common.item.AlfheimItems
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.stats.Achievement
import net.minecraftforge.common.AchievementPage
import vazkii.botania.api.item.IRelic
import vazkii.botania.common.item.ModItems
import java.util.*

object AlfheimAchievements {
	
	val achievements: MutableList<Achievement> = ArrayList()
	
	val alfheim: Achievement // go to alfheim					A
	val infuser: Achievement // build up infuser				I
	val wingedHussar: Achievement // become winged hussar		W
	
	// relics
	val excaliber: Achievement //								E
	
	val flugelSoul: Achievement //								S
	val mask: Achievement //									M
	//val mjolnir: Achievement
	val subspace: Achievement //								U
	val moonlightBow: Achievement //							B
	
	val newChance: Achievement // gain race nullifier			G
	
	val flugelKill: Achievement // kill hardmode flugel			F
	val outstander: Achievement // live 3 minutes in mask		O
	
	// ---------------> X
	// |
	// |    W  I
	// |          B
	// |     A S M E
	// |        F U
	// |       G
	// |         O
	// Y
	
	init {
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		infuser = AlfheimAchievement("infuser", 2, -2, AlfheimBlocks.manaInfuser, null)
		
		flugelSoul = AlfheimAchievement("flugelSoul", 2, 0, AlfheimItems.flugelSoul, null)
		
		mask = AlfheimAchievement("mask", 4, 0, AlfheimItems.mask, flugelSoul)
		flugelKill = AlfheimAchievement("flugelKill", 3, 1, ModItems.flightTiara, flugelSoul)
		
		newChance = AlfheimAchievement("newChance", 2, 2, AlfheimItems.raceNullifier, flugelKill)
		
		excaliber = AlfheimAchievement("excaliber", 6, 0, AlfheimItems.excaliber, mask)
		//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, mask)
		moonlightBow = AlfheimAchievement("moonlightBow", 5, -1, AlfheimItems.moonlightBow, mask)
		subspace = AlfheimAchievement("subspace", 5, 1, AlfheimItems.subspaceSpear, mask)
		
		outstander = AlfheimAchievement("outstander", 4, 3, Items.diamond_chestplate, mask).setSpecial()
		
		wingedHussar = AlfheimAchievement("wingedHussaurs", -1, -2, AlfheimItems.elvoriumHelmet, null).setSpecial()
		
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