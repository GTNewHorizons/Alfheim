package alfheim.common.item.equipment.bauble

import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.client.event.RenderPlayerEvent
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania

class ItemCreativeReachPendant: ItemPendant("CreativeReachPendant") {
	
	override fun onEquippedOrLoadedIntoWorld(stack: ItemStack?, player: EntityLivingBase?) {
		Botania.proxy.setExtraReach(player, 100f)
	}
	
	override fun onUnequipped(stack: ItemStack?, player: EntityLivingBase?) {
		Botania.proxy.setExtraReach(player, -100f)
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack): String {
		val s = this.getUnlocalizedName(stack)
		return if (s == null) "" else StatCollector.translateToLocal(s)
	}
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		// NO-OP
	}
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
}
