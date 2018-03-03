package alfheim.common.core.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AlfheimAchievements {

	public static Achievement alfheim;
	public static Achievement excaliber;
	public static Achievement flugelSoul;
	public static Achievement mask;
	
	public static void init() {
		alfheim = new Achievement("achievement.alfheim.name", "alfheim", 0, 0, new ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null).initIndependentStat().registerStat();
		excaliber = new Achievement("achievement.excaliber.name", "excaliber", 2, 0, AlfheimItems.excaliber, null).setSpecial().initIndependentStat().registerStat();
		flugelSoul = new Achievement("achievement.flugelSoul.name", "flugelSoul", -2, 0, AlfheimItems.flugelSoul, null).setSpecial().initIndependentStat().registerStat();
		mask = new Achievement("achievement.mask.name", "mask", 0, 2, AlfheimItems.mask, null).setSpecial().initIndependentStat().registerStat();
		AchievementPage.registerAchievementPage(new AchievementPage("Alfheim", new Achievement[]{ alfheim, excaliber, flugelSoul, mask }));
	}
}
