package alfheim.common.item;

import static vazkii.botania.common.core.helper.ItemNBTHelper.getString;
import static vazkii.botania.common.core.helper.ItemNBTHelper.setInt;
import static vazkii.botania.common.core.helper.ItemNBTHelper.setString;
import static vazkii.botania.common.core.helper.ItemNBTHelper.verifyExistance;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PlayerSegment;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPeacePipe extends Item {

	public static String TAG_LEAD = "PARTYLEAD";
	public static String TAG_MEMBER = "PARTYMEMBER";
	public static String TAG_MEMBERS = "PARTYMEMBERS";
	
	public ItemPeacePipe() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":PeacePipe");
		setUnlocalizedName("PeacePipe");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!AlfheimCore.enableMMO) return stack;
		if (!world.isRemote) {
			if (!verifyExistance(stack, TAG_LEAD)) {
				Party pt = PartySystem.getParty(player);
				if (pt.count >= AlfheimConfig.maxPartyMembers) {
					ASJUtilities.say(player, "alfheimmisc.party.full");
					return stack;
				}
				setString(stack, TAG_LEAD, player.getCommandSenderName());
				setInt(stack, TAG_MEMBERS, pt.count-1);
				for (int i = 1; i < pt.count; i++) setString(stack, TAG_MEMBER + i, pt.getName(i));
			} else {
				Party pt = PartySystem.getParty(player);
				if (pt.count > 1) {
					ASJUtilities.say(player, "alfheimmisc.party.leave");
					return stack;
				}
				PlayerSegment segment = CardinalSystem.playerSegments.get(getString(stack, TAG_LEAD, ""));
				if (segment == null) {
					ASJUtilities.say(player, "alfheimmisc.party.no");
					return stack;
				}
				Party py = segment.party;
				if (py == null) {
					ASJUtilities.say(player, "alfheimmisc.party.no");
					return stack;
				}
				if (py.count >= AlfheimConfig.maxPartyMembers) {
					ASJUtilities.say(player, "alfheimmisc.party.full");
					return stack;
				}
				if (py == pt) {
					ASJUtilities.say(player, "alfheimmisc.party.already");
					return stack;
				}
				
				py.add(player);
				PartySystem.setParty(player, py);
				--stack.stackSize;
				return stack;
			}
		}
		return stack;
	}
}