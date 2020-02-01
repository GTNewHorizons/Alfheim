package alfheim.common.lexicon.page

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.util.*
import net.minecraft.stats.Achievement
import vazkii.botania.common.lexicon.page.PageText

class PageTextLearnableAchievement(unName: String, internal val achievement: Achievement): PageText(unName) {
	
	fun known(): Boolean {
		return if (mc.thePlayer == null) false else mc.thePlayer.hasAchievement(achievement)
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || known()) unlocalizedName else unlocalizedName + "u"
	}
}
