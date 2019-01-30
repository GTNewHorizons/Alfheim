package alfheim.common.item;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPaperBreak extends Item {

	public static IIcon[] textures = new IIcon[2];
	
	public ItemPaperBreak() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":Paper");
		setUnlocalizedName("PaperBreak");
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return textures[stack.hasDisplayName() ? 1 : 0];
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":Paper");
		textures[1] = reg.registerIcon(ModInfo.MODID + ":PaperSigned");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!AlfheimCore.enableMMO) return stack;
		if (!world.isRemote) {
			String name = getCompound(stack, "display", false).getString("Name");
			Party pt = PartySystem.getParty(player);
			EntityPlayer pl = pt.getPL();
			boolean flag1 = name != null && !name.isEmpty(), flag2 = flag1 && name.equalsIgnoreCase(player.getCommandSenderName());
			
			if (pl != null && !player.equals(pl) && !flag2) {
				ASJUtilities.say(player, "alfheimmisc.party.notpl");
				return stack;
			}
			
			if (flag1) {
				if (pt.remove(name)) --stack.stackSize;
				else player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("alfheimmisc.party.notinpartyoffline", name)));
				return stack;
			}
			
			return stack;
		}
		return stack;
	}
}
