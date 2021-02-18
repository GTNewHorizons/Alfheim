package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper.*

class ItemPeacePipe: ItemMod("PeacePipe") {
	
	init {
		setFull3D()
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack? {
		if (!AlfheimConfigHandler.enableMMO) return stack
		
		if (!world.isRemote) {
			if (!verifyExistance(stack, TAG_LEAD)) {
				val pt = PartySystem.getParty(player)
				if (pt.count >= AlfheimConfigHandler.maxPartyMembers) {
					ASJUtilities.say(player, "alfheimmisc.party.full")
					return stack
				}
				setString(stack, TAG_LEAD, player.commandSenderName)
				setInt(stack, TAG_MEMBERS, pt.count - 1)
				for (i in 1 until pt.count) setString(stack, TAG_MEMBER + i, pt.getName(i))
			} else {
				val pt = PartySystem.getParty(player)
				if (pt.count > 1) {
					ASJUtilities.say(player, "alfheimmisc.party.leave")
					return stack
				}
				val segment = CardinalSystem.playerSegments[getString(stack, TAG_LEAD, "")]
				if (segment == null) {
					ASJUtilities.say(player, "alfheimmisc.party.no")
					return stack
				}
				val py = segment.party
				if (py.count >= AlfheimConfigHandler.maxPartyMembers) {
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
	
	override fun shouldRotateAroundWhenRendering() = true
	
	companion object {
		
		const val TAG_LEAD = "PARTYLEAD"
		const val TAG_MEMBER = "PARTYMEMBER"
		const val TAG_MEMBERS = "PARTYMEMBERS"
	}
}