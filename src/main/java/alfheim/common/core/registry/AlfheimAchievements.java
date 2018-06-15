package alfheim.common.core.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;

public class AlfheimAchievements {

	public static List<Achievement> achievements = new ArrayList();
	public static Achievement alfheim;
	public static Achievement excaliber;
	public static Achievement flugelSoul;
	public static Achievement mask;
	//public static Achievement mjolnir;
	public static Achievement flugelKill;
	
	public static void init() {
		alfheim = new AlfheimAchievement("alfheim", 0, 0, new ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null);
		excaliber = new AlfheimAchievement("excaliber", 2, 0, AlfheimItems.excaliber, null);
		flugelSoul = new AlfheimAchievement("flugelSoul", -2, 0, AlfheimItems.flugelSoul, null);
		mask = new AlfheimAchievement("mask", 0, 2, AlfheimItems.mask, null);
		//mjolnir = new AlfheimAchievement("mjolnir", 0, -2, AlfheimItems.mjolnir, null);
		flugelKill = new AlfheimAchievement("flugelKill", 0, 4, ModItems.flightTiara, null);
		
		AchievementPage.registerAchievementPage(new AchievementPage("Alfheim", achievements.toArray(new Achievement[achievements.size()])));
	}
	
	public static class AlfheimAchievement extends Achievement {

		public AlfheimAchievement(String name, int x, int y, ItemStack icon, Achievement parent) {
			super("achievement." + name + ".name", name, x, y, icon, parent);
			achievements.add(this);
			registerStat();

			if(icon.getItem() instanceof IRelic) ((IRelic) icon.getItem()).setBindAchievement(this);
		}

		public AlfheimAchievement(String name, int x, int y, Item icon, Achievement parent) {
			this(name, x, y, new ItemStack(icon), parent);
		}

		public AlfheimAchievement(String name, int x, int y, Block icon, Achievement parent) {
			this(name, x, y, new ItemStack(icon), parent);
		}
	}
}
