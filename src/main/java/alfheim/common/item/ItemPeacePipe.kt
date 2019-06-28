package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper.*

class ItemPeacePipe: Item() {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setTextureName(ModInfo.MODID + ":PeacePipe")
		unlocalizedName = "PeacePipe"
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack? {
		if (!AlfheimCore.enableMMO) return stack
		if (!world.isRemote) {
			if (!verifyExistance(stack, TAG_LEAD)) {
				val pt = PartySystem.getParty(player)
				if (pt!!.count >= AlfheimConfig.maxPartyMembers) {
					ASJUtilities.say(player, "alfheimmisc.party.full")
					return stack
				}
				setString(stack, TAG_LEAD, player.commandSenderName)
				setInt(stack, TAG_MEMBERS, pt.count - 1)
				for (i in 1 until pt.count) setString(stack, TAG_MEMBER + i, pt.getName(i))
			} else {
				val pt = PartySystem.getParty(player)
				if (pt!!.count > 1) {
					ASJUtilities.say(player, "alfheimmisc.party.leave")
					return stack
				}
				val segment = CardinalSystem.playerSegments[getString(stack, TAG_LEAD, "")]
				if (segment == null) {
					ASJUtilities.say(player, "alfheimmisc.party.no")
					return stack
				}
				val py = segment.party
				if (py.count >= AlfheimConfig.maxPartyMembers) {
					ASJUtilities.say(player, "alfheimmisc.party.full")
					return stack
				}
				if (py === pt) {
					ASJUtilities.say(player, "alfheimmisc.party.already")
					return stack
				}
				
				py.add(player)
				PartySystem.setParty(player, py)
				--stack.stackSize
				return stack
			}
		}
		return stack
	}
	
	companion object {
		
		const val TAG_LEAD = "PARTYLEAD"
		const val TAG_MEMBER = "PARTYMEMBER"
		const val TAG_MEMBERS = "PARTYMEMBERS"
	}
}