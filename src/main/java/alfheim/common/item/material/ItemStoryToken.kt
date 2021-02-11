package alfheim.common.item.material

import alexsocol.asjlib.meta
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.ItemMod
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.recipe.IElvenItem
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemStoryToken: ItemMod("StoryToken"), IElvenItem {
	
	lateinit var icons: Array<IIcon>
	
	init {
		creativeTab = AlfheimTab
		setHasSubtypes(true)
		setMaxStackSize(1)
	}
	
	override fun registerIcons(reg: IIconRegister) {
		icons = Array(2) { IconHelper.forItem(reg, this, it) }
	}
	
	override fun getIconFromDamage(meta: Int): IIcon {
		return icons[if (meta == 1) 1 else 0]
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, slotID: Int, inHand: Boolean) {
		if (world.isRemote) return
		
		if (stack.meta == 1 && !ItemNBTHelper.verifyExistance(stack, TAG_STORY))
			ItemNBTHelper.setInt(stack, TAG_STORY, world.rand.nextInt(AlfheimConfigHandler.storyLines) + 1)
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, info: MutableList<Any?>, adv: Boolean) {
		if (stack.meta == 1) {
			info.add(StatCollector.translateToLocal("item.StoryToken.story.${ItemNBTHelper.getInt(stack, TAG_STORY, 0)}"))
		}
	}
	
	override fun isElvenItem(stack: ItemStack) = stack.meta == 1
	
	companion object {
		
		val TAG_STORY = "story"
	}
}
