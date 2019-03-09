package alfheim.common.item;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.status.StatusConsoleListener;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.common.Loader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemStoryToken extends Item {
	
	public static final String TAG_STORY = "story";
	public IIcon[] icons = new IIcon[2];
	
	public ItemStoryToken() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setHasSubtypes(true);
		setMaxStackSize(1);
		setUnlocalizedName("StoryToken");
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		icons[0] = reg.registerIcon(ModInfo.MODID + ":StoryToken");
		icons[1] = reg.registerIcon(ModInfo.MODID + ":StoryTokenSigned");
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta == 1 ? 1 : 0];
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean extra) {
		if (stack.getItemDamage() == 1) {
			if (!ItemNBTHelper.verifyExistance(stack, TAG_STORY)) ItemNBTHelper.setInt(stack, TAG_STORY, player.getRNG().nextInt(AlfheimConfig.storyLines) + 1);
			info.add(StatCollector.translateToLocal(String.format("item.StoryToken.story.%d",
					 ItemNBTHelper.getInt(stack, TAG_STORY, 0))));
		}
	}
}