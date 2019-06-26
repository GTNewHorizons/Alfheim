package alfheim.common.lexicon.page;

import alexsocol.asjlib.ASJUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Achievement;
import vazkii.botania.common.lexicon.page.PageText;

public class PageTextLearnableAchievement extends PageText {

	final Achievement achievement;
	
	public PageTextLearnableAchievement(String unName, Achievement ach) {
		super(unName);
		achievement = ach;
	}
	
	public boolean known() {
		if (Minecraft.getMinecraft().thePlayer == null) return false;
		return Minecraft.getMinecraft().thePlayer.getStatFileWriter().hasAchievementUnlocked(achievement);
	}
	
	@Override
	public String getUnlocalizedName() {
		return ASJUtilities.isServer() || known() ? unlocalizedName : unlocalizedName + "u";
	}
}
