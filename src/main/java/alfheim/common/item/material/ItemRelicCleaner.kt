package alfheim.common.item.material

import alfheim.AlfheimCore
import alfheim.common.item.ItemMod
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class ItemRelicCleaner: ItemMod("relicCleaner") {
	
	val TAG_UUID = "uuid"
	
	init {
		creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, target: Entity): Boolean {
		return if (target is EntityPlayer)
			ItemNBTHelper.setString(stack, TAG_UUID, target.commandSenderName).let { true }
		else
			super.onLeftClickEntity(stack, player, target)
	}
}
