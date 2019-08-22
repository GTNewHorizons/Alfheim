package alfheim.common.lexicon.page

import alexsocol.asjlib.ASJUtilities
import net.minecraft.client.Minecraft
import net.minecraft.stats.Achievement
import vazkii.botania.common.lexicon.page.PageText

class PageTextLearnableAchievement(unName: String, internal val achievement: Achievement): PageText(unName) {
	
	fun known(): Boolean {
		return if (Minecraft.getMinecraft().thePlayer == null) false else Minecraft.getMinecraft().thePlayer.statFileWriter.hasAchievementUnlocked(achievement)
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || known()) unlocalizedName else unlocalizedName + "u"
	}
}
