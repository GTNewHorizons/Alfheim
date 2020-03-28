package alfheim.common.lexicon.page

import alexsocol.asjlib.*
import net.minecraft.stats.Achievement
import vazkii.botania.common.lexicon.page.PageText

class PageTextLearnableAchievement(unName: String, internal val achievement: Achievement): PageText(unName) {
	
	fun known(): Boolean {
		return mc.thePlayer?.hasAchievement(achievement) == true
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || known()) unlocalizedName else unlocalizedName + "u"
	}
}
