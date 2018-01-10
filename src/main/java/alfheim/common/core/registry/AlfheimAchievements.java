package alfheim.common.core.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AlfheimAchievements {

	public static Achievement alfheim;
	
	public static void init() {
		alfheim = new Achievement("achievement.alfheim.name", "alfheim", 0, 0, new ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null).initIndependentStat().registerStat();
		AchievementPage.registerAchievementPage(new AchievementPage("Alfheim", new Achievement[]{ alfheim }));
	}
}
