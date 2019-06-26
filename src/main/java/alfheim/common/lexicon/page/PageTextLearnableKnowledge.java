package alfheim.common.lexicon.page;

import alexsocol.asjlib.ASJUtilities;
import alfheim.client.core.handler.CardinalSystemClient;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import net.minecraft.client.Minecraft;
import vazkii.botania.common.lexicon.page.PageText;

public class PageTextLearnableKnowledge extends PageText {

	final Knowledge knowledge;
	
	public PageTextLearnableKnowledge(String unName, Knowledge kn) {
		super(unName);
		knowledge = kn;
	}
	
	@SuppressWarnings("AccessStaticViaInstance")
	private boolean know() {
		if (Minecraft.getMinecraft().thePlayer == null) return false;
		return CardinalSystemClient.segment().knowledge[knowledge.ordinal()];
	}
	
	@Override
	public String getUnlocalizedName() {
		return ASJUtilities.isServer() || know() ? unlocalizedName : unlocalizedName + "u";
	}
}
