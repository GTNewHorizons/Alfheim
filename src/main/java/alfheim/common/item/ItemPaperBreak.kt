package alfheim.common.item

import vazkii.botania.common.core.helper.ItemNBTHelper.*

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IIcon
import net.minecraft.util.StatCollector
import net.minecraft.world.World

class ItemPaperBreak: Item() {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setTextureName(ModInfo.MODID + ":Paper")
		unlocalizedName = "PaperBreak"
	}
	
	override fun getIconIndex(stack: ItemStack): IIcon {
		return textures[if (stack.hasDisplayName()) 1 else 0]
	}
	
	override fun getIcon(stack: ItemStack, pass: Int): IIcon {
		return getIconIndex(stack)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":Paper")
		textures[1] = reg.registerIcon(ModInfo.MODID + ":PaperSigned")
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		if (!AlfheimCore.enableMMO) return stack
		if (!world!!.isRemote) {
			val name = getCompound(stack, "display", false).getString("Name")
			val pt = PartySystem.getParty(player)
			val pl = pt!!.pl
			val flag1 = name != null && !name.isEmpty()
			val flag2 = flag1 && name!!.equals(player!!.commandSenderName, ignoreCase = true)
			
			if (pl != null && player != pl && !flag2) {
				ASJUtilities.say(player!!, "alfheimmisc.party.notpl")
				return stack
			}
			
			if (flag1) {
				if (pt.remove(name))
					--stack.stackSize
				else
					player!!.addChatMessage(ChatComponentText(StatCollector.translateToLocalFormatted("alfheimmisc.party.notinpartyoffline", name)))
				return stack
			}
			
			return stack
		}
		return stack
	}
	
	companion object {
		
		val textures = arrayOfNulls<IIcon>(2)
	}
}
