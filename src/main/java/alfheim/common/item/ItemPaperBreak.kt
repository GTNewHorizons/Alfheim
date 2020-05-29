package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.helper.IconHelper
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper.getCompound

class ItemPaperBreak: ItemMod("PaperBreak") {

	override fun getIconIndex(stack: ItemStack) = textures[if (stack.hasDisplayName()) 1 else 0]!!
	
	override fun getIcon(stack: ItemStack, pass: Int) = getIconIndex(stack)
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		textures = Array(2) { IconHelper.forItem(reg, this, it) }
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!AlfheimConfigHandler.enableMMO) return stack
		
		if (!world.isRemote) {
			val name = getCompound(stack, "display", false).getString("Name")
			val pt = PartySystem.getParty(player)
			val pl = pt.pl
			val flag1 = name != null && name.isNotEmpty()
			val flag2 = flag1 && name!!.equals(player.commandSenderName, ignoreCase = true)
			
			if (player != pl && !flag2) {
				ASJUtilities.say(player, "alfheimmisc.party.notpl")
				return stack
			}
			
			if (flag1) {
				if (pt.remove(name))
					--stack.stackSize
				else
					player.addChatMessage(ChatComponentText(StatCollector.translateToLocalFormatted("alfheimmisc.party.notinpartyoffline", name)))
				return stack
			}
			
			return stack
		}
		return stack
	}
	
	companion object {
		
		lateinit var textures: Array<IIcon>
	}
}
