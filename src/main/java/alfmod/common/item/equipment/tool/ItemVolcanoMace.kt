package alfmod.common.item.equipment.tool

import alfmod.common.core.helper.IconHelper
import alfmod.common.core.util.AlfheimModularTab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword

class ItemVolcanoMace: ItemManasteelSword(volcano, "VolcanoMace") {
	
	init {
		creativeTab = AlfheimModularTab
	}
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
	}
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.setFire(10)
		return super.hitEntity(stack, target, attacker)
	}
	
	companion object {
		val volcano = EnumHelper.addToolMaterial("Volcano", 0, 1200, 6f, 6f, 6)!!
	}
}