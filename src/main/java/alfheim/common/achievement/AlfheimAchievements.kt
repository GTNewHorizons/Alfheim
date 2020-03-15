package alfheim.common.achievement

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimAchievementHandler
import alfheim.common.item.AlfheimItems
import net.minecraft.block.Block
import net.minecraft.init.*
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
	val akashic: Achievement //									K
	val excaliber: Achievement //								E
	val flugelSoul: Achievement //								S
	val mask: Achievement //									M
	//val mjolnir: Achievement
	val subspace: Achievement //								U
	val moonlightBow: Achievement //							B
	
	val divineMarksman: Achievement //							D
	val newChance: Achievement // gain race nullifier			N
	val rosaBomb: Achievement //								R
	
	val flugelKill: Achievement // kill hardmode flugel			F
	val outstander: Achievement // live 3 minutes in mask		O
	
	// ---------------> X
	// |
	// |    W I    D N
	// |          B K
	// |     A S M
	// |        F U E
	// |           R
	// |         O
	// Y
	
	init {
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		infuser = AlfheimAchievement("infuser", 1, -2, AlfheimBlocks.manaInfuser, alfheim)
		
		flugelSoul = AlfheimAchievement("flugelSoul", 2, 0, AlfheimItems.flugelSoul, null)
		
		mask = AlfheimAchievement("mask", 4, 0, AlfheimItems.mask, flugelSoul)
		flugelKill = AlfheimAchievement("flugelKill", 3, 1, ModItems.flightTiara, flugelSoul)
		
		akashic = AlfheimAchievement("akashic", 7, -1, AlfheimItems.akashicRecords, mask)
		excaliber = AlfheimAchievement("excaliber", 7, 1, AlfheimItems.excaliber, mask)
		//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, mask)
		moonlightBow = AlfheimAchievement("moonlightBow", 5, -1, AlfheimItems.moonlightBow, mask)
		subspace = AlfheimAchievement("subspace", 5, 1, AlfheimItems.subspaceSpear, mask)
		
		newChance = AlfheimAchievement("newChance", 8, -2, Items.spawn_egg, akashic)
		
		divineMarksman = AlfheimAchievement("divineMarksman", 6, -2, ItemStack(Blocks.red_flower, 1, 2), moonlightBow).setSpecial()
		rosaBomb = AlfheimAchievement("rosaBomb", 6, 2, Blocks.red_flower, subspace).setSpecial()
		outstander = AlfheimAchievement("outstander", 4, 3, Items.diamond_chestplate, mask).setSpecial()
		
		wingedHussar = AlfheimAchievement("wingedHussaurs", -1, -2, AlfheimItems.elvoriumHelmet, infuser).setSpecial()
		
		AchievementPage.registerAchievementPage(AchievementPage(ModInfo.MODID.capitalize(), *achievements.toTypedArray()))
		
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