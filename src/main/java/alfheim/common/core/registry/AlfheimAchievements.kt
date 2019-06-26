package alfheim.common.core.registry

import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.stats.Achievement
import net.minecraftforge.common.AchievementPage
import vazkii.botania.api.item.IRelic
import vazkii.botania.common.item.ModItems

import java.util.*

object AlfheimAchievements {
	
	val achievements: MutableList<Achievement> = ArrayList()
	var alfheim: Achievement
	var excaliber: Achievement
	var flugelSoul: Achievement
	var infuser: Achievement
	var mask: Achievement
	//public static Achievement mjolnir;
	var flugelKill: Achievement
	
	fun init() {
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		excaliber = AlfheimAchievement("excaliber", 2, 0, AlfheimItems.excaliber, null)
		flugelSoul = AlfheimAchievement("flugelSoul", -2, 0, AlfheimItems.flugelSoul, null)
		infuser = AlfheimAchievement("infuser", 0, -2, AlfheimBlocks.manaInfuser, null)
		mask = AlfheimAchievement("mask", 0, 2, AlfheimItems.mask, null)
		//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, null);
		flugelKill = AlfheimAchievement("flugelKill", 0, 4, ModItems.flightTiara, null)
		
		AchievementPage.registerAchievementPage(AchievementPage("Alfheim", *achievements.toTypedArray<Achievement>()))
	}
	
	class AlfheimAchievement(name: String, x: Int, y: Int, icon: ItemStack, parent: Achievement): Achievement("achievement.$name.name", name, x, y, icon, parent) {
		
		init {
			achievements.add(this)
			registerStat()
			
			if (icon.item is IRelic) (icon.item as IRelic).bindAchievement = this
		}
		
		constructor(name: String, x: Int, y: Int, icon: Item, parent: Achievement): this(name, x, y, ItemStack(icon), parent) {}
		
		constructor(name: String, x: Int, y: Int, icon: Block, parent: Achievement): this(name, x, y, ItemStack(icon), parent) {}
	}
}
