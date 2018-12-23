package alfheim.common.lexicon.page;

import alexsocol.asjlib.ASJUtilities;
import alfheim.client.core.handler.CardinalSystemClient;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Achievement;
import vazkii.botania.common.lexicon.page.PageText;

public class PageTextLearnableKnowledge extends PageText {

	Knowledge knowledge;
	
	public PageTextLearnableKnowledge(String unName, Knowledge kn) {
		super(unName);
		knowledge = kn;
	}
	
	private boolean know() {
		if (Minecraft.getMinecraft().thePlayer == null) return false;
		return CardinalSystemClient.segment().knowledge[knowledge.ordinal()];
	}
	
	@Override
	public String getUnlocalizedName() {
		return ASJUtilities.isServer() || know() ? unlocalizedName : unlocalizedName + "u";
	}
}
