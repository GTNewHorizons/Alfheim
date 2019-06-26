package alfheim.common.lexicon.page

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.handler.CardinalSystemClient
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import net.minecraft.client.Minecraft
import vazkii.botania.common.lexicon.page.PageText

class PageTextLearnableKnowledge(unName: String, internal val knowledge: Knowledge): PageText(unName) {
	
	private fun know(): Boolean {
		return if (Minecraft.getMinecraft().thePlayer == null) false else CardinalSystemClient.segment().knowledge[knowledge.ordinal]
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || know()) unlocalizedName else unlocalizedName + "u"
	}
}
