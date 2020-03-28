package alfheim.common.lexicon.page

import alexsocol.asjlib.*
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import vazkii.botania.common.lexicon.page.PageText

class PageTextLearnableKnowledge(unName: String, internal val knowledge: Knowledge): PageText(unName) {
	
	private fun know(): Boolean {
		return if (mc.thePlayer == null) false else PlayerSegmentClient.knowledge.contains(knowledge.toString())
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || know()) unlocalizedName else unlocalizedName + "u"
	}
}
